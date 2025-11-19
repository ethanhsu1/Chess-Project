import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Pieces {
    public int col, row;
    public int xPos, yPos;

    public boolean isWhite;
    public String name;
    public int value;
    public boolean isFirstMove = true;
    Image sprite;
    Board board;

    public Pieces(Board board) {
        this.board = board;
    }
    BufferedImage sheet;
    {
        try {
             String path = "resources/pieces1.png";
            sheet = ImageIO.read(new FileInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected int sheetScale = sheet.getWidth() / 6;
    public void paint(Graphics2D g2d) {
        g2d.drawImage(sprite, xPos, yPos, null);
    }

    public boolean isValidMovement(int col, int row) {
        return true;
    }
    public boolean moveCollides(int col, int row) {
        return false;
    }
}
