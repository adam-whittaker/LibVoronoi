package main.com.mason.libvoronoi.io;

import main.com.mason.libvoronoi.algorithms.components.CentroidData;
import main.com.mason.libvoronoi.algorithms.components.ChunkingGrid;

import java.io.*;

public class ChunkingGridReader{


    public void writeToFile(String filePath, ChunkingGrid<?> grid) throws IOException{
        try(DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(filePath)))){
            grid.writeToDataStream(out);
        }
    }

    public static <T extends CentroidData> ChunkingGrid<T> readFromFile(String filePath, CentroidDataReader<T> centroidReader) throws IOException{
        try(DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream(filePath)))){
            return ChunkingGrid.readFromDataStream(in, centroidReader);
        }
    }

}
