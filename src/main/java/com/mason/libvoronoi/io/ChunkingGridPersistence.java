package com.mason.libvoronoi.io;

import com.mason.libvoronoi.algorithms.components.CentroidData;
import com.mason.libvoronoi.algorithms.components.ChunkingGrid;

import java.io.*;

public class ChunkingGridPersistence{

    public static void writeToFile(File file, ChunkingGrid<?> grid) throws IOException{
        try(DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(file)))){
            grid.writeToDataStream(out);
        }
    }

    public static <T extends CentroidData> ChunkingGrid<T> readFromFile(File file, CentroidDataReader<T> centroidReader) throws IOException{
        try(DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream(file)))){
            return ChunkingGrid.readFromDataStream(in, centroidReader);
        }
    }

}
