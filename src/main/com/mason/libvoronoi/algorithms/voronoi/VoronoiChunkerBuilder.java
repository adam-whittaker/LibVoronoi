package main.com.mason.libvoronoi.algorithms.voronoi;

import main.com.mason.libvoronoi.algorithms.AnnexQueries;
import main.com.mason.libvoronoi.algorithms.CentroidFloodFill;
import main.com.mason.libvoronoi.algorithms.FloodFillAnnexQuery;
import main.com.mason.libvoronoi.algorithms.components.CentroidData;
import main.com.mason.libvoronoi.algorithms.components.ChunkingGrid;
import main.com.mason.libvoronoi.structures.Size;

import java.util.Random;

public class VoronoiChunkerBuilder{

    public static <E extends CentroidData> VoronoiChunkerSkeleton<E> buildSkeleton(Random randomSource,
                                                                                      Size size,
                                                                                      int numChunks,
                                                                                      int lloydRelaxCount,
                                                                                      CentroidDataInitializer<E> centroidDataInitializer,
                                                                                      FloodFillAnnexQuery<E> annexQuery){
        VoronoiChunkerSkeleton<E> skeleton = new VoronoiChunkerSkeleton<>(randomSource);
        skeleton.setGrid(new ChunkingGrid<>(size));
        skeleton.setNumChunks(numChunks);
        skeleton.setLloydRelaxCount(lloydRelaxCount);
        skeleton.setCentroidDataInitializer(centroidDataInitializer);
        constructLloydRelaxFloodFill(skeleton);
        constructChunkingFloodFill(skeleton, annexQuery);
        return skeleton;
    }

    private static <E extends CentroidData> void constructLloydRelaxFloodFill(VoronoiChunkerSkeleton<E> skeleton){
        CentroidFloodFill<E> lloydFloodFill = new CentroidFloodFill<>(skeleton.getGrid(), AnnexQueries::euclideanQuery, false);
        skeleton.setLloydRelaxFloodFill(lloydFloodFill);
    }

    private static <E extends CentroidData> void constructChunkingFloodFill(VoronoiChunkerSkeleton<E> skeleton, FloodFillAnnexQuery<E> annexQuery){
        CentroidFloodFill<E> chunkingFloodFill = new CentroidFloodFill<>(skeleton.getGrid(), annexQuery, true);
        skeleton.setChunkingFloodFill(chunkingFloodFill);
    }

}
