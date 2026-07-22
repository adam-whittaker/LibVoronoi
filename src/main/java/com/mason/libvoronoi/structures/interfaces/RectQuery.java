package com.mason.libvoronoi.structures.interfaces;

import com.mason.libvoronoi.structures.Coord;
import com.mason.libvoronoi.structures.Size;

public interface RectQuery extends CoordQuery{

    Size getSize();


    default int width(){
        return getSize().width();
    }

    default int height(){
        return getSize().height();
    }

    default boolean withinBounds(Coord c){
        return c.x() >= x() && c.y() >= y() && c.x() < x()+width() && c.y() < y()+height();
    }

}
