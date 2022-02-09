package eu.sim642.regex_crossword;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CrosswordSolverTest {

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
