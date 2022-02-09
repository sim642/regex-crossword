package eu.sim642.regex_crossword;

import dk.brics.automaton.*;

import java.util.ArrayList;
import java.util.List;

public class CrosswordSolver {

    public static Automaton regExpAutomaton(String str) {
        return new AutomatonRegExp(str).toAutomaton();
    }

    private static Automaton repeatExact(Automaton automaton, int count) {
        return automaton.repeat(count, count);
    }

    public static String solve(Crossword crossword) {
        Automaton anyCharWidth = repeatExact(BasicAutomata.makeAnyChar(), crossword.width());

        List<Automaton> rowAutos = new ArrayList<>();
        for (int y = 0; y < crossword.height(); y++) {
            Automaton row = regExpAutomaton(crossword.rows().get(y));
            Automaton prefix = repeatExact(anyCharWidth, y);
            Automaton rowWidth = row.intersection(anyCharWidth);
            Automaton suffix = repeatExact(anyCharWidth, crossword.height() - y - 1);
            rowAutos.add(prefix.concatenate(rowWidth).concatenate(suffix));
        }

        Automaton rowsAuto = rowAutos.stream().reduce(Automaton::intersection).get();

        List<Automaton> colAutos = new ArrayList<>();
        for (int x = 0; x < crossword.width(); x++) {
            Automaton col = regExpAutomaton(crossword.cols().get(x));
            Automaton offset = repeatExact(BasicAutomata.makeAnyChar(), x);
            Automaton guard = offset.concatenate(anyCharWidth.repeat());
            colAutos.add(ProductOperations.guarded(col, guard));
        }

        Automaton colsAuto = colAutos.stream().reduce(Automaton::intersection).get();

        Automaton solAuto = rowsAuto.intersection(colsAuto);
        //solAuto.minimize();
        return solAuto.getShortestExample(true);
    }

    public static String solve(Crossword crossword, Crossword doubleCrossword) {
        return solve(crossword.intersect(doubleCrossword));
    }
}
