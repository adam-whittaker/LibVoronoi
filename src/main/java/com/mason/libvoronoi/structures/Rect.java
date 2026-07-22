package com.mason.libvoronoi.structures;

import com.mason.libvoronoi.structures.interfaces.RectQuery;

public record Rect(int x, int y, int width, int height) implements RectQuery{


    //Factories
    public static Rect buildRect(Coord topLeft, Coord bottomRight){
        return buildRect(topLeft, bottomRight.x() - topLeft.x() + 1,  bottomRight.y() - topLeft.y() + 1);
    }

    public static Rect buildRect(Coord topLeft, int width, int height){
        return new Rect(topLeft.x(), topLeft.y(), width, height);
    }

    public static Rect buildRect(Coord topLeft, Size size){
        return new Rect(topLeft.x(), topLeft.y(), size.width(), size.height());
    }


    //Queries
    public boolean withinBounds(Coord c){
        return c.x() >= x && c.y() >= y && c.x() < x+width && c.y() < y+height;
    }

    public boolean withinInterior(Coord c){
        return c.x() > x && c.y() > y && c.x() < x+width-1 && c.y() < y+height-1;
    }

    @Override
    public Coord getCoord(){
        return topLeft();
    }

    @Override
    public Size getSize(){
        return new Size(width, height);
    }


    //Coordinates
    public Coord centre(){
        return new Coord(x + width/2, y + height/2);
    }

    public Coord topLeft(){
        return new Coord(x, y);
    }

    public Coord topRight(){
        return new Coord(x+width-1, y);
    }

    public Coord bottomLeft(){
        return new Coord(x, y+height-1);
    }

    public Coord bottomRight(){
        return new Coord(x+width-1, y+height-1);
    }

    public Coord topMid(){
        return new Coord(x+width/2, y);
    }

    public Coord bottomMid(){
        return new Coord(x+width/2, y+height-1);
    }

    public Coord leftMid(){
        return new Coord(x, y+height/2);
    }

    public Coord rightMid(){
        return new Coord(x+width-1, y+height/2);
    }

}
