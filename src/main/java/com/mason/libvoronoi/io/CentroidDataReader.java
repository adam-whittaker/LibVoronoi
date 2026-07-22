package com.mason.libvoronoi.io;

import com.mason.libvoronoi.algorithms.components.CentroidData;

import java.io.DataInputStream;
import java.io.IOException;

public interface CentroidDataReader<T extends CentroidData>{


    T readFromDataStream(DataInputStream in) throws IOException;

}
