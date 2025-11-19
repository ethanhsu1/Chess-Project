public class King extends Pieces {
    public King(int pieceColor) {

        super(KING, pieceColor);
    }


    @Override
public boolean isValidMove(int targetRow, int targetCol) {
    if (Board.SQUARE[targetRow][targetCol] != null &&
        Movement.isSameTeam(this, Board.SQUARE[targetRow][targetCol])) {
        return false;
        }
        int rowDiff = Math.abs(targetRow - row);
        int colDiff = Math.abs(targetCol - col);

        // A king can move one square in any direction: horizontally, vertically, or diagonally.
        return (rowDiff <= 1 && colDiff <= 1);    }
}

//public boolean isInvalidMove(King king) {
//
//}

//private BoardPosition kingSquare(Turn color) {
//    Pieces piece = this.piece
//    if (piece.getPieceType)
//    for (int row = 0; row < Board.BOARD_SIZE; row++) {
//        for (int col = 0; col < Board.BOARD_SIZE; col++) {
//            Pieces piece = Board.SQUARE[row][col];
//            return new BoardPosition(row, col);
//        }
//    }
//}


