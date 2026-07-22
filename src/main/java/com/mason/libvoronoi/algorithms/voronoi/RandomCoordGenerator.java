package com.mason.libvoronoi.algorithms.voronoi;

import com.mason.libstruct.geo.Coord;
import com.mason.libstruct.geo.Size;

import java.util.Set;

public interface RandomCoordGenerator{


    Set<Coord> generateRandomDistinctCoords(Size bounds, int numCoords);

}
