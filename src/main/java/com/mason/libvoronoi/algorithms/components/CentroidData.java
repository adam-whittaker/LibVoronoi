package com.mason.libvoronoi.algorithms.components;

import com.mason.libvoronoi.structures.Coord;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CentroidData{


    private Coord coord;
    private final Set<Short> neighbourIDs;


    public CentroidData(Coord coord){
        this.coord = coord;
        this.neighbourIDs = new HashSet<>();
    }

    protected CentroidData(Coord coord, Set<Short> neighbourIDs){
        this.coord = coord;
        this.neighbourIDs = neighbourIDs;
    }


    public Coord getCoord(){
        return coord;
    }

    public void setCoord(Coord coord){
        this.coord = coord;
    }

    public void addNeighbourID(Short neighbourID){
        neighbourIDs.add(neighbourID);
    }

    public Iterable<Short> neighbours(){
        return neighbourIDs;
    }


    public void writeToDataStream(DataOutputStream out) throws IOException{
        out.writeInt(coord.x());
        out.writeInt(coord.y());
        out.writeInt(neighbourIDs.size());
        for(short id : neighbourIDs){
            out.writeShort(id);
        }
    }

    public static CentroidData readFromDataStream(DataInputStream in) throws IOException{
        int x = in.readInt();
        int y = in.readInt();

        int numNeighbours = in.readInt();
        Set<Short> neighbours = new HashSet<>();
        for(int n=0; n<numNeighbours; n++){
            neighbours.add(in.readShort());
        }
        return new CentroidData(new Coord(x, y), neighbours);
    }

}
