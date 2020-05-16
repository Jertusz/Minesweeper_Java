import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class is used to create a board object,
 * works both with console and with GUI version
 *
 * @author JS
 */
public class Board {
    List<List<Field>> board;
    List<Integer[]> bombPositions = new ArrayList<>();

    public Board(int size, int bombs) {
        this.board = new ArrayList<>();
        setDefaultField(size);
        generateMines(bombs);
    }

    /**
     * Method used to created board fields with default values
     * @param size Size of the board
     */
    private void setDefaultField(int size) {
        for(int i = 0; i < size; i++) {
            List<Field> line = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                line.add(new Field(0, false));
            }
            board.add(line);
        }
    }

    public List<List<Field>> getBoard() {
        return board;
    }

    /**
     * Method used to get field coordinates on the board
     * @param searched Field object that we want to find on the board
     * @return int[] List [position on x axis, positions of y axis]
     */
    public int[] getFieldCoordinates (Field searched) {
        for(List<Field> line: board){
            for(Field field: line) {
                if (field.equals(searched)) {
                    return new int[] {board.indexOf(line), board.get(board.indexOf(line)).indexOf(field)};
                }
            }
        }
        return null;
    }

    /**
     * Method used to mark bombs to forbid user from clicking on them accidentaly
     * @param x Position of field on x axis
     * @param y Position of field on y axis
     * @return Boolean true only if marking worked
     */
    public boolean markBomb(int x, int y) {

        try {
            if(!board.get(x).get(y).isDiscovered() && !board.get(x).get(y).isMarkedBomb()) {
                board.get(x).get(y).setMarkedBomb(true);
                return true;
            } else if (board.get(x).get(y).isDiscovered()) {
//                Console version only
//                System.out.println("That field is already discovered");
                return false;
            } else if (board.get(x).get(y).isMarkedBomb()) {
//                Console version only
//                System.out.println("That field is already marked");
                return false;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Field out of range");
            return false;
        }
        return false;
    }

    /**
     * Method used to unmark bombs previously marked, to allow for discovery
     * ArrayIndexOutOfBounds possible only when playing console version
     * @param x Position of field on x axis
     * @param y Position of field on y axis
     */
    public void unmarkBomb(int x, int y) {
        try {
            if(board.get(x).get(y).isMarkedBomb()) {
                board.get(x).get(y).setMarkedBomb(false);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
//            Console version only
//            System.out.println("Field out of range");
        }
    }

    /**
     * Method used to generate bombs on the board in random positions
     * @param quantity Bomb quantity
     */
    private void generateMines(int quantity) {
        Random generator = new Random();
        int x, y;
        while (quantity > 0) {
            x = generator.nextInt(board.size())%10;
            y = generator.nextInt(board.size())%10;

            if(board.get(x).get(y).getValue() != 9) {
                board.get(x).get(y).placeMine();
                Integer[] pos = {x, y};
                bombPositions.add(pos);
                for(int i = -1; i < 2; i++) {
                    for(int j = -1; j < 2; j++) {
                        if (!(x+i < 0 || x+i > board.size() - 1 || y+j < 0 || y+j > board.size() - 1)) {
                            if(board.get(x + i).get(y + j).getValue() != 9) {
                                int newFieldValue = board.get(x + i).get(y + j).getValue() + 1;
                                board.get(x + i).get(y + j).setValue(newFieldValue);
                            }
                        }
                    }
                }
                quantity--;
            }
        }
    }

    /**
     * Method used to recursively check for undiscovered fields, until fields near bomb (1 - 8 values) are found
     * @param x Position of the field on x axis
     * @param y Position of the field on y axis
     * @return Boolean true only if the field contains bomb
     */
    public boolean unveilField(int x, int y) {
        Field field;
        try {
            field = board.get(x).get(y);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

        if (field.isDiscovered() || field.isMarkedBomb()) {
            return false;
        }

        if (field.getValue() == 9) {
//            Only for console version
//            System.out.println("Bomba");
            return true;
        } else {
            field.setDiscovered(true);
        }

        if (field.getValue() != 0) {
            return false;
        }

        unveilField(x - 1, y - 1);
        unveilField(x - 1, y);
        unveilField(x - 1, y + 1);
        unveilField(x + 1, y - 1);
        unveilField(x + 1, y);
        unveilField(x + 1, y + 1);
        unveilField(x, y - 1);
        unveilField(x, y);
        unveilField(x, y + 1);
        return false;
    }

    /**
     * Debug purpose only, allows to dynamically see what happens on the board after user clicks.
     * Can be deleted without breaking the program.
     */
    public void drawBoard() {
        StringBuilder boardImage = new StringBuilder();
        for(List<Field> line: board) {
            for (Field field: line) {
                if (field.getValue() == 0 && field.isDiscovered()) {
                    boardImage.append(" ");
                } else if (field.getValue() != 0 && field.isDiscovered()) {
                    boardImage.append(field.getValue());
                } else if (field.isMarkedBomb()){
                    boardImage.append("*");
                } else {
                    boardImage.append("#");
                }
            }
            boardImage.append("\n");
        }
        System.out.println(boardImage);
    }

    /**
     * This method check if user discovered all fields and marked all bombs
     * @return Boolean true if all fields discovered, false if not
     */
    public boolean winCondition() {
        int discovered = 0;
        int bombs = 0;
        for(List<Field> line: board) {
            for (Field field: line) {
                if (field.isDiscovered()) {
                    discovered++;
                } else if (field.isMarkedBomb()) {
                    bombs++;
                }
            }
        }

//        Enable this to see additional output in console
//        System.out.println("Discovered: " + discovered);
//        System.out.println("Discovered bombs: " + bombs);
//        System.out.println("Total fields: " + (int) Math.pow(board.size(), 2));
//        System.out.println("Total bombs: " + bombPositions.size());

        if (discovered + bombs == (int) Math.pow(board.size(), 2) && bombs == bombPositions.size()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(List<Field> line: board) {
            for (Field field: line) {
                result.append(field.toString());
            }
            result.append("\n");
        }
        return "Board{\n" + result + '}';
    }
}
