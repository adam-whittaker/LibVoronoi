package main.com.mason.libvoronoi.algorithms;

import main.com.mason.libvoronoi.algorithms.components.CentroidData;
import main.com.mason.libvoronoi.algorithms.components.ChunkingGrid;

public interface FloodFillAnnexQuery<T extends CentroidData>{


    boolean canAnnex(ChunkingGrid<T> grid, Short centroidID, Integer targetIdx);

}
