package main.com.mason.libvoronoi.algorithms.voronoi;

import main.com.mason.libvoronoi.structures.Coord;
import main.com.mason.libvoronoi.structures.Size;

import java.util.Set;

public interface RandomCoordGenerator{


    Set<Coord> generateRandomDistinctCoords(Size bounds, int numCoords);

}
