package main.com.mason.libvoronoi.algorithms.components;

import main.com.mason.libvoronoi.io.CentroidDataReader;
import main.com.mason.libvoronoi.structures.Coord;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CentroidIDMap<T extends CentroidData>{


    public static final short CENTROID_UNSET = -1;

    private final Map<Short, T> map;
    private short nextID = 0;


    public CentroidIDMap(){
        map = new HashMap<>();
    }

    private CentroidIDMap(Map<Short, T> map, short nextID){
        this.map = map;
        this.nextID = nextID;
    }


    public boolean isCentroid(short id){
        return map.containsKey(id);
    }

    private void putCentroid(short id, T data){
        map.put(id, data);
    }

    public T getCentroidData(short id){
        return map.get(id);
    }

    public Coord getCentroidCoord(short id){
        T data = getCentroidData(id);
        return data.getCoord();
    }

    public void moveCentroid(short id, Coord newCoord){
        T data = map.get(id);
        data.setCoord(newCoord);
    }

    public short createCentroidAndReturnID(T data){
        short id = generateID();
        putCentroid(id, data);
        return id;
    }

    private short generateID(){
        short id = nextID;
        nextID++;
        return id;
    }

    public Iterable<Short> getCentroidIDs(){
        return map.keySet();
    }


    public void writeToDataStream(DataOutputStream out) throws IOException{
        out.writeShort(nextID);
        out.writeInt(map.size());
        for(Map.Entry<Short, T> entry : map.entrySet()){
            out.writeShort(entry.getKey());
            entry.getValue().writeToDataStream(out);
        }
    }

    public static <T extends CentroidData> CentroidIDMap<T> readFromDataStream(
            DataInputStream in, CentroidDataReader<T> centroidReader) throws IOException{

        short nextID = in.readShort();
        int count = in.readInt();

        Map<Short, T> map = new HashMap<>();
        for(int i = 0; i < count; i++){
            short id = in.readShort();
            T data = centroidReader.readFromDataStream(in);
            map.put(id, data);
        }
        return new CentroidIDMap<>(map, nextID);
    }

}
