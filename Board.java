import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    List<List<Field>> board;
    List<Integer[]> bombPositions = new ArrayList<>();

    public Board(int size, int bombs) {
        this.board = new ArrayList<>();
        setDefaultField(size);
        generateMines(bombs);
    }

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

    public boolean markBomb(int x, int y) {

        try {
            if(!board.get(x).get(y).isDiscovered() && !board.get(x).get(y).isMarkedBomb()) {
                board.get(x).get(y).setMarkedBomb(true);
                return true;
            } else if (board.get(x).get(y).isDiscovered()) {
                System.out.println("That field is already discovered");
                return false;
            } else if (board.get(x).get(y).isMarkedBomb()) {
                System.out.println("That field is already marked");
                return false;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Field out of range");
            return false;
        }
        return false;
    }

    public boolean unmarkBomb(int x, int y) {
        try {
            if(board.get(x).get(y).isMarkedBomb()) {
                board.get(x).get(y).setMarkedBomb(false);
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Field out of range");
            return false;
        }
        return false;
    }

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
            System.out.println("Bomba");
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

    // drawBoard is for test purposes only
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
        System.out.println("Discovered: " + discovered);
        System.out.println("Discovered bombs: " + bombs);
        System.out.println("Total fields: " + (int) Math.pow(board.size(), 2));
        System.out.println("Total bombs: " + bombPositions.size());
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
