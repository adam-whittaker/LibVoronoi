package main.com.mason.libvoronoi.structures.lowMemory;

import main.com.mason.libvoronoi.structures.Size;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CardinalIndexNeighbours implements Iterable<Integer>{


    private final List<Integer> neighbours;


    public CardinalIndexNeighbours(Integer index, Size size){
        this.neighbours = new ArrayList<>(4);
        populateNeighbours(index, size);
    }

    private void populateNeighbours(Integer index, Size size){
        int indexCapacity = size.height() * size.width();
        tryAdd(index-size.width(), indexCapacity);
        tryAdd(index+1, indexCapacity);
        tryAdd(index+size.width(), indexCapacity);
        tryAdd(index-1, indexCapacity);
    }

    private void tryAdd(int index, int indexCapacity){
        if(index >= 0 && index < indexCapacity){
            neighbours.add(index);
        }
    }


    @Override
    public Iterator<Integer> iterator(){
        return neighbours.iterator();
    }

}
