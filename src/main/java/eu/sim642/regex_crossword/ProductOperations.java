package eu.sim642.regex_crossword;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.StatePair;
import dk.brics.automaton.Transition;

import java.util.HashMap;
import java.util.Map;

public final class ProductOperations {

    private ProductOperations() {

    }

    public static Automaton guarded(Automaton a, Automaton g) {
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
