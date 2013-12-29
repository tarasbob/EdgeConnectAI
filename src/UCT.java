import java.util.*;

public class UCT {

    /*
     * Conduct a UCT search for maxIter iterations starting from root state.
     * return the best move from root state.
     */
    public static GameMove findMove(GameState rootState, int maxIter){

        Node rootNode = new Node(null, null, rootState);
        Node curNode;
        GameState curState;
        Random generator = new Random();

        for(int i = 0; i < maxIter; i++){
            curNode = rootNode;
            curState = rootState.clone();

            //Select
            while(curNode.getUntriedMoves().isEmpty() && !curNode.getChildNodes().isEmpty()){
                curNode = curNode.UCTSelectChild();
                curState.doMove(curNode.getMove());
            }

            //Expand
            if(!curNode.getUntriedMoves().isEmpty()){
                int randomIndex = generator.nextInt(curNode.getUntriedMoves().size());
                GameMove m = curNode.getUntriedMoves().get(randomIndex);
                curState.doMove(m);
                curNode = curNode.addChild(m, curState);
            }

            //Rollout
            curState.rollOut();

            //Backpropagate
            while(curNode != null){
                curNode.update(curState.getResult(curNode.getPlayerJustMoved()));
                curNode = curNode.getParent();
            }
        }

        return rootNode.getMostVisitedChild().getMove();

    }


}
