import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class of the minesweeper program
 * Responsible for handling all GUI stuff
 *
 * @author JS
 */
public class GUI {

    static GraphicsConfiguration gc;
    Board board;
    List<List<JButton>> buttons = new ArrayList<>();

    public GUI(Board board) {
        this.board = board;
    }

    /**
     * Method used to refresh visual properties for all fields on the boards
     */
    public void changeButtons() {
        for (List<Field> line : board.getBoard()) {
            for (Field field : line) {
                if (field.isDiscovered()) {
                    int[] fieldCoordinates = board.getFieldCoordinates(field);
                    JButton button = buttons.get(fieldCoordinates[0]).get(fieldCoordinates[1]);
                    button.setText(String.valueOf(field.getValue()));
                    setFontColor(button, field);
                }
            }
        }
    }

    /**
     * Method used to setting correct color according to field value
     * @param button Button visually representing the field object
     * @param field Field object we are requesting the values from
     */
    public void setFontColor(JButton button, Field field) {
        switch (field.getValue()) {
            case 0:
                button.setText("");
            case 1:
                button.setForeground(Color.BLUE);
                break;
            case 2:
                button.setForeground(Color.GREEN);
                break;
            case 3:
                button.setForeground(Color.RED);
                break;
            case 4:
                button.setForeground(Color.MAGENTA);
                break;
            case 5:
                button.setForeground(Color.decode("#8B4513"));
                break;
            case 6:
                button.setForeground(Color.CYAN);
                break;
            case 7:
                button.setForeground(Color.BLACK);
                break;
            case 8:
                button.setForeground(Color.GRAY);
                break;
        }
        button.setBackground(Color.lightGray);
    }

    /**
     * Method used to add mouseClick listener to button
     * @param arg0 Mouse key pressed
     * @param field Field object bound with button clicked to get value from
     * @param button Button clicked
     * @param frame Frame containing the button
     */
    public void mousePressed(MouseEvent arg0, Field field, JButton button, JFrame frame) {
        int[] fieldCoordinates = board.getFieldCoordinates(field);
        boolean result;

        if (arg0.getButton() == MouseEvent.BUTTON1) {
            result = board.unveilField(fieldCoordinates[0], fieldCoordinates[1]);
            if (!result && !field.isMarkedBomb()) {
                button.setText(String.valueOf(field.getValue()));
            } else if (field.getValue() == 9 && !field.isMarkedBomb()) {
                int option = JOptionPane.showConfirmDialog(frame, "Przegrana! \n Czy chcesz zagrać ponownie?", "", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    frame.dispose();
                    newGame();
                } else {
                    frame.dispose();
                }
            }
        } else if (arg0.getButton() == MouseEvent.BUTTON3) {
            if (!field.isMarkedBomb()) {
                result = board.markBomb(fieldCoordinates[0], fieldCoordinates[1]);
                if (result) {
                    Image img;
                    try {
                        int width = button.getWidth();
                        int height = button.getHeight();
                        img = ImageIO.read(getClass().getResource("resources/flag.png"));
                        img = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
                        button.setIcon(new ImageIcon(img));
                        button.setBackground(Color.lightGray);
                    } catch (IOException e) {
                        button.setText("*");
                    }
                }
            } else {
                board.unmarkBomb(fieldCoordinates[0], fieldCoordinates[1]);
                button.setText("");
                button.setBackground(null);
                button.setIcon(null);
            }
        }
        changeButtons();
        if (board.winCondition()) {
            int option = JOptionPane.showConfirmDialog(frame, "Wygrana! \n Czy chcesz zagrać ponownie?", "", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                frame.dispose();
                newGame();
            } else {
                frame.dispose();
            }
        }
    }

    /**
     * Method used to create grid of buttons with fields / mouseListeners assigned
     * @param frame Frame we want to reference in mouseListener
     * @return Grid of buttons
     */
    public List<List<JButton>> createButtons (JFrame frame) {
        List<List<JButton>> buttonGrid = new ArrayList<>();
        for (List<Field> line : board.getBoard()) {
            List<JButton> temporary = new ArrayList<>();
            for (Field field : line) {
                JButton button = new JButton();
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        GUI.this.mousePressed(e, field, button, frame);
                    }
                });
                temporary.add(button);
            }
            buttonGrid.add(temporary);
        }
        return buttonGrid;
    }

    /**
     * Method used for creating the program window.
     * Responsible for handling all events and calling methods for changes / updates
     */
    public void showGui() {
        JFrame frame = new JFrame(gc);
        frame.setTitle("Minesweeper JS");
        JLabel status = new JLabel();
        status.setText(String.format("Bombs: %d", board.bombPositions.size()));
        buttons = createButtons(frame);
        for (List<JButton> line : buttons) {
            for (JButton button : line) {
                frame.add(button);
            }
        }

        frame.setSize(600, 400);
        JButton reset = new JButton();
        reset.setText("Reset");
        reset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                frame.dispose();
                newGame();
            }
        });
        frame.setLayout(new GridLayout(board.getBoard().size() + 2, board.getBoard().size() - 1));

        // Loop required to position buttons etc. in the last row, fills one row with empty JPanel
        for (int i = 0; i < board.getBoard().size(); i++) {
            frame.add(new JPanel());
        }
        frame.add(status);
        JButton close = new JButton();
        close.setText("Close");
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                frame.dispose();
            }
        });

        // Loop for filling columns between bombs number and reset/close
        for (int i = 0; i < board.getBoard().size() - 3; i++) {
            frame.add(new JPanel());
        }
        frame.add(reset);
        frame.add(close);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Method containing main game loop
     */
    public static void newGame() {
        int bombs = Integer.parseInt(JOptionPane.showInputDialog("Podaj ilość bomb"));
        int size = Integer.parseInt(JOptionPane.showInputDialog("Podaj rozmiar planszy"));
        Board gra = new Board(size, bombs);
        GUI nowaGra = new GUI(gra);
        nowaGra.showGui();
    }


}
