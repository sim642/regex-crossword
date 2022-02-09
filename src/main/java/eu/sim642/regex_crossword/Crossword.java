package eu.sim642.regex_crossword;

import java.util.ArrayList;
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

    public Crossword intersect(Crossword that) {
        assert this.width() == that.width();
        assert this.height() == that.height();

        List<String> rows = new ArrayList<>();
        for (int y = 0; y < height(); y++)
            rows.add(String.format("(%s)&(%s)", this.rows.get(y), that.rows.get(y)));

        List<String> cols = new ArrayList<>();
        for (int x = 0; x < width(); x++)
            cols.add(String.format("(%s)&(%s)", this.cols.get(x), that.cols.get(x)));

        return new Crossword(rows, cols);
    }
}
