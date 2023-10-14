package simulation.utils;

public class Location {
    private final int row;
    private final int col;
    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public int getRow() { return row; }
    public int getCol() { return col; }

    public int getCircle(Location target) {
        //fixme: think about round world- 35,0 should be circle=1
        int rowDiff = Math.abs(this.row - target.row);
        int colDiff = Math.abs(this.col - target.col);
        return Math.max(rowDiff, colDiff);
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
