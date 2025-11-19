import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Board extends JPanel {
    int rows = 8;
    int cols = 8;

    ArrayList<Pieces> pieceList = new ArrayList<>();
    public int squareSize = 85;

    Color lightBrown = new Color(135, 75, 45);
    Color brown = new Color(222, 177, 155);
    public Pieces selectedPiece;
    Input input = new Input(this);

    public int enPassantTile = -1;
    public CheckScanner checkScanner = new CheckScanner(this);
    protected boolean whiteTurn = true;
    private static final int PAWN_VALUE = 1;
    private static final int KNIGHT_VALUE = 3;
    private static final int BISHOP_VALUE = 3;
    private static final int ROOK_VALUE = 5;
    private static final int QUEEN_VALUE = 9;
    GameMode mode = GameMode.PLAYER_VS_PLAYER; // Default mode
    // String fen = "8/8/8/6p1/7p/4k2P/5R1K/r7 b - - 0 1";
    String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public enum GameMode {
        PLAYER_VS_PLAYER,
        PLAYER_VS_COMPUTER,
        COMPUTER_VS_COMPUTER
    }


    
    public Board() {
        this.setPreferredSize(new Dimension(cols * squareSize, rows * squareSize));
        this.setBackground(Color.WHITE);
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        setBoardFromFEN(fen);
    }

    public void setBoardFromFEN(String fen) {
        String[] parts = fen.split(" ");

        // Clear the existing piece list
        pieceList.clear();

        // Parse piece positions
        String[] rows = parts[0].split("/");
        for (int i = 0; i < 8; i++) {
            String row = rows[i];
            int col = 0;
            for (char c : row.toCharArray()) {
                if (Character.isDigit(c)) {
                    col += Character.getNumericValue(c);
                } else {
                    boolean isWhite = Character.isUpperCase(c);
                    char piece = Character.toLowerCase(c);
                    switch (piece) {
                        case 'p':
                            pieceList.add(new Pawn(this, col, i, isWhite));
                            break;
                        case 'r':
                            pieceList.add(new Rook(this, col, i, isWhite));
                            break;
                        case 'n':
                            pieceList.add(new Knight(this, col, i, isWhite));
                            break;
                        case 'b':
                            pieceList.add(new Bishop(this, col, i, isWhite));
                            break;
                        case 'q':
                            pieceList.add(new Queen(this, col, i, isWhite));
                            break;
                        case 'k':
                            pieceList.add(new King(this, col, i, isWhite));
                            break;
                    }
                    col++;
                }
            }
        }
        whiteTurn = parts[1].equals("w");
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if ((c + r) % 2 == 0) {
                    g2d.setColor(brown);
                } else {
                    g2d.setColor(lightBrown);
                }
                g2d.fillRect(c * squareSize, r * squareSize, squareSize, squareSize);
            }
        }

        if (selectedPiece != null) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (isValidMove(new Move(this, selectedPiece, c, r))) {
                        g2d.setColor(new Color(91, 159, 92));
                        g2d.fillRect(c * squareSize, r * squareSize, squareSize, squareSize);
                    }
                }
            }
        }

        // Draw pieces on the board
        for (Pieces piece : pieceList) {
            piece.paint(g2d);
        }
    }
    public void displayCheckmateMessage(boolean isWhite) {
        String message = isWhite ? "White is checkmated!" : "Black is checkmated!";
        JOptionPane.showMessageDialog(this, message, "Checkmate", JOptionPane.INFORMATION_MESSAGE);
    }
    public void displayCheckMessage(boolean isWhite) {
        String message = isWhite ? "White is in check." : "Black is in check.";
        JOptionPane.showMessageDialog(this, message, "Check", JOptionPane.INFORMATION_MESSAGE);
    }


    public Pieces getPiece(int col, int row) {
        for (Pieces piece : pieceList) {
            if (piece.col == col && piece.row == row) {
                return piece;
            }
        }
        return null;
    }
    public boolean isValidMove(Move move) {
        if (move.piece.isWhite != whiteTurn) {
            return false;
        }
        if (sameTeam(move.piece, move.capture)) {
            return false;
        }
        if (!move.piece.isValidMovement(move.newCol, move.newRow)) {
            return false;
        }
        if (move.piece.moveCollides(move.newCol, move.newRow)) {
            return false;
        }
        if (checkScanner.isKingChecked(move)) {
            return false;
        }
        return true;
    }
    public boolean sameTeam(Pieces p1, Pieces p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.isWhite == p2.isWhite;
    }


    Pieces findKing(boolean isWhite) {
        for (Pieces piece : pieceList) {
            if (isWhite == piece.isWhite && piece.name.equals("King")) {
                return piece;
            }
        }
        return null;
    }

    public int getTileNum(int col, int row) {
        return row * rows + col;
    }

    public void makeMove(Move move) {
        if (move.piece.name.equals("Pawn")) {
            movePawn(move);
        } else if (move.piece.name.equals("King")) {
            moveKing((move));
        }
            move.piece.col = move.newCol;
            move.piece.row = move.newRow;
            move.piece.xPos = move.newCol * squareSize;
            move.piece.yPos = move.newRow * squareSize;

            move.piece.isFirstMove = false;

            capture(move.capture);
    }

    private void movePawn(Move move) {

        //en passant
        int colorIndex = move.piece.isWhite ? 1 : -1;

        if (getTileNum(move.newCol, move.newRow) == enPassantTile) {
            move.capture = getPiece(move.newCol, move.newRow + colorIndex);
        }
        if (Math.abs(move.piece.row - move.newRow) == 2) {
            enPassantTile = getTileNum(move.newCol, move.newRow + colorIndex);
        } else {
            enPassantTile = -1;
        }

        //promotions
        colorIndex = move.piece.isWhite ? 0 : 7;
        if (move.newRow == colorIndex) {
            promotePawn(move);
        }
    }

    private void moveKing(Move move) {
        if (Math.abs(move.piece.col - move.newCol) == 2) {
            Pieces rook;
            if (move.piece.col < move.newCol) {
                rook = getPiece(7, move.piece.row);
                rook.col = 5;
            } else {
                rook = getPiece(0, move.piece.row);
                rook.col = 3;
            }
            rook.xPos = rook.col * squareSize;
        }
    }

    private void promotePawn(Move move) {
        pieceList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
        capture(move.piece);
    }

    public void capture(Pieces piece) {
        pieceList.remove(piece);
    }
    public void nextTurn() {
        whiteTurn = !whiteTurn; // Switch turns
    }
    public boolean isCheckmate(boolean isWhite) {
        if (checkScanner.isKingChecked(isWhite)) {
            // Iterate through all pieces of the current player
            for (Pieces piece : pieceList) {
                if (piece.isWhite == isWhite) {
                    // Check all possible moves for each piece
                    for (int r = 0; r < rows; r++) {
                        for (int c = 0; c < cols; c++) {
                            Move move = new Move(this, piece, c, r);
                            if (isValidMove(move)) {
                                // Simulate the move
                                makeMove(move);
                                // If the king is no longer in check, return false (not checkmate)
                                if (!checkScanner.isKingChecked(isWhite)) {
                                    // Undo the move
                                    piece.col = move.piece.col;
                                    piece.row = move.piece.row;
                                    piece.xPos = move.piece.xPos;
                                    piece.yPos = move.piece.yPos;
                                    if (move.capture != null) {
                                        pieceList.add(move.capture);
                                    }
                                    return false;
                                }
                                // Undo the move
                                piece.col = move.piece.col;
                                piece.row = move.piece.row;
                                piece.xPos = move.piece.xPos;
                                piece.yPos = move.piece.yPos;
                                if (move.capture != null) {
                                    pieceList.add(move.capture);
                                }
                            }
                        }
                    }
                }
            }
            // If no move can get the king out of check, return true (checkmate)
            return true;
        }
        // If the king is not in check, return false
        return false;
    }
    public Move chooseComputerMove() {
        ArrayList<Move> prioritizedMoves = prioritizeMoves(); // Prioritize moves
        int depth = 3; // Set the depth for the search algorithm
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int maxEval = Integer.MIN_VALUE;
        Move bestMove = null;

        for (Move move : prioritizedMoves) {
            makeMove(move); // Make the move on a temporary board
            int eval = search(depth - 1, alpha, beta, false); // Perform alpha-beta search
            undoMove(move); // Undo the move

            if (eval > maxEval) {
                maxEval = eval;
                bestMove = move;
            }
        }

        return bestMove;
    }
    private ArrayList<Move> generateMoves() {
        ArrayList<Move> moves = new ArrayList<>();

        for (Pieces piece : pieceList) {
            if (piece.isWhite == whiteTurn) {
                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < cols; c++) {
                        Move move = new Move(this, piece, c, r);
                        if (isValidMove(move)) {
                            moves.add(move);
                        }
                    }
                }
            }
        }
        return moves;
    }
    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public void playerVsPlayerMode() {
        setMode(GameMode.PLAYER_VS_PLAYER);
    }

    public void playerVsComputerMode() {
        setMode(GameMode.PLAYER_VS_COMPUTER);
    }

    public void computerVsComputerMode() {
        setMode(GameMode.COMPUTER_VS_COMPUTER);
    }
    public int evaluatePosition() {
        int whiteMaterial = 0;
        int blackMaterial = 0;

        // Calculate material value for each side
        for (Pieces piece : pieceList) {
            if (piece.isWhite) {
                whiteMaterial += getValue(piece);
            } else {
                blackMaterial += getValue(piece);
            }
        }

        // Calculate material advantage
        int materialAdvantage = whiteMaterial - blackMaterial;

        // Return evaluation based on material advantage
        if (materialAdvantage > 0) {
            return materialAdvantage; // White is doing better
        } else if (materialAdvantage < 0) {
            return materialAdvantage; // Black is doing better
        } else {
            return 0; // Even position
        }
    }

    // Helper method to get the value of a piece
    private int getValue(Pieces piece) {
        switch (piece.name) {
            case "Pawn":
                return PAWN_VALUE;
            case "Knight":
                return KNIGHT_VALUE;
            case "Bishop":
                return BISHOP_VALUE;
            case "Rook":
                return ROOK_VALUE;
            case "Queen":
                return QUEEN_VALUE;
            default:
                return 0; // Unknown piece type
        }
    }
    public int search(int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || isGameOver()) {
            System.out.println(evaluatePosition());
            return evaluatePosition();
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : generateMoves()) {
                makeMove(move);
                int eval = search(depth - 1, alpha, beta, false);
                undoMove(move);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break; // Beta cutoff
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : generateMoves()) {
                makeMove(move);
                int eval = search(depth - 1, alpha, beta, true);
                undoMove(move);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break; // Alpha cutoff
                }
            }
            return minEval;
        }
    }
    public boolean isGameOver() {
        return isCheckmate(true) || isCheckmate(false);
    }
    public void undoMove(Move move) {
        // Restore the piece's previous position
        move.piece.col = move.oldCol;
        move.piece.row = move.oldRow;
        move.piece.xPos = move.oldCol * squareSize;
        move.piece.yPos = move.oldRow * squareSize;

        // If a captured piece was involved, restore it to the board
        if (move.capture != null) {
            pieceList.add(move.capture);
        }
    }
    public ArrayList<Move> prioritizeMoves() {
        ArrayList<Move> moves = generateMoves();
        ArrayList<PriorityMove> prioritizedMoves = new ArrayList<>();

        // Create a map to store the value of each opponent's piece type
        Map<String, Integer> opponentPieceValues = new HashMap<>();
        opponentPieceValues.put("Pawn", 1);
        opponentPieceValues.put("Knight", 3);
        opponentPieceValues.put("Bishop", 3);
        opponentPieceValues.put("Rook", 5);
        opponentPieceValues.put("Queen", 9);

        // Iterate through all available moves
        for (Move move : moves) {
            int movePriority = 0;

            // Check if the move involves capturing opponent's piece
            if (move.capture != null) {
                movePriority += opponentPieceValues.getOrDefault(move.capture.name, 0);
            }

            // Check if the move involves promoting a pawn
            if (move.piece.name.equals("Pawn") && (move.newRow == 0 || move.newRow == 7)) {
                movePriority += 10; // Promoting a pawn is given high priority
            }

            // Check if the move leads to our piece being attacked
            if (isSquareAttacked(move.newCol, move.newRow, !move.piece.isWhite)) {
                movePriority -= 5; // Penalize moves that lead to our piece being attacked
            }

            // Add the move along with its priority to the prioritizedMoves list
            prioritizedMoves.add(new PriorityMove(move, movePriority));
        }

        // Sort moves based on their priority (higher priority moves come first)
        prioritizedMoves.sort(Comparator.comparingInt(PriorityMove::getPriority).reversed());

        // Extract the original moves from the PriorityMove objects
        ArrayList<Move> finalMoves = new ArrayList<>();
        for (PriorityMove priorityMove : prioritizedMoves) {
            finalMoves.add(priorityMove.getMove());
        }

        return finalMoves;
    }

    // Check if the square at the specified position is attacked by the opponent
    private boolean isSquareAttacked(int col, int row, boolean byWhite) {
        for (Pieces piece : pieceList) {
            if (piece.isWhite != byWhite && piece.isValidMovement(col, row) && piece.moveCollides(col, row)) {
                return true;
            }
        }
        return false;
    }
}
// Helper class to represent a move along with its priority
class PriorityMove {
    private Move move;
    private int priority;

    public PriorityMove(Move move, int priority) {
        this.move = move;
        this.priority = priority;
    }

    public Move getMove() {
        return move;
    }

    public int getPriority() {
        return priority;
    }

}