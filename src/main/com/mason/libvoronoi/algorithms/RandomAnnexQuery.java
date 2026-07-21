package main.com.mason.libvoronoi.algorithms;

import main.com.mason.libvoronoi.algorithms.components.CentroidData;
import main.com.mason.libvoronoi.algorithms.components.ChunkingGrid;
import main.com.mason.libvoronoi.structures.Coord;

import java.util.Random;

import static main.com.mason.libvoronoi.algorithms.AnnexQueries.squareDist;

public class RandomAnnexQuery<T extends CentroidData> implements FloodFillAnnexQuery<T>{


    private final Random R;
    private final double surroundedTakeOverChance;
    private final double semiTakeOverChance;


    RandomAnnexQuery(Random random, double surroundedTakeOverChance, double semiTakeOverChance){
        this.R = random;
        this.surroundedTakeOverChance = surroundedTakeOverChance;
        this.semiTakeOverChance = semiTakeOverChance;
    }

    RandomAnnexQuery(Random random){
        this(random, 0.9, 0.65);
    }


    @Override
    public boolean canAnnex(ChunkingGrid<T> grid, Short centroidID, Integer targetIdx){
        return canAnnexByDistance(grid, centroidID, targetIdx)
                || canAnnexBySurrounding(grid, centroidID, targetIdx);
    }

    private boolean canAnnexByDistance(ChunkingGrid<T> grid, Short centroidID, Integer targetIdx){
        Coord targetCoord = grid.asCoord(targetIdx);
        CentroidData targetCentroidData = grid.getCentroidDataByIndex(targetIdx);
        int targetOwnCentroidDist = squareDist(targetCentroidData.getCoord(), targetCoord);
        int targetAnnexingCentroidDist = squareDist(grid.getCentroidCoord(centroidID), targetCoord);
        return R.nextDouble() * (targetAnnexingCentroidDist + targetOwnCentroidDist) < targetOwnCentroidDist;
    }

    private boolean canAnnexBySurrounding(ChunkingGrid<T> grid, Short centroidID, Integer targetIdx){
        int adjacents = numCardinalAdjacents(grid, centroidID, targetIdx);
        if(adjacents == 4){
            return R.nextDouble() < surroundedTakeOverChance;
        }
        if(adjacents == 3){
            return R.nextDouble() < semiTakeOverChance;
        }
        return false;
    }

    private int numCardinalAdjacents(ChunkingGrid<?> grid, Short centroidID, Integer targetIdx){
        int adjacents = 0;
        for(Integer neighbourIdx : grid.cardinalNeighbours(targetIdx)){
            if(!grid.hasCentroid(neighbourIdx)){
                continue;
            }
            if(grid.centroidID(neighbourIdx) == centroidID){
                adjacents++;
            }
        }
        return adjacents;
    }

}
