package com.mason.libvoronoi.algorithms;

import com.mason.libvoronoi.algorithms.components.ChunkingGrid;
import com.mason.libstruct.geo.Coord;
import com.mason.libstruct.lowMemory.CardinalIndexNeighbours;

import java.util.ArrayDeque;
import java.util.Queue;

public class CentroidFloodFillFrontier{


    private final Queue<Integer> frontier;
    private final ChunkingGrid<?> grid;


    CentroidFloodFillFrontier(ChunkingGrid<?> grid){
        this.grid = grid;
        frontier = constructFrontier(grid);
    }

    private static Queue<Integer> constructFrontier(ChunkingGrid<?> grid){
        Queue<Integer> frontier = new ArrayDeque<>();
        Coord centroidCoord;
        short ZERO = 0;
        for(Short centroidID : grid.getAllCentroidIDs()){
            centroidCoord = grid.getCentroidCoord(centroidID);
            frontier.add(grid.asIndex(centroidCoord));
            grid.setCentroid(centroidCoord, centroidID, ZERO);
        }
        return frontier;
    }


    void register(short centroidID, Integer pointIdx, short distFromCentroid){
        frontier.add(pointIdx);
        grid.setCentroidByIndex(pointIdx, centroidID, distFromCentroid);
    }

    boolean isEmpty(){
        return frontier.isEmpty();
    }

    Integer poll(){
        return frontier.poll();
    }


    Iterable<Integer> cardinalNeighbours(Integer index){
        return new CardinalIndexNeighbours(index, grid.size());
    }

}
