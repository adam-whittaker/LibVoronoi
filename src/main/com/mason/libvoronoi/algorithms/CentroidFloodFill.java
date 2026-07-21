package main.com.mason.libvoronoi.algorithms;

import main.com.mason.libvoronoi.algorithms.components.CentroidData;
import main.com.mason.libvoronoi.algorithms.components.ChunkingGrid;

public class CentroidFloodFill<T extends CentroidData>{


    private final ChunkingGrid<T> grid;
    private final FloodFillAnnexQuery<T> annexQuery;
    private boolean connectChunkGraph;


    public CentroidFloodFill(ChunkingGrid<T> grid, FloodFillAnnexQuery<T> annexQuery, boolean connectChunkGraph){
        this.grid = grid;
        this.annexQuery = annexQuery;
        this.connectChunkGraph = connectChunkGraph;
    }


    public void shouldConnectChunkGraph(boolean connectChunkGraph){
        this.connectChunkGraph = connectChunkGraph;
    }

    public void floodFill(){
        CentroidFloodFillFrontier frontier = new CentroidFloodFillFrontier(grid);
        Integer currentIdx;
        while(!frontier.isEmpty()){
            currentIdx = frontier.poll();
            annexEligibleNeighbors(currentIdx, frontier);
        }
    }

    private void annexEligibleNeighbors(Integer currentIdx, CentroidFloodFillFrontier frontier){
        short centroidID = grid.centroidID(currentIdx);
        short newDistFromCentroid = (short)(grid.distanceToCentroid(currentIdx) + 1);
        for(Integer neighbourIdx : frontier.cardinalNeighbours(currentIdx)){
            if(shouldAnnex(centroidID, neighbourIdx)){
                frontier.register(centroidID, neighbourIdx, newDistFromCentroid);
            }
        }
    }

    private boolean shouldAnnex(Short centroidID, Integer targetIdx){
        if(grid.isCentroid(targetIdx)){
            return false;
        }
        if(!grid.hasCentroid(targetIdx)){
            return true;
        }
        if(centroidAlreadyAnnexedPoint(centroidID, targetIdx)){
            return false;
        }

        boolean canAnnex = annexQuery.canAnnex(grid, centroidID, targetIdx);
        if(connectChunkGraph && !canAnnex){
            connectChunk(centroidID, targetIdx);
        }
        return canAnnex;
    }

    private boolean centroidAlreadyAnnexedPoint(Short centroidID, Integer targetIdx){
        return grid.centroidID(targetIdx) == centroidID;
    }

    private void connectChunk(Short centroidID, Integer targetIdx){
        CentroidData data = grid.getCentroidDataByID(centroidID);
        data.addNeighbourID(grid.centroidID(targetIdx));
    }

}
