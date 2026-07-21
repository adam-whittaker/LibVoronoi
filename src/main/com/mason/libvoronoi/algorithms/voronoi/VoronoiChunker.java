package main.com.mason.libvoronoi.algorithms.voronoi;

import main.com.mason.libvoronoi.algorithms.CentroidCounterMap;
import main.com.mason.libvoronoi.algorithms.CentroidFloodFill;
import main.com.mason.libvoronoi.algorithms.FloodFillAnnexQuery;
import main.com.mason.libvoronoi.algorithms.components.CentroidData;
import main.com.mason.libvoronoi.algorithms.components.ChunkingGrid;
import main.com.mason.libvoronoi.structures.Coord;
import main.com.mason.libvoronoi.structures.Size;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class VoronoiChunker<T extends CentroidData>{


    private final ChunkingGrid<T> grid;
    private final int numChunks;
    private final int lloydRelaxCount;
    private final RandomCoordGenerator randomCoordGenerator;
    private final CentroidDataInitializer<T> centroidDataInitializer;
    private final CentroidFloodFill<T> lloydRelaxFloodFill;
    private final CentroidFloodFill<T> chunkingFloodFill;


    protected VoronoiChunker(VoronoiChunkerSkeleton<T> skeleton){
        grid = skeleton.getGrid();
        numChunks = skeleton.getNumChunks();
        lloydRelaxCount = skeleton.getLloydRelaxCount();
        centroidDataInitializer = skeleton.getCentroidDataInitializer();
        lloydRelaxFloodFill = skeleton.getLloydRelaxFloodFill();
        chunkingFloodFill = skeleton.getChunkingFloodFill();
        randomCoordGenerator = skeleton.getRandomCoordGenerator();
    }

    public static <E extends CentroidData> VoronoiChunker<E> build(Random randomSource,
                                          Size size,
                                          int numChunks,
                                          int lloydRelaxCount,
                                          CentroidDataInitializer<E> centroidDataInitializer,
                                          FloodFillAnnexQuery<E> annexQuery){
        return new VoronoiChunker<>(VoronoiChunkerBuilder.buildSkeleton(randomSource, size, numChunks, lloydRelaxCount, centroidDataInitializer, annexQuery));
    }

    public static <E extends CentroidData> VoronoiChunker<E> buildFromSkeleton(VoronoiChunkerSkeleton<E> skeleton){
        return new VoronoiChunker<>(skeleton);
    }


    public void createChunks(){
        placeCentroidsRandomly();
        lloydRelax();
        chunkingFloodFill.floodFill();
    }

    public ChunkingGrid<T> getGrid(){
        return grid;
    }

    private void placeCentroidsRandomly(){
        Set<Coord> coords = randomCoordGenerator.generateRandomDistinctCoords(grid.size(), numChunks);
        for(Coord coord : coords){
            grid.createCentroid(centroidDataInitializer.initializeCentroid(coord));
        }
    }

    private void lloydRelax(){
        for(int n=0; n<lloydRelaxCount; n++){
            lloydRelaxFloodFill.floodFill();
            resetCentroidsToChunkCentre();
        }
    }

    private void resetCentroidsToChunkCentre(){
        CentroidCounterMap counterMap = new CentroidCounterMap(grid);
        counterMap.averageAndClearCentroids();
        moveCentroidsToChunkCentre(counterMap);
    }

    private void moveCentroidsToChunkCentre(CentroidCounterMap counterMap){
        for(Map.Entry<Short, long[]> centroidPair : counterMap){
            grid.moveCentroid(centroidPair.getKey(), counterMap.counterToCoord(centroidPair.getValue()));
        }
    }

}
