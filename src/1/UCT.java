/**
 * Represents a move in a game
 */
public class NimMove extends GameMove{

    private int numChipsRemoved;

    public NimMove(int num){
        numChipsRemoved = num;
    }

    public int getNumChips(){
        return numChipsRemoved;
    }
    
    /**
     * Return a string representation of the move
     */
    public String toString(){
        return "Number of chips removed: " + Integer.toString(numChipsRemoved);
    }

}
