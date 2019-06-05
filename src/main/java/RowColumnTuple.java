import java.util.Objects;

public class RowColumnTuple {
    private int row;
    private int column;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public RowColumnTuple(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RowColumnTuple)) return false;
        RowColumnTuple that = (RowColumnTuple) o;
        return getRow() == that.getRow() &&
                getColumn() == that.getColumn();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getColumn());
    }

    @Override
    public String toString() {
        return "{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }
}
