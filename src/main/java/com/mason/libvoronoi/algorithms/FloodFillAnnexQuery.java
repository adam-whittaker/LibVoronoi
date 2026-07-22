package com.mason.libvoronoi.algorithms;

import com.mason.libvoronoi.algorithms.components.CentroidData;
import com.mason.libvoronoi.algorithms.components.ChunkingGrid;

public interface FloodFillAnnexQuery<T extends CentroidData>{


    boolean canAnnex(ChunkingGrid<T> grid, Short centroidID, Integer targetIdx);

}
