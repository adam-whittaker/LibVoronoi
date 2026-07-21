package main.com.mason.libvoronoi.structures.lowMemory;

import main.com.mason.libvoronoi.structures.Coord;
import main.com.mason.libvoronoi.structures.Size;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ShortGrid{

    private final Size size;
    private final short[] grid;


    public ShortGrid(Size size){
        this.size = size;
        grid = new short[size.width()*size.height()];
    }

    public ShortGrid(Size size, short initialValue){
        this(size);
        Arrays.fill(grid, initialValue);
    }

    private ShortGrid(Size size, short[] grid){
        this.size = size;
        this.grid = grid;
    }


    public int asIndex(Coord coord){
        return index(coord.x(), coord.y());
    }

    private int index(int x, int y){
        return y * size.width() + x;
    }

    public short get(int x, int y){
        return getByIndex(index(x, y));
    }

    public short getByIndex(int idx){
        return grid[idx];
    }

    public void set(int x, int y, short value){
        setByIndex(index(x, y), value);
    }

    public void setByIndex(int idx, short value){
        grid[idx] = value;
    }

    public Size size(){
        return size;
    }

    public short max(){
        short max = Short.MIN_VALUE;
        for(short value : grid){
            if(max < value){
                max = value;
            }
        }
        return max;
    }


    public void writeToDataStream(DataOutputStream out) throws IOException{
        out.writeInt(size.width());
        out.writeInt(size.height());
        for (short value : grid) {
            out.writeShort(value);
        }
    }

    public static ShortGrid readFromDataStream(DataInputStream in) throws IOException{
        int width = in.readInt();
        int height = in.readInt();
        Size size = new Size(width, height);

        short[] grid = new short[width * height];
        for(int i = 0; i < grid.length; i++){
            grid[i] = in.readShort();
        }
        return new ShortGrid(size, grid);
    }

}
