package main.com.mason.libvoronoi.algorithms;

import main.com.mason.libvoronoi.algorithms.components.ChunkingGrid;
import main.com.mason.libvoronoi.structures.Coord;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CentroidCounterMap implements Iterable<Map.Entry<Short, long[]>>{


    /** Indexes:
     * 0: avg x
     * 1: avg y
     * 2: num points allocated to centroid
    */
    private final Map<Short, long[]> counterMap;
    private final ChunkingGrid<?> grid;


    public CentroidCounterMap(ChunkingGrid<?> grid){
        this.grid = grid;
        counterMap = constructCounterMap(grid);
    }

    private static Map<Short,long[]> constructCounterMap(ChunkingGrid<?> grid){
        Map<Short, long[]> counterMap = new HashMap<>();
        for(Short centroidID : grid.getAllCentroidIDs()){
            counterMap.put(centroidID, new long[3]);
        }
        return counterMap;
    }


    public void averageAndClearCentroids(){
        long[] currentCounter;
        int[] currentCoord = {0, 0};
        for(Integer pointIdx : grid.pointIndices()){
            currentCounter = counterMap.get(grid.centroidID(pointIdx));
            currentCounter[0] += currentCoord[0];
            currentCounter[1] += currentCoord[1];
            currentCounter[2]++;
            incrementCurrentCoord(currentCoord);
            grid.unsetCentroidByIndex(pointIdx);
        }
        divideCountersByChunkSize();
    }

    private void incrementCurrentCoord(int[] currentCoord){
        currentCoord[0]++;
        if(currentCoord[0] >= grid.size().width()){
            currentCoord[0] = 0;
            currentCoord[1]++;
        }
    }

    private void divideCountersByChunkSize(){
        for(long[] counter : counterMap.values()){
            counter[0] /= counter[2];
            counter[1] /= counter[2];
        }
    }

    public Coord counterToCoord(long[] counter){
        return new Coord((int)counter[0], (int)counter[1]);
    }


    @Override
    public Iterator<Map.Entry<Short, long[]>> iterator(){
        return counterMap.entrySet().iterator();
    }

}
