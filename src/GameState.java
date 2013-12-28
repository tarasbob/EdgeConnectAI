import java.util.*;

/**
 * Represents the state (position, current turn, etc)
 */
public abstract class GameState {
    
    /**
     * Create a deep clone of this game state.
     */
    public abstract GameState clone();

    /**
     * Update the state by carrying out the given move.
     */
    public abstract void doMove(GameMove move);

    /**
     * Get a list of legal moves.
     */
    public abstract List getLegalMoves();

    /**
     * Get a list of legal moves.
     */
    public abstract GameMove getRandomMove();

    /**
     * Get the result from the perspective of the given player.
     */
    public abstract double getResult(int playerNum);

    /**
     * Return a string representation of the game state.
     */
    public abstract String toString();

    /**
     * Return the player number that just moved
     */
    public abstract int getPlayerJustMoved();

}
