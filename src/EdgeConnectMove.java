/**
 * Represents a move in a game
 */
public class EdgeConnectMove extends GameMove {

    CellCoordinate coord;
    
    public EdgeConnectMove(CellCoordinate coord){
        this.coord = coord;
    }

    public CellCoordinate getCoord(){
        return coord;
    }

    /**
     * Return a string representation of the move
     */
    public String toString(){
        return coord.toString();
    }

}
