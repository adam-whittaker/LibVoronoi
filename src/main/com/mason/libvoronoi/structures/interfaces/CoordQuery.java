package main.com.mason.libvoronoi.structures.interfaces;

import main.com.mason.libvoronoi.structures.Coord;

public interface CoordQuery{

    Coord getCoord();


    default int x(){
        return getCoord().x();
    }

    default int y(){
        return getCoord().y();
    }

}
