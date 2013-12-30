import java.util.*;

/**
 * Represents the state (position, current turn, etc)
 */
public class EdgeConnectState extends GameState {

    private class CellCoordinate {
        public final int x;
        public final int y;
        public final int z;

        public CellCoordinate(int x, int y, int z){
            assert x + y + z == 0;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public boolean equals(CellCoordinate c){
            if (c == null) return false;
            if (c == this) return true;
            return this.x == c.x && this.y == c.y && this.z == c.z;
        }

        public int hashCode(){
            return (x+20) + (y+20) * 40 + (z+20) * 1600;
        }

        public List<CellCoordinate> getNeighbors(){
            List<CellCoordinate> result = new ArrayList<CellCoordinate>();
            if(Math.max(Math.abs(x), Math.abs(y), Math.abs(z)) == 1){
                if(x == -1 && y == 1 && z == 0){
                    result.add(new CellCoordinate(1, -1, 0));
                    result.add(new CellCoordinate(-1, 0, 1));
                    result.add(new CellCoordinate(1, 0, -1));
                    result.add(new CellCoordinate(0, -1, 1));
                    result.add(new CellCoordinate(0, 1, -1));

                    result.add(new CellCoordinate(-2, 2, 0));
                    result.add(new CellCoordinate(-2, 1, 1));
                    result.add(new CellCoordinate(-1, 2, -1));
                } else if(x == 1 && y == -1 && z == 0){
                    result.add(new CellCoordinate(-1, 1, 0));
                    result.add(new CellCoordinate(-1, 0, 1));
                    result.add(new CellCoordinate(1, 0, -1));
                    result.add(new CellCoordinate(0, -1, 1));
                    result.add(new CellCoordinate(0, 1, -1));

                    result.add(new CellCoordinate(2, -2, 0));
                    result.add(new CellCoordinate(2, -1, -1));
                    result.add(new CellCoordinate(1, -2, 1));
                } else if(x == -1 && y == 0 && z == 1){
                    result.add(new CellCoordinate(1, -1, 0));
                    result.add(new CellCoordinate(-1, 0, 1));
                    result.add(new CellCoordinate(1, 0, -1));
                    result.add(new CellCoordinate(0, -1, 1));
                    result.add(new CellCoordinate(0, 1, -1));

                    result.add(new CellCoordinate(-2, 1, 1));
                    result.add(new CellCoordinate(-2, 0, 2));
                    result.add(new CellCoordinate(-1, -1, 2));
                } else if(x == 1 && y == 0 && z == -1){
                    result.add(new CellCoordinate(-1, 1, 0));
                    result.add(new CellCoordinate(-1, 0, 1));
                    result.add(new CellCoordinate(1, 0, -1));
                    result.add(new CellCoordinate(0, -1, 1));
                    result.add(new CellCoordinate(0, 1, -1));

                    result.add(new CellCoordinate(2, -1, -1));
                    result.add(new CellCoordinate(2, 0, -2));
                    result.add(new CellCoordinate(1, 1, -2));
                } else if(x == 0 && y == -1 && z == 1){
                    result.add(new CellCoordinate(-1, 1, 0));
                    result.add(new CellCoordinate(1, -1, 0));
                    result.add(new CellCoordinate(-1, 0, 1));
                    result.add(new CellCoordinate(1, 0, -1));
                    result.add(new CellCoordinate(0, 1, -1));

                    result.add(new CellCoordinate(1, -2, 1));
                    result.add(new CellCoordinate(-1, -1, 2));
                    result.add(new CellCoordinate(0, -2, 2));
                } else if(x == 0 && y == 1 && z == -1){
                    result.add(new CellCoordinate(-1, 1, 0));
                    result.add(new CellCoordinate(1, -1, 0));
                    result.add(new CellCoordinate(-1, 0, 1));
                    result.add(new CellCoordinate(1, 0, -1));
                    result.add(new CellCoordinate(0, -1, 1));

                    result.add(new CellCoordinate(-1, 2, -1));
                    result.add(new CellCoordinate(1, 1, -2));
                    result.add(new CellCoordinate(0, 2, -2));
                }
            } else {
                result.add(new CellCoordinate(x-1, y+1, z));
                result.add(new CellCoordinate(x+1, y-1, z));
                result.add(new CellCoordinate(x-1, y, z+1));
                result.add(new CellCoordinate(x+1, y, z-1));
                result.add(new CellCoordinate(x, y-1, z+1));
                result.add(new CellCoordinate(x, y+1, z-1));
            }
            return result;
        }
    }

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

        public int getState(){
            return state;
        }

        public int setState(int state){
            this.state = state;
        }

        public int getGroup(){
            return group;
        }

        public int setGroup(int group){
            self.group = group;
        }

        public int getScoreState(){
            return scoreState;
        }

        public int setScoreState(int scoreState){
            this.scoreState = scoreState;
        }

        public void setBonus(){
            bonus = true;
        }

        public boolean isEdge(){
            return edge;
        }

        public void setEdge(){
            edge = true;
        }
    }

    private Map<CellCoordinate, Cell> cellMap;
    private int boardSize;

    public EdgeConnectState(int boardSize){
        cellMap = new HashMap<CellCoordinate, Cell>();
        this.boardSize = boardSize;
        for(int i = -boardSize; i <= boardSize; i++){
            for(int j = -boardSize; j <= boardSize; j++){
                int k = -i - j;
                if((k <= boardSize && k >= -boardSize) && !(i == 0 && j == 0){
                    Cell c = new Cell(i, j, k);
                    if(Math.max(Math.abs(i), Math.abs(j), Math.abs(k)) == boardSize){
                        c.setEdge();
                    }

                    cellMap.put(new CellCoordinate(i, j, k), c);
                }

            }
        }
    }

    public List<Cell> getNeighbors(CellCoordinate coord){
        List<Cell> result = new ArrayList<Cell>();
        List<CellCoordinate> initList = coord.getNeighbors();
        for(Cell Coordinate c : initList){
            if(CellMap.containsKey(c)){
                result.add(CellMap.get(c));
            }
        }
        return result;
    }

    public void setBonus(List<CellCoordinate> bonusCells){
        for(CellCoordinate bonusCell : bonusCells){
            cellMap.get(bonusCell).setBonus();
        }
    }

    
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
    public abstract List<GameMove> getLegalMoves();

    /**
     * Get a list of legal moves.
     */
    public abstract GameMove getRandomMove();

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
            int[] numGroups = {0, 0, 0};
            for(Cell cell : cellMap.values()){
                cell.setGroup(-1);
            }
            for(Cell c : cellMap.values()){
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

            for(Cell c : cellMap.values()){
                if(cell.isEdge()){
                    numEdgeNodes[cell.getScoreState()][cell.getGroup()] += 1;
                }
            }

            for(Cell c : cellMap.values()){
                if(numEdgeNodes[cell.getScoreState()][cell.getGroup()] < 2){
                    cell.toggleScoreState();
                    done = false;
                }
            }
        }

    }


    /**
     * Return a string representation of the game state.
     */
    public abstract String toString();

    /**
     * Keep playing random moves until end is reached
     */
    public abstract void rollOut();

    /**
     * Return the player number that just moved
     */
    public abstract int getPlayerJustMoved();

}
