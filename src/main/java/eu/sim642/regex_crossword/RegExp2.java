/*
 * dk.brics.automaton
 * 
 * Copyright (c) 2001-2017 Anders Moeller
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package eu.sim642.regex_crossword;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicAutomata;

public class RegExp2 {

	/**
	 * Syntax flag, enables intersection (<code>&amp;</code>).
	 */
	public static final int INTERSECTION = 0x0001;

	/**
	 * Syntax flag, enables complement (<code>~</code>).
	 */
	public static final int COMPLEMENT = 0x0002;

	/**
	 * Syntax flag, enables empty language (<code>#</code>).
	 */
	public static final int EMPTY = 0x0004;

	/**
	 * Syntax flag, enables all optional regexp syntax.
	 */
	public static final int ALL = 0xffff;

	/**
	 * Syntax flag, enables no optional regexp syntax.
	 */
	public static final int NONE = 0x0000;

	private static boolean allow_mutation = false;

	String b;
	int flags;
	int pos;

	private boolean minimize;

	/**
	 * Constructs new <code>RegExp</code> from a string.
	 * Same as <code>RegExp(s, ALL)</code>.
	 * @param s regexp string
	 * @exception IllegalArgumentException if an error occured while parsing the regular expression
	 */
	public RegExp2(String s) throws IllegalArgumentException {
		this(s, ALL);
	}

	/**
	 * Constructs new <code>RegExp</code> from a string.
	 * @param s regexp string
	 * @param syntax_flags boolean 'or' of optional syntax constructs to be enabled
	 * @exception IllegalArgumentException if an error occured while parsing the regular expression
	 */
	public RegExp2(String s, int syntax_flags) {
		b = s;
		flags = syntax_flags;
	}

	public Automaton toAutomaton() {
		return toAutomaton(true);
	}

	public Automaton toAutomaton(boolean minimize) {
		this.minimize = minimize;
		boolean b = false;
		if (allow_mutation)
			b = Automaton.setAllowMutate(true); // thread unsafe
		Automaton a;
		if (this.b.length() == 0)
			a = BasicAutomata.makeEmptyString();
		else {
			a = parseUnionExp();
			if (pos < this.b.length())
				throw new IllegalArgumentException("end-of-string expected at position " + pos);
		}
		if (allow_mutation)
			Automaton.setAllowMutate(b);
		this.b = null;
		return a;
	}

	Automaton makeUnion(Automaton exp1, Automaton exp2) {
		Automaton a = exp1.union(exp2);
		if (minimize)
			a.minimize();
		return a;
	}

	Automaton makeConcatenation(Automaton exp1, Automaton exp2) {
		Automaton a = exp1.concatenate(exp2);
		if (minimize)
			a.minimize();
		return a;
	}

	Automaton makeIntersection(Automaton exp1, Automaton exp2) {
		Automaton a = exp1.intersection(exp2);
		if (minimize)
			a.minimize();
		return a;
	}

	Automaton makeOptional(Automaton exp) {
		Automaton a = exp.optional();
		if (minimize)
			a.minimize();
		return a;
	}

	Automaton makeRepeat(Automaton exp) {
		Automaton a = exp.repeat();
		if (minimize)
			a.minimize();
		return a;
	}

	Automaton makeRepeat(Automaton exp, int min) {
		Automaton a = exp.repeat(min);
		if (minimize)
			a.minimize();
		return a;
	}

	Automaton makeRepeat(Automaton exp, int min, int max) {
		Automaton a = exp.repeat(min, max);
		if (minimize)
			a.minimize();
		return a;
	}

	Automaton makeComplement(Automaton exp) {
		Automaton a = exp.complement();
		if (minimize)
			a.minimize();
		return a;
	}

	static Automaton makeChar(char c) {
		return BasicAutomata.makeChar(c);
	}

	static Automaton makeCharRange(char from, char to) {
		return BasicAutomata.makeCharRange(from, to);
	}

	static Automaton makeAnyChar() {
		return BasicAutomata.makeAnyChar();
	}

	static Automaton makeEmpty() {
		return BasicAutomata.makeEmpty();
	}

	private boolean peek(String s) {
		return more() && s.indexOf(b.charAt(pos)) != -1;
	}

	private boolean match(char c) {
		if (pos >= b.length())
			return false;
		if (b.charAt(pos) == c) {
			pos++;
			return true;
		}
		return false;
	}

	private boolean more() {
		return pos < b.length();
	}

	private char next() throws IllegalArgumentException {
		if (!more())
			throw new IllegalArgumentException("unexpected end-of-string");
		return b.charAt(pos++);
	}

	private boolean check(int flag) {
		return (flags & flag) != 0;
	}

	final Automaton parseUnionExp() throws IllegalArgumentException {
		Automaton e = parseInterExp();
		if (match('|'))
			e = makeUnion(e, parseUnionExp());
		return e;
	}

	final Automaton parseInterExp() throws IllegalArgumentException {
		Automaton e = parseConcatExp();
		if (check(INTERSECTION) && match('&'))
			e = makeIntersection(e, parseInterExp());
		return e;
	}

	final Automaton parseConcatExp() throws IllegalArgumentException {
		Automaton e = parseRepeatExp();
		if (more() && !peek(")|") && (!check(INTERSECTION) || !peek("&")))
			e = makeConcatenation(e, parseConcatExp());
		return e;
	}

	final Automaton parseRepeatExp() throws IllegalArgumentException {
		Automaton e = parseComplExp();
		while (peek("?*+{")) {
			if (match('?'))
				e = makeOptional(e);
			else if (match('*'))
				e = makeRepeat(e);
			else if (match('+'))
				e = makeRepeat(e, 1);
			else if (match('{')) {
				int start = pos;
				while (peek("0123456789"))
					next();
				if (start == pos)
					throw new IllegalArgumentException("integer expected at position " + pos);
				int n = Integer.parseInt(b.substring(start, pos));
				int m = -1;
				if (match(',')) {
					start = pos;
					while (peek("0123456789"))
						next();
					if (start != pos)
						m = Integer.parseInt(b.substring(start, pos));
				} else
					m = n;
				if (!match('}'))
					throw new IllegalArgumentException("expected '}' at position " + pos);
				if (m == -1)
					e = makeRepeat(e, n);
				else
					e = makeRepeat(e, n, m);
			}
		}
		return e;
	}

	final Automaton parseComplExp() throws IllegalArgumentException {
		if (check(COMPLEMENT) && match('~'))
			return makeComplement(parseComplExp());
		else
			return parseCharClassExp();
	}

	final Automaton parseCharClassExp() throws IllegalArgumentException {
		if (match('[')) {
			boolean negate = false;
			if (match('^'))
				negate = true;
			Automaton e = parseCharClasses();
			if (negate)
				e = makeIntersection(makeAnyChar(), makeComplement(e));
			if (!match(']'))
				throw new IllegalArgumentException("expected ']' at position " + pos);
			return e;
		} else
			return parseSimpleExp();
	}

	final Automaton parseCharClasses() throws IllegalArgumentException {
		Automaton e = parseCharClass();
		while (more() && !peek("]"))
			e = makeUnion(e, parseCharClass());
		return e;
	}

	final Automaton parseCharClass() throws IllegalArgumentException {
		char c = parseCharExp();
		if (match('-'))
			if (peek("]"))
                return makeUnion(makeChar(c), makeChar('-'));
            else
                return makeCharRange(c, parseCharExp());
		else
			return makeChar(c);
	}

	final Automaton parseSimpleExp() throws IllegalArgumentException {
		if (match('.'))
			return makeAnyChar();
		else if (check(EMPTY) && match('#'))
			return makeEmpty();
		else if (match('(')) {
			if (match(')'))
				return makeEmpty();
			Automaton e = parseUnionExp();
			if (!match(')'))
				throw new IllegalArgumentException("expected ')' at position " + pos);
			return e;
		} else
			return makeChar(parseCharExp());
	}

	final char parseCharExp() throws IllegalArgumentException {
		match('\\');
		return next();
	}
}
