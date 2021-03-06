/**
 * Class used for creating field object,
 *
 * @author JS
 */
public class Field {
    private int value;
    private boolean discovered;
    private boolean markedBomb = false;

    public Field(int value, boolean discovered) {
        this.value = value;
        this.discovered = discovered;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }

    public void setMarkedBomb(boolean state) {
        this.markedBomb = state;
    }

    public boolean isMarkedBomb() {
        return this.markedBomb;
    }


    public void placeMine() {
        setValue(9);
    }

    @Override
    public String toString() {
        return "{" + value +
                ", " + discovered +
                '}';
    }
}
