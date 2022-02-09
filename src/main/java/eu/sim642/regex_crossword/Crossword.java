package eu.sim642.regex_crossword;

import java.util.List;

public record Crossword(
        List<String> rows,
        List<String> cols
) {

    public int width() {
        return cols.size();
    }

    public int height() {
        return rows.size();
    }
}
