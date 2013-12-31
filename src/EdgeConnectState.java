import java.util.*;

/**
 * Represents the state (position, current turn, etc)
 */
public class EdgeConnectState extends GameState {

    private class Cell {

        private CellCoordinate coord;

        private boolean bonus;
        private boolean edge;

        private int state;
        private int scoreState;
        private int group;

        public Cell(CellCoordinate coord){
            this.coord = coord;
            edge = false;
            bonus = false;
            state = 0;
            scoreState = 0;
            group = -1;
        }

        public boolean isBonus(){
            return bonus;
        }

        public Cell clone(){
            Cell result = new Cell(coord);
            result.setEdge(edge);
            result.setBonus(bonus);
            result.setState(state);
            return result;
        }

        public int getState(){
            return state;
        }

        public void toggleScoreState(){
            assert scoreState != 0;
            scoreState = 3 - scoreState;
        }
        public CellCoordinate getCoord(){
            return coord;
        }

        public void setState(int state){
            this.state = state;
        }

        public int getGroup(){
            return group;
        }

        public void setGroup(int group){
            this.group = group;
        }

        public int getScoreState(){
            return scoreState;
        }

        public void setScoreState(int scoreState){
            this.scoreState = scoreState;
        }

        public void setBonus(boolean bonus){
            this.bonus = bonus;
        }

        public boolean isEdge(){
            return edge;
        }

        public void setEdge(boolean edge){
            this.edge = edge;
        }
    }

    private Map<CellCoordinate, Cell> cellMap;
    private int boardSize;
    private int playerJustMoved;
    private int movesLeft;

    Random generator = new Random();

    public EdgeConnectState(int boardSize, List<CellCoordinate> bonusCells){
        cellMap = new HashMap<CellCoordinate, Cell>();
        this.boardSize = boardSize;
        for(int i = -boardSize; i <= boardSize; i++){
            for(int j = -boardSize; j <= boardSize; j++){
                int k = -i - j;
                if((k <= boardSize && k >= -boardSize) && !(i == 0 && j == 0)){
                    CellCoordinate coord = new CellCoordinate(i, j, k);
                    Cell cell = new Cell(coord);
                    if(Math.max(Math.abs(i), Math.max(Math.abs(j), Math.abs(k))) == boardSize){
                        cell.setEdge(true);
                    }
                    cellMap.put(coord, cell);
                }
            }
        }
        for(CellCoordinate bonusCell : bonusCells){
            cellMap.get(bonusCell).setBonus(true);
        }
        playerJustMoved = 1;
        movesLeft = 1;
    }

    public EdgeConnectState(int boardSize, Map<CellCoordinate, Cell> newMap, int playerJustMoved, int movesLeft){
        cellMap = new HashMap<CellCoordinate, Cell>();
        this.boardSize = boardSize;
        for(CellCoordinate coord : newMap.keySet()){
            cellMap.put(coord, newMap.get(coord).clone());
        }
        this.playerJustMoved = playerJustMoved;
        this.movesLeft = movesLeft;
    }

    public List<Cell> getNeighbors(CellCoordinate coord){
        List<Cell> result = new ArrayList<Cell>();
        List<CellCoordinate> initList = coord.getNeighbors();
        for(CellCoordinate c : initList){
            if(cellMap.containsKey(c)){
                result.add(cellMap.get(c));
            }
        }
        return result;
    }

    /**
     * Create a deep clone of this game state.
     */
    public GameState clone(){
        return new EdgeConnectState(boardSize, cellMap, playerJustMoved, movesLeft);
    }

    /**
     * Update the state by carrying out the given move.
     */
    public void doMove(GameMove move){
        EdgeConnectMove eMove = (EdgeConnectMove)move;
        assert cellMap.get(eMove.getCoord()).getState() == 0;

        if(movesLeft == 0){
            playerJustMoved = 3 - playerJustMoved;
            movesLeft = 2;
        }
        cellMap.get(eMove.getCoord()).setState(playerJustMoved);
        movesLeft -= 1;
    }

    /**
     * Get a list of legal moves.
     */
    public List<GameMove> getLegalMoves(){
        List<GameMove> legalMoves = new ArrayList<GameMove>();
        for(CellCoordinate coord : cellMap.keySet()){
            if(cellMap.get(coord).getState() == 0){
                legalMoves.add(new EdgeConnectMove(coord));
            }

        }

        return legalMoves;
    }

    /**
     * Get a list of legal moves.
     */

    public GameMove getRandomMove(){
        List<GameMove> possibleMoves = getLegalMoves();
        GameMove move = possibleMoves.get(generator.nextInt(possibleMoves.size()));
        return move;
    }

    public void floodFill(Cell startCell){
        int scoreState = startCell.getScoreState();
        int groupNum = startCell.getGroup();
        CellCoordinate coord = startCell.getCoord();
        Stack<CellCoordinate> stack = new Stack<CellCoordinate>();
        stack.push(coord);
        while(!stack.empty()){
            coord = stack.pop();
            //unchecked neighbors
            List<CellCoordinate> neighbors = coord.getNeighbors();
            for(CellCoordinate neighbor : neighbors){
                if(cellMap.containsKey(neighbor)){ //if such cell exists
                    Cell cell = cellMap.get(neighbor);
                    if(cell.getGroup() == -1 && cell.getScoreState() == scoreState){
                        cell.setGroup(groupNum);
                        stack.push(neighbor);
                    }
                }
            }
        }
    }

    /**
     * Get the result from the perspective of the given player.
     */
    public double getResult(int playerNum){
        for(Cell c : cellMap.values()){
            if(c.getState() == 1){
                c.setScoreState(1);
            } else { 
                c.setScoreState(2);
            }
        }

        int[] numGroups = {0, 0, 0};
        boolean done = false;
        int iterations = 0;

        while(!done && iterations < 1000){
            done = true;
            iterations += 1;
            numGroups[0] = 0;
            numGroups[1] = 0;
            numGroups[2] = 0;
            for(Cell cell : cellMap.values()){
                cell.setGroup(-1);
            }
            for(Cell cell : cellMap.values()){
                if(cell.group == -1){
                    cell.setGroup(numGroups[cell.getScoreState()]);
                    floodFill(cell);
                    numGroups[cell.getScoreState()] += 1;
                }
            }

            int[][] numEdgeNodes = {null, new int[numGroups[1]], new int[numGroups[2]]};
            for(int i = 0; i < numEdgeNodes[1].length; i++){
                numEdgeNodes[1][i] = 0;
            }
            for(int i = 0; i < numEdgeNodes[2].length; i++){
                numEdgeNodes[2][i] = 0;
            }

            for(Cell cell : cellMap.values()){
                if(cell.isEdge()){
                    numEdgeNodes[cell.getScoreState()][cell.getGroup()] += 1;
                }
            }

            for(Cell cell : cellMap.values()){
                if(numEdgeNodes[cell.getScoreState()][cell.getGroup()] < 2){
                    cell.toggleScoreState();
                    done = false;
                }
            }
        }
        assert done;

        int[] numEdges = {0, 0, 0};
        int[] numBonus = {0, 0, 0};

        for(Cell cell : cellMap.values()){
            if(cell.isEdge())
                numEdges[cell.getScoreState()] += 1;
            if(cell.isBonus())
                numBonus[cell.getScoreState()] += 1;
        }
        
    
        //This could be rewritten to calculate only p1Score, and figure out P2 score by sutracting from max score
        int p1Score = numEdges[1] + (numGroups[2] - numGroups[1])*2;
        int p2Score = numEdges[2] + (numGroups[1] - numGroups[2])*2;
        if(numBonus[1] > numBonus[2]){
            p1Score += 1;
        } else {
            p2Score += 1;
        }

        assert p1Score + p2Score == boardSize * 6 + 1;

        if(playerNum == 1){
            if(p1Score > p2Score){
                return 1.0;
            } else {
                return 0.0;
            }
        } else {
            if(p1Score > p2Score){
                return 0.0;
            } else {
                return 1.0;
            }
        }
    }


    /**
     * Return a string representation of the game state.
     */
    public String toString(){
        return "";
    }

    /**
     * Keep playing random moves until end is reached
     */
    public void rollOut(){
        while(!getLegalMoves().isEmpty()){
            doMove(getRandomMove());
        }
    }

    /**
     * Return the player number that just moved
     */
    public int getPlayerJustMoved(){
        return playerJustMoved;
    }

}
