import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUI {

    static GraphicsConfiguration gc;
    Board board;
    List<List<JButton>> buttons = new ArrayList<>();

    public GUI(Board board) {
        this.board = board;
    }

    public void changeButtons() {
        for(List<Field> line: board.getBoard()) {
            for (Field field: line) {
                if(field.isDiscovered()) {
                    int[] fieldCoordinates = board.getFieldCoordinates(field);
                    buttons.get(fieldCoordinates[0]).get(fieldCoordinates[1]).setText(String.valueOf(field.getValue()));
                    setFontColor(buttons.get(fieldCoordinates[0]).get(fieldCoordinates[1]), field);
                }
            }
        }
    }

    public void setFontColor(JButton button, Field field) {
        switch (field.getValue()) {
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
        button.setBackground(Color.WHITE);
    }

    public void showGui() {
        JFrame frame = new JFrame(gc);
        frame.setTitle("Minesweeper JS");
        JLabel status = new JLabel();
        status.setText(String.format("Bombs: %d", board.bombPositions.size()));
        for(List<Field> line: board.getBoard()) {
            List<JButton> temporary = new ArrayList<>();
            for (Field field: line) {
                JButton button = new JButton();
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent arg0) {
                        int[] fieldCoordinates = board.getFieldCoordinates(field);
                        System.out.println(Arrays.toString(fieldCoordinates));
                        System.out.println(button.getMouseListeners()[0]);
                        boolean result;
                        if (arg0.getButton() == MouseEvent.BUTTON1){
                            result = board.unveilField(fieldCoordinates[0], fieldCoordinates[1]);
                            if (!result && !field.isMarkedBomb()) {
                                button.setText(String.valueOf(field.getValue()));
                            } else if (field.getValue() == 9) {
                                int option = JOptionPane.showConfirmDialog(frame, "Przegrana! \n Czy chcesz zagrać ponownie?", "", JOptionPane.YES_NO_OPTION);
                                if(option == JOptionPane.YES_OPTION) {
                                    frame.dispose();
                                    newGame();
                                } else {
                                    frame.dispose();
                                }
                            }
                        } else if (arg0.getButton() == MouseEvent.BUTTON3) {
                            if(!field.isMarkedBomb()) {
                                result = board.markBomb(fieldCoordinates[0], fieldCoordinates[1]);
                                if (result) {
                                    Image img;
                                    try {
                                        int width = button.getWidth();
                                        int height = button.getHeight();
                                        img = ImageIO.read(getClass().getResource("resources/flag.png"));
                                        img = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
                                        button.setIcon(new ImageIcon(img));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        button.setText("*");
                                    }
                                }
                            } else {
                                board.unmarkBomb(fieldCoordinates[0], fieldCoordinates[1]);
                                button.setText("");
                                button.setIcon(null);
                            }
                        }
                        System.out.println(board);
                        changeButtons();
                        boolean won = board.winCondition();
                        if(won) {
                            int option = JOptionPane.showConfirmDialog(frame, "Wygrana! \n Czy chcesz zagrać ponownie?", "", JOptionPane.YES_NO_OPTION);
                            if(option == JOptionPane.YES_OPTION) {
                                frame.dispose();
                                newGame();
                            } else {
                                frame.dispose();
                            }
                        }
                    }
                });
            temporary.add(button);
            }
            buttons.add(temporary);
            System.out.println();
        }
        for(List<JButton> line: buttons) {
            for (JButton button : line) {
                frame.add(button);
            }
        }
        frame.add(status);
        frame.setSize(600,400);
        frame.setLayout(new GridLayout(board.getBoard().size()+1, board.getBoard().size()-1));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void newGame() {
        int bombs = Integer.parseInt(JOptionPane.showInputDialog("Podaj ilość bomb"));
        int size = Integer.parseInt(JOptionPane.showInputDialog("Podaj rozmiar planszy"));
        Board gra = new Board(size, bombs);
        GUI nowaGra = new GUI(gra);
        nowaGra.showGui();
    }


}
