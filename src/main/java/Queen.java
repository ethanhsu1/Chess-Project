public class Queen extends Pieces {
    public Queen(int pieceColor) {

        super(QUEEN, pieceColor);
    }

    public boolean isValidBishop(int targetRow, int targetCol) {
        int rowDiff = Math.abs(targetRow - row);
        int colDiff = Math.abs(targetCol - col);

        if (rowDiff != colDiff) {
            return false; // Not a diagonal move
        }

        // Check if there are any pieces in the path
        int rowIncrement = (targetRow > row) ? 1 : -1;
        int colIncrement = (targetCol > col) ? 1 : -1;
        int newRow = row + rowIncrement;
        int newCol = col + colIncrement;

        while (newRow != targetRow && newCol != targetCol && newRow >= 0 && newCol >= 0) {
            if (Board.SQUARE[newRow][newCol] != null) {
                return false; // There is a piece in the path
            }
            newRow += rowIncrement;
            newCol += colIncrement;
        }

        // Check if destination square is empty or has an enemy piece
        Pieces destinationPiece = Board.SQUARE[targetRow][targetCol];
        if (destinationPiece != null && destinationPiece.getPieceColor() == getPieceColor()) {
            return false; // Trying to capture an allied piece
        }

        return true;
    }

    public boolean isValidRook(int targetRow, int targetCol) {
        int rowDiff = targetRow - row;
        int colDiff = targetCol - col;
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


    @Override
    public boolean isValidMove(int targetRow, int targetCol) {
        return isValidBishop(targetRow, targetCol) || isValidRook(targetRow, targetCol);
    }
}



