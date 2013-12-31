import java.util.*;

public class CellCoordinate implements Comparable<CellCoordinate> {
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

    public int compareTo(CellCoordinate other){
        if(this.x > other.getX()) return 1;
        if(this.x < other.getX()) return -1;
        if(this.y > other.getY()) return 1;
        if(this.y < other.getY()) return -1;
        if(this.z > other.getZ()) return 1;
        if(this.z < other.getZ()) return -1;
        return 0;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getZ(){
        return z;
    }

    public int hashCode(){
        return (x+20) + (y+20) * 40 + (z+20) * 1600;
    }

    public List<CellCoordinate> getNeighbors(){
        List<CellCoordinate> result = new ArrayList<CellCoordinate>();
        if(Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(z))) == 1){
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
