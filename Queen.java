import java.awt.image.BufferedImage;

public class Queen extends Pieces {
    public Queen(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.squareSize;
        this.yPos = row * board.squareSize;

        this.isWhite = isWhite;
        this.name = "Queen";

        this.sprite = sheet.getSubimage
                (sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance
                (board.squareSize, board.squareSize, BufferedImage.SCALE_SMOOTH);

    }

    public boolean isValidMovement(int col, int row) {
        return this.col == col || this.row == row || Math.abs(this.col - col) == Math.abs(this.row - row);
    }

    public boolean moveCollides(int col, int row) {
        if (this.col == col || this.row == row) {
            // checking if rook collides with piece to the left
            if (this.col > col) {
                for (int c = this.col - 1; c > col; c--) {
                    if (board.getPiece(c, this.row) != null)
                        return true;
                }
            }
            // checking right
            if (this.col < col) {
                for (int c = this.col + 1; c < col; c++) {
                    if (board.getPiece(c, this.row) != null)
                        return true;
                }
            }
            // checking up
            if (this.row > row) {
                for (int r = this.row - 1; r > row; r--) {
                    if (board.getPiece(this.col, r) != null)
                        return true;
                }
            }
            // checking down
            if (this.row < row) {
                for (int r = this.row + 1; r < row; r++) {
                    if (board.getPiece(this.col, r) != null)
                        return true;
                }
            }
        } else {
            if (this.col > col && this.row > row) {
                for (int i = 1; i < Math.abs(this.col - col); i++) {
                    if (board.getPiece(this.col - i, this.row - i) != null) {
                        return true;
                    }
                }
            }
            // right diagonal right
            if (this.col < col && this.row > row) {
                for (int i = 1; i < Math.abs(this.col - col); i++) {
                    if (board.getPiece(this.col + i, this.row - i) != null) {
                        return true;
                    }
                }
            }
            // left diag down
            if (this.col > col && this.row < row) {
                for (int i = 1; i < Math.abs(this.col - col); i++) {
                    if (board.getPiece(this.col - i, this.row + i) != null) {
                        return true;
                    }
                }
            }
            // right diag down
            if (this.col < col && this.row < row) {
                for (int i = 1; i < Math.abs(this.col - col); i++) {
                    if (board.getPiece(this.col + i, this.row + i) != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
