public class Rook extends Pieces {
    public Rook(int pieceColor) {
        super(ROOK, pieceColor);
    }

    @Override
    public boolean isValidMove(int targetRow, int targetCol) {
        // Check if the target square is occupied by a piece of the same team
        if (Board.SQUARE[targetRow][targetCol] != null &&
                Movement.isSameTeam(this, Board.SQUARE[targetRow][targetCol])) {
            return false;
        }
        if (row == targetRow || col == targetCol) {
            if (row == targetRow) {
                int startCol = Math.min(col, targetCol);
                int endCol = Math.max(col, targetCol);
                for (int newCol = startCol + 1; newCol < endCol; newCol++) {
                    if (Board.SQUARE[row][newCol] != null) {
                        return false; // There is a piece in the way
                    }
                }
            } else { // Moving along columns
                int startRow = Math.min(row, targetRow);
                int endRow = Math.max(row, targetRow);
                for (int newRow = startRow + 1; newRow < endRow; newRow++) {
                    if (Board.SQUARE[newRow][col] != null) {
                        return false; // There is a piece in the way
                    }
                }
            }
            return true; // No pieces in the way
        }
        return false; // Not moving along a row or column
    }
}

