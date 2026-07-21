package main.com.mason.libvoronoi.algorithms;

import main.com.mason.libvoronoi.algorithms.components.CentroidData;
import main.com.mason.libvoronoi.algorithms.components.ChunkingGrid;
import main.com.mason.libvoronoi.structures.Coord;

import java.util.*;

import static main.com.mason.libvoronoi.algorithms.AnnexQueries.euclideanDist;

public class CentroidNeighbourhoodSearch<T extends CentroidData>{


    private final ChunkingGrid<T> grid;


    public CentroidNeighbourhoodSearch(ChunkingGrid<T> grid){
        this.grid = grid;
    }


    public Iterable<Short> centroidNeighbourhood(Short centroidID, int maxDist){
        Coord startCoord = grid.getCentroidCoord(centroidID);
        Set<Short> found = new HashSet<>();
        Deque<Short> frontier = new ArrayDeque<>(8);
        frontier.add(centroidID);
        found.add(centroidID);
        short current;
        while(!frontier.isEmpty()){
            current = frontier.pop();
            for(Short neighbour : grid.getCentroidDataByID(current).neighbours()){
                if(!found.contains(neighbour) && euclideanDist(startCoord, grid.getCentroidCoord(neighbour)) < maxDist){
                    found.add(neighbour);
                    frontier.add(neighbour);
                }
            }
        }
        return found;
    }

}
