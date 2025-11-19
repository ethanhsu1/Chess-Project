import javax.swing.*;


public class CheckScanner {

    Board board;
    public CheckScanner(Board board) {
        this.board = board;
    }

    public boolean isKingChecked(Move move) {

        Pieces king = board.findKing(move.piece.isWhite);
        assert king != null;

        int kingCol = king.col;
        int kingRow = king.row;

        if (board.selectedPiece != null && board.selectedPiece.name.equals("King")) {
            kingCol = move.newCol;
            kingRow = move.newRow;
        }
        // checks cases which king is hit
        return hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 0, 1) ||
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 1, 0) ||
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 0, -1) ||
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, -1, 0) ||

                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, -1) ||
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, -1) ||
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, 1) ||
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, 1) ||

                hitByKnight(move.newCol, move.newRow, king, kingCol, kingRow) ||
                hitByPawn(move.newCol, move.newRow, king, kingCol, kingRow) ||
                hitByKing(king, kingCol, kingRow);
    }

    private boolean hitByRook(int col, int row, Pieces king, int kingCol, int kingRow, int colValue, int rowValue) {
        for (int i = 1; i < 8; i++) {
            if (kingCol + (i * colValue) == col && kingRow + (i * rowValue) == row) {
                break;
            }
            Pieces piece = board.getPiece(kingCol + (i * colValue), kingRow + (i * rowValue));
            if (piece != null && piece != board.selectedPiece) {
                if (!board.sameTeam(piece, king) && (piece.name.equals("Rook") || piece.name.equals("Queen"))) {
                    return true;
                }
                break;
            }
        }
        return false;
    }
    private boolean hitByBishop(int col, int row, Pieces king, int kingCol, int kingRow, int colValue, int rowValue) {
        for (int i = 1; i < 8; i++) {
            if (kingCol - (i * colValue) == col && kingRow - (i * rowValue) == row) {
                break;
            }
            Pieces piece = board.getPiece(kingCol - (i * colValue), kingRow - (i * rowValue));
            if (piece != null && piece != board.selectedPiece) {
                if (!board.sameTeam(piece, king) && (piece.name.equals("Bishop") || piece.name.equals("Queen"))) {
                    return true;
                }
                break;
            }
        }
        return false;
    }
    private boolean hitByKnight(int col, int row, Pieces king, int kingCol, int kingRow) {
        return checkKnight(board.getPiece(kingCol - 1, kingRow - 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 1, kingRow - 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 2, kingRow - 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 2, kingRow + 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 1, kingRow + 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 1, kingRow + 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 2, kingRow + 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 2, kingRow - 1), king, col, row);

    }
    private boolean checkKnight(Pieces p, Pieces k, int col, int row) {
        return p != null && !board.sameTeam(p, k) && p.name.equals("Knight") && !(p.col == col && p.row == row);
    }
    private boolean hitByKing(Pieces king, int kingCol, int kingRow) {
        return checkKing(board.getPiece(kingCol - 1, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol, kingRow + 1), king);
    }
    private boolean checkKing(Pieces p, Pieces k) {
        return p != null && !board.sameTeam(p, k) && p.name.equals("King");
    }
    private boolean hitByPawn(int col, int row, Pieces king, int kingCol, int kingRow) {
        int colorVal = king.isWhite ? - 1 : 1;
        return checkPawn(board.getPiece(kingCol + 1, kingRow + colorVal), king, col, row) ||
                checkPawn(board.getPiece(kingCol - 1, kingRow + colorVal), king, col, row);
    }
    private boolean checkPawn(Pieces p, Pieces k, int col, int row) {
        return p != null && !board.sameTeam(p, k) && p.name.equals("Pawn") && !(p.col == col && p.row == row);
    }
    public boolean isKingChecked(boolean isWhite) {
        Pieces king = board.findKing(isWhite);

        // Iterate through all opponent pieces and check if any can capture the king
        for (Pieces piece : board.pieceList) {
            if (piece.isWhite != isWhite && board.isValidMove(new Move(board, piece, king.col, king.row))) {
                return true;
            }
        }
        return false;
    }
}
