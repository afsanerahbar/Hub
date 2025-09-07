package com.NCByTrain.common;

import java.util.List;

// Factory class with multiple constructions
public class ConnectionFactory {

  // Case 1: Four individual parameters with automatic quoting of first three
  // Note: Java doesn't have symbol quoting, so we use strings directly
  public static Connection connection(City from, City to, String colorStr, int segNum) {
    Color color = Color.fromString(colorStr);
    return Connection.createCheckedConnection(from, to, color, segNum);
  }

  // Case 2: Four individual parameters with automatic quoting of first three
  // Note: Java doesn't have symbol quoting, so we use strings directly
  public static Connection connection(City from, City to, Color color, int segNum) {
    return Connection.createCheckedConnection(from, to, color, segNum);
  }

  // Case 3: From-to pair and two additional parameters
  public static Connection connection(List<City> fromTo, Object colorParam, int segNum) {
    if (fromTo.size() != 2) {
      throw new IllegalArgumentException("fromTo list must contain exactly 2 elements");
    }
    City from = fromTo.get(0);
    City to = fromTo.get(1);

    Color color;
    if (colorParam instanceof Color) {
      color = (Color) colorParam;
    } else if (colorParam instanceof String) {
      color = Color.fromString((String) colorParam);
    } else {
      throw new IllegalArgumentException("color parameter must be Color or String");
    }

    return Connection.createCheckedConnection(from, to, color, segNum);
  }
}
