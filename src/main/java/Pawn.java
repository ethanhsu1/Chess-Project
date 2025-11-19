public class Pawn extends Pieces {

    public Pawn(int pieceColor) {
        super(Pieces.PAWN, pieceColor);
    }

    @Override
    public boolean isValidMove(int targetRow, int targetCol) {
        // Check if the target square is occupied by a piece of the same team
        if (Board.SQUARE[targetRow][targetCol] != null &&
                Movement.isSameTeam(this, Board.SQUARE[targetRow][targetCol])) {
            return false;
        }

        // Pawns can only move forward
        int direction = (this.getPieceColor() == Pieces.WHITE) ? -1 : 1;

        // Moving forward
        if (targetCol == this.getCol() && targetRow == this.getRow() + direction) {
            // Normal move one square ahead
            if (Board.SQUARE[targetRow][targetCol] == null) {
                return true;
            }
        } else if (targetCol == this.getCol() && targetRow == this.getRow() + 2 * direction && this.firstMove) {
            // First move option, move two squares ahead
            if (Board.SQUARE[targetRow][targetCol] == null &&
                    Board.SQUARE[this.getRow() + direction][targetCol] == null) {
                return true;
            }
        } else if (Math.abs(targetCol - this.getCol()) == 1 && targetRow == this.getRow() + direction) {
            // Diagonal capture
            if (Board.SQUARE[targetRow][targetCol] != null) {
                return true;
            }
        }

        // Invalid move
        return false;
    }
}
