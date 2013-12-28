import java.util.*;

/**
 * Represents the state (position, current turn, etc)
 */
public class NimState extends GameState {

    private int numChips;
    private int playerJustMoved = 2;
    Random generator;

    public NimState(int numChips){
        generator = new Random();
        this.numChips = numChips;
    }
    
    /**
     * Create a deep clone of this game state.
     */
    public NimState clone(){
        return new NimState(numChips);
    }

    /**
     * Update the state by carrying out the given move.
     */
    public void doMove(GameMove move){
        numChips -= ((NimMove)move).getNumChips();
        playerJustMoved = 3 - playerJustMoved;
        assert numChips >= 0;
    }

    /**
     * Get a list of legal moves.
     */
    public List<NimMove> getLegalMoves(){
        List<NimMove> moves = new ArrayList<NimMove>();
        for(int i = 0; i < Math.min(3, numChips); i++){
            moves.add(new NimMove(i));
        }
        return moves;
    }

    /**
     * Get a list of legal moves.
     */
    public NimMove getRandomMove(){
        List<NimMove> moves = getLegalMoves();
        return moves.get(generator.nextInt(moves.size()));
    }

    public void rollOut(){
        while(!getLegalMoves().isEmpty()){
            doMove(getRandomMove());
        }
    }

    /**
     * Get the result from the perspective of the given player.
     */
    public double getResult(int playerNum){
        assert numChips == 0;
        if(playerJustMoved == playerNum){
            return 1.0;
        } else {
            return 0.0;
        }
    }

    /**
     * Return a string representation of the game state.
     */
    public String toString(){
        return "Chips remaining: " + Integer.toString(numChips);
    }

}
