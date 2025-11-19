import java.awt.image.BufferedImage;

public class Knight extends Pieces {
    public Knight(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.squareSize;
        this.yPos = row * board.squareSize;

        this.isWhite = isWhite;
        this.name = "Knight";

        this.sprite = sheet.getSubimage
                (3 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance
                (board.squareSize, board.squareSize, BufferedImage.SCALE_SMOOTH);

    }

    @Override
    public boolean isValidMovement(int col, int row) {
        return Math.abs(col - this.col) * Math.abs(row - this.row) == 2;
    }
}
