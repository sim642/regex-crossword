package eu.sim642.regex_crossword;

import dk.brics.automaton.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegexCrossword {
    public static void main(String[] args) {
        //List<RegExp> rows = List.of(
        //        new RegExp("HE|LL|O+"),
        //        new RegExp("[PLEASE]+")
        //);
        //List<RegExp> cols = List.of(
        //        new RegExp("[^SPEAK]+"),
        //        new RegExp("EP|IP|EF")
        //);
        //List<RegExp> rows = List.of(
        //        new RegExp("[NOTAD]*"),
        //        new RegExp("WEL|BAL|EAR")
        //);
        //List<RegExp> cols = List.of(
        //        new RegExp("UB|IE|AW"),
        //        new RegExp("[TUBE]*"),
        //        new RegExp("[BORF].")
        //);
        List<RegExp> rows = List.of(
                new RegExp("(EP|ST)*"),
                new RegExp("T[A-Z]*"),
                new RegExp(".M.T"),
                new RegExp(".*P.[S-X]+")
        );
        List<RegExp> cols = List.of(
                new RegExp(".*E.*"),
                new RegExp("[^P]I(IT|ME)"),
                new RegExp("(EM|FE)(IT|IP)"),
                new RegExp("(TS|PE|KE)*")
        );

        int width = cols.size();
        int height = rows.size();

        List<Automaton> rowAutos = new ArrayList<>();
        Automaton anyCharWidth = BasicAutomata.makeAnyChar().repeat(width, width);
        for (int y = 0; y < rows.size(); y++) {
            RegExp row = rows.get(y);
            rowAutos.add(anyCharWidth.repeat(y, y).concatenate(row.toAutomaton().intersection(anyCharWidth)).concatenate(anyCharWidth.repeat(height - y - 1, height - y - 1)));
        }

        Automaton rowsAuto = rowAutos.stream().reduce(Automaton::intersection).get();
        //rowsAuto.minimize();
        //System.out.println(rowsAuto);
        //System.out.println(rowsAuto.getFiniteStrings());

        List<Automaton> colAutos = new ArrayList<>();
        //Automaton anyCharHeight = BasicAutomata.makeAnyChar().repeat(height, height);
        for (int x = 0; x < cols.size(); x++) {
            RegExp col = cols.get(x);
            Automaton g = BasicAutomata.makeAnyChar().repeat(x, x).concatenate(anyCharWidth.repeat());
            Automaton guarded = guarded(col.toAutomaton(), g);
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
        System.out.println(solAuto);
        System.out.println(solAuto.getFiniteStrings());
    }

    private static Automaton guarded(Automaton a, Automaton g) {
        Automaton r = new Automaton();
        Map<StatePair, State> stateMap = new HashMap<>();
        for (State aState : a.getStates()) {
            for (State gState : g.getStates()) {
                State rState = new State();
                rState.setAccept(aState.isAccept());
                stateMap.put(new StatePair(aState, gState), rState);
            }
        }
        r.setInitialState(stateMap.get(new StatePair(a.getInitialState(), g.getInitialState())));

        for (State gStateFrom : g.getStates()) {
            for (Transition gTransition : gStateFrom.getTransitions()) {
                State gStateTo = gTransition.getDest();
                char gMin = gTransition.getMin();
                char gMax = gTransition.getMax();
                for (State aStateFrom : a.getStates()) {
                    State rStateFrom = stateMap.get(new StatePair(aStateFrom, gStateFrom));
                    if (gStateFrom.isAccept()) {
                        for (Transition aTransition : aStateFrom.getTransitions()) {
                            State aStateTo = aTransition.getDest();
                            char aMin = aTransition.getMin();
                            char aMax = aTransition.getMax();
                            char rMin = aMin > gMin ? aMin : gMin;
                            char rMax = aMax < gMax ? aMax : gMax;
                            if (rMin <= rMax) {
                                State rStateTo = stateMap.get(new StatePair(aStateTo, gStateTo));
                                Transition rTransition = new Transition(rMin, rMax, rStateTo);
                                rStateFrom.addTransition(rTransition);
                            }
                        }
                    }
                    else {
                        State aStateTo = aStateFrom;
                        State rStateTo = stateMap.get(new StatePair(aStateTo, gStateTo));
                        Transition rTransition = new Transition(gMin, gMax, rStateTo);
                        rStateFrom.addTransition(rTransition);
                    }
                }
            }
        }

        r.removeDeadTransitions();
        r.minimize();
        return r;
    }
}
