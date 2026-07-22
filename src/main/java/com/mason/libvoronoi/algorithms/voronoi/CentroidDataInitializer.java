package com.mason.libvoronoi.algorithms.voronoi;

import com.mason.libvoronoi.algorithms.components.CentroidData;
import com.mason.libvoronoi.structures.Coord;

public interface CentroidDataInitializer<T extends CentroidData>{

    T initializeCentroid(Coord coord);

}
