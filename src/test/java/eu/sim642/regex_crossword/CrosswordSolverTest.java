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
        assertEquals(" ", CrosswordSolver.solve(crossword));
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

    @Test
    void doublecross1() {
        Crossword crossword = new Crossword(List.of(
                "[A-GN-Z]+"
        ), List.of(
                "[D-HJ-M]",
                "[^A-RU-Z]"
        ));
        Crossword doubleCrossword = new Crossword(List.of(
                "[^A-DI-S]+"
        ), List.of(
                "[^F-KM-Z]",
                "[A-KS-V]"
        ));
        assertEquals("ET", CrosswordSolver.solve(crossword, doubleCrossword));
    }

    @Test
    void doublecross2() {
        Crossword crossword = new Crossword(List.of(
                "(CAT|A-T)+",
                "[MA\\-\\sE]+"
        ), List.of(
                "[^MCI]+",
                ".A",
                "(TM|BF)"
        ));
        Crossword doubleCrossword = new Crossword(List.of(
                "[^KI\\sP]+",
                "(M|APS|EA)*"
        ), List.of(
                "[AI][E\\s]",
                "[A\\-Z]+",
                "[\\sT\\-M]+"
        ));
        assertEquals("A-TEAM", CrosswordSolver.solve(crossword, doubleCrossword));
    }

    @Test
    void doublecross3() {
        Crossword crossword = new Crossword(List.of(
                "[ONE]*[SKA]",
                ".*(RE|ER)",
                "A+[TUB]*"
        ), List.of(
                ".*[GAF]*",
                "(P|ET|O|TEA)*",
                "[RUSH]+"
        ));
        Crossword doubleCrossword = new Crossword(List.of(
                ".*(O|S)*",
                "[^GOA]*",
                "[STUPA]+"
        ), List.of(
                "(NF|FA|A|FN)+",
                ".*(A|E|I).*",
                "[SUPER]*"
        ));
        assertEquals("NOSFERATU", CrosswordSolver.solve(crossword, doubleCrossword));
    }

    @Test
    void cities1() {
        Crossword crossword = new Crossword(List.of(
                "[LINE]+",
                "[LAM]+"
        ), List.of(
                "(MA|LM)",
                "[^MESH]+"
        ));
        Crossword doubleCrossword = new Crossword(List.of(
                "[ISLE]+",
                "[MALE]+"
        ), List.of(
                "[LAME]*",
                "[^LES]+"
        ));
        assertEquals("LIMA", CrosswordSolver.solve(crossword, doubleCrossword));
    }

    @Test
    void cities3() {
        Crossword crossword = new Crossword(List.of(
                "(W\\s|NE|PS)*",
                "[YOU]{2}[ARK]+"
        ), List.of(
                "[NAYE]*",
                "(EO|N\\s)",
                "[WRONG]*",
                "(K|R|I|M|\\s)+"
        ));
        Crossword doubleCrossword = new Crossword(List.of(
                "[END\\sWITH]+",
                ".+R.*"
        ), List.of(
                "[YEN]+",
                "[ETPHONE\\s]+",
                "[^GONE\\s]+",
                "[\\sANKT]+"
        ));
        assertEquals("NEW YORK", CrosswordSolver.solve(crossword, doubleCrossword));
    }

    @Test
    void cities4() {
        Crossword crossword = new Crossword(List.of(
                "[ALK\\sU]+(P|AL|E)+",
                "[ABC]\\s(LU|LP)]*",
                "(RU|MP|UR|S)+"
        ), List.of(
                "(KA|LK)[MNO]",
                ".+(AL|P)",
                "[^PIM\\s]+",
                "[LP].*[PR]"
        ));
        Crossword doubleCrossword = new Crossword(List.of(
                "[LUKE]{2}[LAKE\\s]+",
                ".*[PUL]+",
                "[RUMPUS]*"
        ), List.of(
                "[KRAMP]+",
                "U[PLAIN\\s]{2}",
                "[ALURE]+",
                "[RULE\\s]+"
        ));
        assertEquals("KUALA LUMPUR", CrosswordSolver.solve(crossword, doubleCrossword));
    }

    @Test
    void cities5() {
        Crossword crossword = new Crossword(List.of(
                "[^SEAP]+",
                "[\\sPIN]E[NET]",
                "[^ONE\\s]+",
                "[END\\s]+"
        ), List.of(
                "[HELP\\s]+",
                "[^\\sPONG]+N",
                "[SONG]+\\s"
        ));
        Crossword doubleCrossword = new Crossword(List.of(
                "[\\sCOPE]+",
                "[^HI\\s]+",
                "[\\sHAG]+",
                "[^SODA]+"
        ), List.of(
                "[^SLIC]+\\w",
                "[OCEAN\\s]+",
                "[^SPIES]+"
        ));
        assertEquals(" COPENHAGEN ", CrosswordSolver.solve(crossword, doubleCrossword));
    }

    @Test
    void volapük1() {
        Crossword crossword = new Crossword(List.of(
                "(PO|R|G|E)*",
                "(Z|OO)[KINZS]+"
        ), List.of(
                "[GRAPEZ]+",
                "[^ION\\sS]+",
                "(ZO|OS|OP)"
        ));
        Crossword doubleCrossword = new Crossword(List.of(
                "[GRA]P+.*",
                "(X|Y|Z)[KIS\\s]+"
        ), List.of(
                "[GIN].",
                "[SPIK\\s]*",
                "[^ZYP]+"
        ));
        assertEquals("GPOZKS", CrosswordSolver.solve(crossword, doubleCrossword));
    }

    @Test
    void hamlet3() {
        Crossword crossword = new Crossword(List.of(
                "[HEL\\s]+P.+",
                "[MI/SON]+[^OLDE]{4}",
                "[IN'THE\\.\\s]+",
                ".[A-G]+(R|D)+[END]+"
        ), List.of(
                "[O-S\\sG-L]+",
                "[ANTIGE]+",
                "(S\\s|\\sS|'A)+",
                "[PI\\sRD]+",
                "(TD|L|LO|O|OH)+",
                "[HITE'\\s]+",
                "[MENDS]+"
        ));
        Crossword doubleCrossword = new Crossword(List.of(
                ".[SEPOLI\\s]+",
                ".{3,4}(\\sH|\\s|IM)+",
                "[IT'\\s]{4}[H.TE]+",
                ".{4}(NI|TE|N|DE)+"
        ), List.of(
                "(\\s\\s|OR|HO|ME)+",
                "[A-G]N+(GI|IG|PI)",
                "[RAM\\sES']+",
                "[^AINED]+",
                "[HORTED]+",
                "[F-K]{2}[F-M]..?",
                "(S|I|MS)[MYEND]*"
        ));
        assertEquals("HE POISONS HIM I' THE GARDEN", CrosswordSolver.solve(crossword, doubleCrossword));
    }
}
