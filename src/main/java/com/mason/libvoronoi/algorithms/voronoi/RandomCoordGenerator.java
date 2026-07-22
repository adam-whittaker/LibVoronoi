package com.mason.libvoronoi.algorithms.voronoi;

import com.mason.libvoronoi.structures.Coord;
import com.mason.libvoronoi.structures.Size;

import java.util.Set;

public interface RandomCoordGenerator{


    Set<Coord> generateRandomDistinctCoords(Size bounds, int numCoords);

}
