package main.com.mason.libvoronoi.algorithms.voronoi;

import main.com.mason.libvoronoi.algorithms.components.CentroidData;
import main.com.mason.libvoronoi.structures.Coord;

public interface CentroidDataInitializer<T extends CentroidData>{

    T initializeCentroid(Coord coord);

}
