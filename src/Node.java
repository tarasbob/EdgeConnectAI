import java.util.*;

/**
 * A node in the game tree. Wins are always from the perspective of playerJustMoved.
 */
public class Node{

    /* The move that got us to this node. Null is for the root node */
    private GameMove move;
    /* Null for the root node */
    private Node parent;
    private List<Node> childNodes;
    private double wins;
    private int visits;
    private List<GameMove> untriedMoves;
    private int playerJustMoved;
    Random generator;

    private double UCTK = 1.0;

    public Node(GameMove move, Node parent, GameState state){
        this.move = move;
        this.parent = parent;
        childNodes = new ArrayList<Node>();
        wins = 0.0;
        visits = 0;
        untriedMoves = (List<GameMove>)state.getLegalMoves();
        playerJustMoved = state.getPlayerJustMoved();
        generator = new Random();
    }

    /**
     * Use the UCB1 formula to select a child node.
     */
    public Node UCTSelectChild(){
        //we want to select child with the highest rating
        Node maxChild = childNodes.get(0);
        double maxRating = maxChild.getRating();
        for(Node n : childNodes){
            double newRating = n.getRating();
            if(newRating > maxRating){
                maxChild = n;
                maxRating = newRating;
            }
        }
        return maxChild;
    }

    public int getPlayerJustMoved(){
        return playerJustMoved;
    }

    /*
    public GameState expand(GameState state){
        if(!untriedMoves.isEmpty()){
            int randomIndex = generator.nextInt(untriedMoves.size());
            GameMove m = untriedMoves.get(randomIndex);
            state.doMove(m);
            addChild(m, state);
        }
        return state;
    }
    */

    public Node getMostVisitedChild(){
        Node maxChild = childNodes.get(0);
        int maxVisits = 0;
        for(Node n : childNodes){
            if(n.getVisits() > maxVisits){
                maxVisits = n.getVisits();
                maxChild = n;
            }
        }
        return maxChild;
    }

    public List<GameMove> getUntriedMoves(){
        return untriedMoves;
    }
    
    public List<Node> getChildNodes(){
        return childNodes;
    }

    public GameMove getMove(){
        return move;
    }

    public double getRating(){
        double rating = wins/visits + UCTK * Math.sqrt(2*Math.log(parent.getVisits())/visits);
        return rating;
    }

    public int getVisits(){
        return visits;
    }

    public Node getParent(){
        return parent;
    }

    /**
     * Remove move from untriedMoves and add a new child node for this move.
     * Return the added child node
     */
    public Node addChild(GameMove move, GameState state){
        assert untriedMoves.remove(move) == true;
        Node child = new Node(move, this, state);
        childNodes.add(child);
        return child;
    }

    /**
     * Update this node after a visit. Result is from viewpoint of playerJustMoved.
     */
    public void update(double result){
        visits += 1;
        wins += result;
    }

    /**
     * Get the string representation of this node.
     */
    public String toString(){
        return "visits: " + Integer.toString(visits);
    }


}
