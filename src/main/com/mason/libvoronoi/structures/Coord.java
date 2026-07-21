package main.com.mason.libvoronoi.structures;

public record Coord(int x, int y){


    public static Coord midpoint(Coord a, Coord b){
        return new Coord((a.x() + b.x())/2, (a.y() + b.y())/2);
    }


    public Coord clampToRect(Rect rect){
        Coord clamped = clampHorizontally(rect);
        clamped = clamped.clampVertically(rect);
        return clamped;
    }

    private Coord clampHorizontally(Rect rect){
        if(x < rect.x()){
            return new Coord(rect.x(), y);
        }
        if(x > rect.x() + rect.width()){
            return new Coord(rect.x() + rect.width(), y);
        }
        return this;
    }

    private Coord clampVertically(Rect rect){
        if(y < rect.y()){
            return new Coord(x, rect.y());
        }
        if(y > rect.y() + rect.height()){
            return new Coord(x, rect.y() + rect.height());
        }
        return this;
    }

}
