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
    public List<GameMove> getLegalMoves(){
        List<GameMove> moves = new ArrayList<GameMove>();
        for(int i = 0; i < Math.min(3, numChips); i++){
            moves.add(new NimMove(i));
        }
        return moves;
    }

    /**
     * Get a list of legal moves.
     */
    public GameMove getRandomMove(){
        List<GameMove> moves = getLegalMoves();
        return moves.get(generator.nextInt(moves.size()));
    }

    public void rollOut(){
        while(!getLegalMoves().isEmpty()){
            doMove(getRandomMove());
        }
    }

    public int getPlayerJustMoved(){
        return playerJustMoved;
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
