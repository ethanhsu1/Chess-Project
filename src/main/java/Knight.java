public class Knight extends Pieces{
    public Knight(int pieceColor) {

        super(KNIGHT, pieceColor);
    }
    @Override
    public boolean isValidMove(int targetRow, int targetCol) {
        if (Board.SQUARE[targetRow][targetCol] != null &&
                Movement.isSameTeam(this, Board.SQUARE[targetRow][targetCol])) {
            return false;
        }
        int rowDiff = Math.abs(targetRow - row);
        int colDiff = Math.abs(targetCol - col);

        // A knight moves in a specific L shape, either two squares horizontally and one square vertically,
        // or two squares vertically and one square horizontally.
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }
}

