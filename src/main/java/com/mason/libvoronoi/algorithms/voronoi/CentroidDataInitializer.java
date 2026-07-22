package com.mason.libvoronoi.algorithms.voronoi;

import com.mason.libvoronoi.algorithms.components.CentroidData;
import com.mason.libstruct.geo.Coord;

public interface CentroidDataInitializer<T extends CentroidData>{

    T initializeCentroid(Coord coord);

}
