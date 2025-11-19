import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class Movement {
    protected static void handleSquareClick(int row, int col) {
        Pieces clickedPiece = Board.SQUARE[row][col];
        if (Board.selectedPiece == null) {
            if (clickedPiece != null && clickedPiece.getPieceColor()
                    == (Board.currentTurn == Turn.WHITE ? Pieces.WHITE : Pieces.BLACK)) {
                System.out.println("Selected is null and clicked is not null");
                clearHighlights();
                Board.selectedPiece = clickedPiece;
                highlightLegalMoves(Board.selectedPiece);
            } else {
                clickedPiece = null;
            }
        } else {
                clearHighlights();
                if (Board.selectedPiece instanceof King && Board.check(Board.selectedPiece.getPieceColor(), row, col)) {
                    Board.selectedPiece = null;
                    System.out.println("King would be in check.");
                } else if (Board.selectedPiece.isValidMove(row, col)
                        && (clickedPiece == null || !isSameTeam(Board.selectedPiece, clickedPiece))) {
                    // Move the selected piece to the clicked square
                    Pieces[][] saveSquare = Board.deepCopy(Board.SQUARE);
                    for (int r = 0; r < Board.BOARD_SIZE; r++) {
                        for (int c = 0; c < Board.BOARD_SIZE; c++) {
                            Pieces currPiece = Board.SQUARE[r][c];
                            if (Board.selectedPiece instanceof King && currPiece != null && currPiece instanceof King && currPiece.getPieceColor() == Board.selectedPiece.getPieceColor()) {
                                Board.selectedPiece = currPiece;
                            }
                        }
                    }
                    Board.SQUARE[row][col] = Board.selectedPiece;
                    Board.SQUARE[Board.selectedPiece.getRow()][Board.selectedPiece.getCol()] = null;
                    System.out.println(Board.selectedPiece instanceof King ? ("is king at " + Board.selectedPiece.getRow() + ", " + Board.selectedPiece.getCol()) : "not king");
                    Board.SQUARE[row][col].setRow(row);
                    Board.SQUARE[row][col].setCol(col);
                    Board.SQUARE[row][col].firstMove = false;
                    Pieces king = null;
                    boolean found = false;
                    for(int r = 0; r < Board.BOARD_SIZE; r++) {
                        for(int c = 0; c < Board.BOARD_SIZE; c++) {
                            Pieces currPiece = Board.SQUARE[r][c];
                            if(currPiece != null && currPiece instanceof King && currPiece.getPieceColor() == Board.selectedPiece.getPieceColor()) {
                                king = currPiece;
                                found = true;
                                break;
                            }
                        }
                        if (found) break;
                    }
                    if (Board.check(king.getPieceColor(), king.getRow(), king.getCol())) {
                        System.out.println("This move would leave your king in check.");
                        Board.SQUARE = saveSquare;
                        updateBoard();
                        return;
                    }
                    Board.selectedPiece = null;
                    switchTurn();
                    updateBoard();
                } else {
                Board.selectedPiece = null;
                // Pieces on the same team, do not allow capturing
                System.out.println("Cannot capture a piece on the same team.");
            }
        }
    }

    protected static void switchTurn() {
        Board.currentTurn = (Board.currentTurn == Turn.WHITE) ? Turn.BLACK : Turn.WHITE;
        updateTurnLabel();
    }

    private static void updateTurnLabel() {
        if (Board.currentTurn == Turn.WHITE) {
            Board.turnLabel.setText("White to move");
        } else {
            Board.turnLabel.setText("Black to move");
        }
    }

    protected static void highlightLegalMoves(Pieces piece) {
        for (int row = 0; row < Board.BOARD_SIZE; row++) {
            for (int col = 0; col < Board.BOARD_SIZE; col++) {
                if (piece.isValidMove(row, col)) {
                    JPanel square = Board.squares[row][col];
                    square.setBackground(new Color(55, 249, 100));
                }
            }
        }
    }

    protected static void clearHighlights() {
        for (int row = 0; row < Board.BOARD_SIZE; row++) {
            for (int col = 0; col < Board.BOARD_SIZE; col++) {
                JPanel square = Board.squares[row][col];
                square.setBackground((row + col) % 2 == 0 ? new Color(222, 177, 155) : new Color(135, 75, 45));
            }
        }
    }

    protected static boolean isSameTeam(Pieces piece1, Pieces piece2) {
        return piece1.getPieceColor() == piece2.getPieceColor();
    }

    protected static void updateBoard() {
        for (int row = 0; row < Board.BOARD_SIZE; row++) {
            for (int col = 0; col < Board.BOARD_SIZE; col++) {
                JPanel square = Board.squares[row][col];
                square.removeAll(); // Clear previous content
                square.setBackground((row + col) % 2 == 0 ? new Color(222, 177, 155) : new Color(135, 75, 45));
                if (Board.SQUARE[row][col] != null) {
                    try {
                        // Load and display the piece icon
                        String imagePath = Board.getPieceImagePath(Board.SQUARE[row][col]);
                        BufferedImage image = ImageIO.read(new FileInputStream(imagePath));
                        square.add(new JLabel(new ImageIcon(image)));
                    } catch (IOException e) {
                        // Handle image loading error
                        e.printStackTrace();
                    }
                }
                square.revalidate(); // Refresh square
                square.repaint(); // Repaint square
            }
        }
    }
}
