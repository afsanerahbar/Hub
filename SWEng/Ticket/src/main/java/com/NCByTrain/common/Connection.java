package com.NCByTrain.common;

import java.util.List;

// represent individual connections
public record Connection(City from, City to, Color color, int segmentLength) {

  // Contract validation - ensures connection is "good" (from < to lexicographically)
  public Connection {
    if (from == null || to == null || color == null) {
      throw new IllegalArgumentException("from, to, and color cannot be null");
    }
//    if (!isGoodConnection(from, to)) {
//      throw new IllegalArgumentException("Connection must have from < to lexicographically");
//    }
  }

  public Color getColor() { return color; }
  public int getSegments() { return segmentLength; }

  public boolean isGoodConnection() {
    return from.name().compareTo(to.name()) < 0;
  }

  // Factory method for Connection
  public static Connection createCheckedConnection(City from, City to, Color color, int segmentLength) {
    return new Connection(from, to, color, segmentLength);
  }

  // Flip the direction of the Connection
  public Connection flip() {
    return new Connection(to, from, color, segmentLength);
  }

  // Get both cities of a Connection as a list
  public List<City> getFromTo() {
    return List.of(from, to);
  }

}

