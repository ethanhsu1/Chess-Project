public class Move {
    int oldCol;
    int oldRow;
    int newCol;
    int newRow;

    Pieces piece;
    Pieces capture;

    public Move(Board board, Pieces piece, int newCol, int newRow) {
        this.oldCol = piece.col;
        this.oldRow = piece.row;
        this.newCol = newCol;
        this.newRow = newRow;
        this.piece = piece;
        this.capture = board.getPiece(newCol, newRow);
    }
}
