import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Chess Game");
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(1000, 850));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Board board = new Board();
        frame.add(board, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JLabel modeLabel = new JLabel("Select Game Mode: ");
        JComboBox<String> modeComboBox = new JComboBox<>(new String[]{"Player vs Player", "Player vs Computer", "Computer vs Computer"});
        JButton startButton = new JButton("Start Game");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMode = (String) modeComboBox.getSelectedItem();
                switch (selectedMode) {
                    case "Player vs Player":
                        board.playerVsPlayerMode();
                        break;
                    case "Player vs Computer":
                        board.playerVsComputerMode();
                        break;
                    case "Computer vs Computer":
                        board.computerVsComputerMode();
                        break;
                }
            }
        });

        controlPanel.add(modeLabel);
        controlPanel.add(modeComboBox);
        controlPanel.add(startButton);

        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}