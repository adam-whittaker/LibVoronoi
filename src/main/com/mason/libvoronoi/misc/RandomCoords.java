package main.com.mason.libvoronoi.misc;

import main.com.mason.libvoronoi.algorithms.voronoi.RandomCoordGenerator;
import main.com.mason.libvoronoi.structures.Coord;
import main.com.mason.libvoronoi.structures.Size;
import main.com.mason.libvoronoi.structures.interfaces.RectQuery;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomCoords implements RandomCoordGenerator{


    private final Random R;


    public RandomCoords(Random random){
        R = random;
    }


    @Override
    public Set<Coord> generateRandomDistinctCoords(Size bounds, int num){
        Set<Coord> coords = new HashSet<>();
        while(coords.size() < num){
            coords.add(generateRandomCoord(bounds));
        }
        return coords;
    }

    public Coord generateRandomCoord(Size bounds){
        int x = R.nextInt(bounds.width());
        int y = R.nextInt(bounds.height());
        return new Coord(x, y);
    }

    public Coord generateRandomCoordWithinClip(RectQuery clip){
        int x = clip.x() + R.nextInt(clip.width());
        int y = clip.y() + R.nextInt(clip.height());
        return new Coord(x, y);
    }

}
