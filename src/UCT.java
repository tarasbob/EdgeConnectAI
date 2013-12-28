public class UCT {

    /*
     * Conduct a UCT search for maxIter iterations starting from root state.
     * return the best move from root state.
     */
    public static findMove(GameState rootState, int maxIter){

        Node rootNode = (null, null, rootState);
        Node curNode;
        GameState curState;

        for(int i = 0; i < maxIter; i++){
            curNode = rootNode;
            curState = rootState.clone();

            //Select
            while(curNode.getUntriedMoves().isEmpty() && !curNode.getChildNodes().isEmpty()){
                curNode = curNode.UCTSelectChild();
                curState.doMove(curNode.getMove());
            }

            //Expand
            curState = curNode.expand(curState);

            //Rollout
            curState.rollOut();

            //Backpropagate
            while(curNode != null){
                curNode.update(curState.GetResult(curNode.getPlayerJustMoved()));
                curNode = curNode.getParent();
            }
        }

        return rootNode.getMostVisitedChild().getMove();

    }

}
