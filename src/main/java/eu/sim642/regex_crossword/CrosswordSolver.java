package eu.sim642.regex_crossword;

import dk.brics.automaton.*;

import java.util.ArrayList;
import java.util.List;

public class CrosswordSolver {

    public static RegExp createRegExp(String str) {
        // https://en.wikipedia.org/wiki/Regular_expression#Character_classes
        str = str.replaceAll("\\\\s", "[ \\t\\r\\n\\v\\f]");
        str = str.replaceAll("\\\\d", "[0-9]");
        return new RegExp(str);
    }

    public static String solve(Crossword crossword) {
        List<Automaton> rowAutos = new ArrayList<>();
        Automaton anyCharWidth = BasicAutomata.makeAnyChar().repeat(crossword.width(), crossword.width());
        for (int y = 0; y < crossword.height(); y++) {
            RegExp row = createRegExp(crossword.rows().get(y));
            rowAutos.add(anyCharWidth.repeat(y, y).concatenate(row.toAutomaton().intersection(anyCharWidth)).concatenate(anyCharWidth.repeat(crossword.height() - y - 1, crossword.height() - y - 1)));
        }

        Automaton rowsAuto = rowAutos.stream().reduce(Automaton::intersection).get();
        //rowsAuto.minimize();
        //System.out.println(rowsAuto);
        //System.out.println(rowsAuto.getFiniteStrings());

        List<Automaton> colAutos = new ArrayList<>();
        //Automaton anyCharHeight = BasicAutomata.makeAnyChar().repeat(height, height);
        for (int x = 0; x < crossword.width(); x++) {
            RegExp col = createRegExp(crossword.cols().get(x));
            Automaton g = BasicAutomata.makeAnyChar().repeat(x, x).concatenate(anyCharWidth.repeat());
            Automaton guarded = ProductOperations.guarded(col.toAutomaton(), g);
            //guarded.minimize();
            //System.out.println(guarded);
            colAutos.add(guarded);
        }
        Automaton colsAuto = colAutos.stream().reduce(Automaton::intersection).get();
        //colsAuto.minimize();
        //System.out.println(colsAuto);
        //System.out.println(colsAuto.getFiniteStrings());

        Automaton solAuto = rowsAuto.intersection(colsAuto);
        solAuto.minimize();
        //System.out.println(solAuto);
        //System.out.println(solAuto.getFiniteStrings());
        return solAuto.getShortestExample(true);
    }

    // TODO: 09.02.22 double-cross
}
