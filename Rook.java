import java.awt.image.BufferedImage;

public class Rook extends Pieces {
    public Rook(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.squareSize;
        this.yPos = row * board.squareSize;

        this.isWhite = isWhite;
        this.name = "Rook";

        this.sprite = sheet.getSubimage
                (4 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance
                (board.squareSize, board.squareSize, BufferedImage.SCALE_SMOOTH);

    }

    @Override
    public boolean isValidMovement(int col, int row) {
        return this.col == col || this.row == row;
    }
    public boolean moveCollides(int col, int row) {
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
        return false;
    }
}
