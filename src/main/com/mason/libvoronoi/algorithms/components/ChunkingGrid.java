package main.com.mason.libvoronoi.algorithms.components;

import main.com.mason.libvoronoi.algorithms.CentroidNeighbourhoodSearch;
import main.com.mason.libvoronoi.io.CentroidDataReader;
import main.com.mason.libvoronoi.structures.Coord;
import main.com.mason.libvoronoi.structures.Rect;
import main.com.mason.libvoronoi.structures.Size;
import main.com.mason.libvoronoi.structures.interfaces.RectQuery;
import main.com.mason.libvoronoi.structures.lowMemory.CardinalIndexNeighbours;
import main.com.mason.libvoronoi.structures.lowMemory.ShortGrid;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static main.com.mason.libvoronoi.algorithms.components.CentroidIDMap.CENTROID_UNSET;

public class ChunkingGrid<T extends CentroidData>{


    private static final short DIST_UNSET = -1;

    private final ShortGrid centroidIDGrid;
    private final ShortGrid distToCentroidGrid;
    private final CentroidIDMap<T> centroidDataMap;
    private final Size size;
    private short maxDistToCentroid = -1;


    public ChunkingGrid(Size size){
        this.size = size;
        centroidIDGrid = new ShortGrid(size, CENTROID_UNSET);
        distToCentroidGrid = new ShortGrid(size, DIST_UNSET);
        centroidDataMap = new CentroidIDMap<>();
    }

    private ChunkingGrid(Size size, short maxDistToCentroid, ShortGrid centroidIDGrid, ShortGrid distToCentroidGrid, CentroidIDMap<T> centroidDataMap){
        this.size = size;
        this.maxDistToCentroid = maxDistToCentroid;
        this.centroidIDGrid = centroidIDGrid;
        this.distToCentroidGrid = distToCentroidGrid;
        this.centroidDataMap = centroidDataMap;
    }


    public Size size(){
        return size;
    }

    public Iterable<Short> getAllCentroidIDs(){
        return centroidDataMap.getCentroidIDs();
    }

    public short centroidID(Integer pointIdx){
        return centroidIDGrid.getByIndex(pointIdx);
    }

    public short distanceToCentroid(Integer pointIdx){
        return distToCentroidGrid.getByIndex(pointIdx);
    }

    public T getCentroidDataByIndex(Integer pointIdx){
        if(!hasCentroid(pointIdx)){
            throw new IllegalStateException("Point does not have centroid!");
        }
        short centroidID = centroidIDGrid.getByIndex(pointIdx);
        return centroidDataMap.getCentroidData(centroidID);
    }

    public T getCentroidDataByID(short centroidID){
        return centroidDataMap.getCentroidData(centroidID);
    }

    public boolean hasCentroid(Integer pointIdx){
        return centroidIDGrid.getByIndex(pointIdx) != CENTROID_UNSET;
    }

    public boolean isCentroid(Integer pointIdx){
        if(!hasCentroid(pointIdx)){
            return false;
        }
        short centroidID = centroidIDGrid.getByIndex(pointIdx);
        return pointIndexMatchesCentroidCoord(pointIdx, centroidID);
    }

    private boolean pointIndexMatchesCentroidCoord(Integer pointIdx, short centroidID){
        Coord centroidCoord = centroidDataMap.getCentroidCoord(centroidID);
        int centroidIndex = centroidIDGrid.asIndex(centroidCoord);
        return centroidIndex == pointIdx;
    }

    public void setCentroid(Coord pointCoord, short centroidID, short distToCentroid){
        int pointIndex = centroidIDGrid.asIndex(pointCoord);
        setCentroidByIndex(pointIndex, centroidID, distToCentroid);
    }

    public void setCentroidByIndex(int pointIdx, short centroidID, short distToCentroid){
        centroidIDGrid.setByIndex(pointIdx, centroidID);
        distToCentroidGrid.setByIndex(pointIdx, distToCentroid);
    }

    public void unsetCentroidByIndex(int pointIdx){
        setCentroidByIndex(pointIdx, CENTROID_UNSET, DIST_UNSET);
    }

    public Coord getCentroidCoord(short centroidID){
        return centroidDataMap.getCentroidCoord(centroidID);
    }

    public void createCentroid(T data){
        short centroidID = centroidDataMap.createCentroidAndReturnID(data);
        Coord centroidCoord = data.getCoord();
        centroidIDGrid.set(centroidCoord.x(), centroidCoord.y(), centroidID);
    }

    public void moveCentroid(short centroidId, Coord newCoord){
        centroidDataMap.moveCentroid(centroidId, newCoord);
    }

    public Coord asCoord(Integer targetIdx){
        return new Coord(targetIdx % size.width(), targetIdx / size.width());
    }

    public Integer asIndex(Coord coord){
        return coord.y()*size.width() + coord.x();
    }

    public Iterable<Integer> cardinalNeighbours(Integer pointIdx){
        return new CardinalIndexNeighbours(pointIdx, size);
    }

    public Iterable<Integer> pointIndices(){
        return () -> new Iterator<>(){

            final int maxIndex = size.width() * size.height() - 1;
            int current = -1;

            @Override
            public boolean hasNext(){
                return current < maxIndex;
            }

            @Override
            public Integer next(){
                current++;
                return current;
            }
        };
    }

    public Iterable<Integer> indicesInClip(RectQuery clip){
        return indicesInSafeClip(constructSafeClip(clip));
    }

    private RectQuery constructSafeClip(RectQuery clip){
        int x = max(clip.x(), 0);
        int y = max(clip.y(), 0);
        int width = min(clip.width(), size.width()-x-1);
        int height = min(clip.height(), size.height()-y-1);
        return new Rect(x, y, width, height);
    }

    private Iterable<Integer> indicesInSafeClip(RectQuery clip){
        return () -> new Iterator<>(){

            final int maxIndex = asIndex(new Coord(clip.x()+clip.width()-1, clip.y()+clip.height()-1));
            final int boundX = clip.x() + clip.width();
            int current = asIndex(clip.getCoord())-1;

            @Override
            public boolean hasNext(){
                return current < maxIndex;
            }

            @Override
            public Integer next(){
                current++;
                if(boundX <= current % size.width()){
                    current += size.width() - clip.width();
                }
                return current;
            }
        };
    }

    public void updateMaxDistToCentroid(){
        maxDistToCentroid = distToCentroidGrid.max();
    }

    public RectQuery constructBoundingRectangle(Coord centroidCoord){
        int x = max(centroidCoord.x() - maxDistToCentroid, 0);
        int y = max(centroidCoord.y() - maxDistToCentroid, 0);
        int width = min(2*maxDistToCentroid+1, size.width()-x-1);
        int height = min(2*maxDistToCentroid+1, size.height()-y-1);
        return new Rect(x, y, width, height);
    }

    public Iterable<Short> centroidNeighbourhood(Short centroidID, int searchDepth){
        return new CentroidNeighbourhoodSearch<>(this).centroidNeighbourhood(centroidID, searchDepth);
    }


    public void writeToDataStream(DataOutputStream out) throws IOException{
        out.writeInt(size.width());
        out.writeInt(size.height());
        out.writeShort(maxDistToCentroid);

        centroidIDGrid.writeToDataStream(out);
        distToCentroidGrid.writeToDataStream(out);
        centroidDataMap.writeToDataStream(out);
    }

    public static <T extends CentroidData> ChunkingGrid<T> readFromDataStream(DataInputStream in, CentroidDataReader<T> centroidReader) throws IOException{
        int width = in.readInt();
        int height = in.readInt();
        Size size = new Size(width, height);
        short maxDistToCentroid = in.readShort();

        ShortGrid centroidIDGrid = ShortGrid.readFromDataStream(in);
        ShortGrid distToCentroidGrid = ShortGrid.readFromDataStream(in);
        CentroidIDMap<T> centroidDataMap = CentroidIDMap.readFromDataStream(in, centroidReader);
        return new ChunkingGrid<>(size, maxDistToCentroid, centroidIDGrid, distToCentroidGrid, centroidDataMap);
    }

}
