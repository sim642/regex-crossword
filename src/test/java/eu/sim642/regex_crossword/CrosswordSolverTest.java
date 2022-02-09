package eu.sim642.regex_crossword;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CrosswordSolverTest {

    @Test
    void tutorial1() {
        Crossword crossword = new Crossword(List.of("A|Z"), List.of("A|B"));
        assertEquals("A", CrosswordSolver.solve(crossword));
    }

    @Test
    void tutorial2() {
        Crossword crossword = new Crossword(List.of("[BDF]"), List.of("[ABC]"));
        assertEquals("B", CrosswordSolver.solve(crossword));
    }

    @Test
    void tutorial3() {
        Crossword crossword = new Crossword(List.of("[ABC]"), List.of("[^AB]"));
        assertEquals("C", CrosswordSolver.solve(crossword));
    }

    @Test
    void tutorial4() {
        Crossword crossword = new Crossword(List.of(
                "A",
                "AB*"
        ), List.of(
                "A*"
        ));
        assertEquals("AA", CrosswordSolver.solve(crossword));
    }

    @Test
    void tutorial5() {
        Crossword crossword = new Crossword(List.of(
                "A|C",
                "B"
        ), List.of(
                "A?B?"
        ));
        assertEquals("AB", CrosswordSolver.solve(crossword));
    }

    @Test
    void tutorial6() {
        Crossword crossword = new Crossword(List.of(
                "A|B",
                "A|Z"
        ), List.of(
                "A+"
        ));
        assertEquals("AA", CrosswordSolver.solve(crossword));
    }

    @Test
    void tutorial8() {
        Crossword crossword = new Crossword(List.of(
                "A{1}",
                "B|A"
        ), List.of(
                "A{2,}"
        ));
        assertEquals("AA", CrosswordSolver.solve(crossword));
    }

    @Test
    void tutorial9() {
        Crossword crossword = new Crossword(List.of("\\s"), List.of("A|\\s"));
        assertEquals(" ", CrosswordSolver.solve(crossword)); // TODO: 09.02.22 non-unique testing
    }

    @Test
    void beginner1() {
        Crossword crossword = new Crossword(List.of(
                "HE|LL|O+",
                "[PLEASE]+"
        ), List.of(
                "[^SPEAK]+",
                "EP|IP|EF"
        ));
        assertEquals("HELP", CrosswordSolver.solve(crossword));
    }

    @Test
    void beginner4() {
        Crossword crossword = new Crossword(List.of(
                "[*]+",
                "/+"
        ), List.of(
                ".?.+",
                ".+"
        ));
        assertEquals("**//", CrosswordSolver.solve(crossword));
    }

    @Test
    void beginner5() {
        Crossword crossword = new Crossword(List.of(
                "18|19|20",
                "[6789]\\d"
        ), List.of(
                "\\d[2480]",
                "56|94|73"
        ));
        assertEquals("1984", CrosswordSolver.solve(crossword));
    }

    @Test
    void intermediate1() {
        Crossword crossword = new Crossword(List.of(
                "[NOTAD]*",
                "WEL|BAL|EAR"
        ), List.of(
                "UB|IE|AW",
                "[TUBE]*",
                "[BORF]."
        ));
        assertEquals("ATOWEL", CrosswordSolver.solve(crossword));
    }

    @Test
    void intermediate2() {
        Crossword crossword = new Crossword(List.of(
                "[AWE]+",
                "[ALP]+K",
                "(PR|ER|EP)"
        ), List.of(
                "[BQW](PR|LE)",
                "[RANK]+"
        ));
        assertEquals("WALKER", CrosswordSolver.solve(crossword));
    }

    @Test
    void intermediate4() {
        Crossword crossword = new Crossword(List.of(
                "[DEF][MNO]*",
                "[^DJNU]P[ABC]",
                "[ICAN]*"
        ), List.of(
                "[JUNDT]*",
                "APA|OPI|OLK",
                "(NA|FE|HE)[CV]"
        ));
        assertEquals("DONTPANIC", CrosswordSolver.solve(crossword));
    }

    @Test
    void palindromeda2() {
        Crossword crossword = new Crossword(List.of(
                "(L|E|D|G|Y)*",
                "(A|E|J)*Y.*",
                "[FLEDG]*"
        ), List.of(
                "(GE|PE)[AL]*",
                "[EAF]+(YE|AB)*",
                "(QR|LE)(G|I|M|P)"
        ));
        assertEquals("GELEYELEG", CrosswordSolver.solve(crossword));
    }

    @Test
    void palindromeda3() {
        Crossword crossword = new Crossword(List.of(
                "[TRASH]*",
                "(FA|AB)[TUP]*",
                "(BA|TH|TU)*",
                ".*A.*"
        ), List.of(
                "(TS|RA|QA)*",
                "(AB|UT|AR)*",
                "(K|T)U.*(A|R)",
                "(AR|FS|ST)+"
        ));
        assertEquals("RATSABUTTUBASTAR", CrosswordSolver.solve(crossword));
    }

    @Test
    void palindromeda5() {
        Crossword crossword = new Crossword(List.of(
                "(EP|ST)*",
                "T[A-Z]*",
                ".M.T",
                ".*P.[S-X]+"
        ), List.of(
                ".*E.*",
                "[^P]I(IT|ME)",
                "(EM|FE)(IT|IP)",
                "(TS|PE|KE)*"
        ));
        assertEquals("STEPTIMEEMITPETS", CrosswordSolver.solve(crossword));
    }
}
