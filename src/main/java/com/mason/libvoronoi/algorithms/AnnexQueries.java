package com.mason.libvoronoi.algorithms;

import com.mason.libvoronoi.algorithms.components.CentroidData;
import com.mason.libvoronoi.algorithms.components.ChunkingGrid;
import com.mason.libvoronoi.structures.Coord;

import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public final class AnnexQueries{


    private AnnexQueries(){}


    public static boolean manhattanQuery(ChunkingGrid<?> grid, Short centroidID, Integer targetIdx){
        return grid.distanceToCentroid(targetIdx) > manhattanDist(grid.getCentroidCoord(centroidID), grid.asCoord(targetIdx));
    }

    private static int manhattanDist(Coord a, Coord b){
        return abs(a.x() - b.x()) + abs(a.y() - b.y());
    }


    public static boolean euclideanQuery(ChunkingGrid<?> grid, Short centroidID, Integer targetIdx){
        Coord targetCoord = grid.asCoord(targetIdx);
        CentroidData targetCentroidData = grid.getCentroidDataByIndex(targetIdx);
        int targetOwnCentroidSquareDist = squareDist(targetCentroidData.getCoord(), targetCoord);
        int targetAnnexingCentroidSquareDist = squareDist(grid.getCentroidCoord(centroidID), targetCoord);
        return targetOwnCentroidSquareDist > targetAnnexingCentroidSquareDist;
    }

    static int squareDist(Coord a, Coord b){
        return (a.x()-b.x())*(a.x()-b.x()) + (a.y()-b.y())*(a.y()-b.y());
    }

    public static double euclideanDist(Coord a, Coord b){
        return Math.sqrt(squareDist(a, b));
    }


    public static boolean minkowskiQuery(ChunkingGrid<?> grid, CentroidData centroidData, Integer targetIdx){
        Coord targetCoord = grid.asCoord(targetIdx);
        CentroidData targetCentroidData = grid.getCentroidDataByIndex(targetIdx);
        int targetOwnCentroidDist = minkowskiDist(targetCentroidData.getCoord(), targetCoord);
        int targetAnnexingCentroidDist = minkowskiDist(centroidData.getCoord(), targetCoord);
        return targetOwnCentroidDist > targetAnnexingCentroidDist;
    }

    private static int minkowskiDist(Coord a, Coord b){
        return max(abs(a.x()-b.x()), abs(a.y()-b.y()));
    }


    public static <T extends CentroidData> FloodFillAnnexQuery<T> buildDefaultRandomQuery(Random source){
        return new RandomAnnexQuery<>(source);
    }

    public static <T extends CentroidData> FloodFillAnnexQuery<T> buildRandomQuery(Random source, double surroundedTakeOverChance, double semiTakeOverChance){
        return new RandomAnnexQuery<>(source, surroundedTakeOverChance, semiTakeOverChance);
    }

}
