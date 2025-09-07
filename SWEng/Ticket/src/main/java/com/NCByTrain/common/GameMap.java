//package com.NCByTrain.common;
//
//import java.util.List;
//import java.util.ArrayList;
//import java.util.Collections;
//
//
//public class GameMap {
//  private final int width;
//  private final int height;
//  private final List<City> cities;
//  private final List<Connection> connections;
//
//  public GameMap(int width, int height, List<City> cities, List<Connection> connections) {
//    this.width = width;
//    this.height = height;
//    this.cities = new ArrayList<>(cities);
//    this.connections = new ArrayList<>(connections);
//  }
//
//  public int getWidth() { return width; }
//  public int getHeight() { return height; }
//  public List<City> getCities() { return Collections.unmodifiableList(cities); }
//  public List<Connection> getConnections() { return Collections.unmodifiableList(connections); }
//
//  public City findCity(String name) {
//    return cities.stream()
//        .filter(city -> city.name().equals(name))
//        .findFirst()
//        .orElse(null);
//  }
//}


package com.NCByTrain.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// a data representation of the railroad map
public class GameMap {

  // the first four fields are essential
  private final int width;
  private final int height;
  private final List<City> cityPlaces;
  private final Map<City, List<Connection>> graph; // invariant city = connection.from

  // this one is for background visualization
  //private final String backgroundImageUrl;

  public GameMap(int width, int height, List<City> cities, List<Connection> connections) {
    this(width, height, cities, connections, null);
  }

//  public GameMap(int width, int height, List<City> cities, HashMap<City, List<City>> graph) {
//    this(width, height, cities, graph, null);
//  }

//  public GameMap(int width, int height, List<City> cities, HashMap<City, List<City>> graph, String backgroundImageUrl) {
//    if (!BasicConstants.isValidWidth(width) || !BasicConstants.isValidHeight(height)) {
//      throw new IllegalArgumentException("Invalid map dimensions: %dx%d".formatted(width, height));
//    }
//
//    this.width = width;
//    this.height = height;
//    this.cityPlaces = cities.stream()
//        .map(city -> new City(city.name(), city.coordinate().get(0), city.coordinate().get(1)))
//        .toList();
//    this.graph = graph;
//  }

  public GameMap(int width, int height, List<City> cities, List<Connection> connections,
      String backgroundImageUrl) {
    if (!BasicConstants.isValidWidth(width) || !BasicConstants.isValidHeight(height)) {
      throw new IllegalArgumentException("Invalid map dimensions: %dx%d".formatted(width, height));
    }

    this.width = width;
    this.height = height;
    this.cityPlaces = cities.stream()
        .map(city -> new City(city.name(), city.getCoordinatePositionList().get(0),
            city.getCoordinatePositionList().get(1)))
        .toList();
    this.graph = this.buildGraph(connections);
  }

  // Create a Map of City to List of neighbors from the given list of Connections
  private Map<City, List<Connection>> buildGraph(List<Connection> connections) {
    Map<City, List<Connection>> graph = new HashMap<>();
    // Add one direction
    this.addOneDirection(graph, connections);

    // Add reverse direction
    var flippedConnections = connections.stream().map(Connection::flip).toList();
    addOneDirection(graph, flippedConnections);

    // Make lists immutable
    graph.replaceAll((k, v) -> List.copyOf(v));
    return Map.copyOf(graph);
  }

//  // ConnectionEdge record for GameMap internal representation
//  public record ConnectionEdge(City toCity, Color color, int segmentLength) {
//
//    public ConnectionEdge {
//      if (!BasicConstants.isValidCityName(toCity.name())) {
//        throw new IllegalArgumentException("Invalid city name: " + toCity);
//      }
//      if (!BasicConstants.isValidColor(color.toString())) {
//        throw new IllegalArgumentException("Invalid color: " + color);
//      }
//      if (!BasicConstants.isValidSegmentLength(segmentLength)) {
//        throw new IllegalArgumentException("Invalid segment length: " + segmentLength);
//      }
//    }
//
//    // Convert to full Connection given the from city
//    public Connection toConnection(City fromCity) {
//      return new Connection(fromCity, toCity, color, segmentLength);
//    }
//  }

  // Create a Map of City to List of neighbors from the given list of Connections
  // Left to right
  private Map<City, List<Connection>> addOneDirection(Map<City, List<Connection>> graph,
      List<Connection> connections) {
    HashMap<City, List<Connection>> directedGraph = new HashMap<>(graph);

    // Group connections by fromCity
    var grouped = connections.stream()
        .collect(Collectors.groupingBy(Connection::from));

    for (Map.Entry<City, List<Connection>> entry : grouped.entrySet()) {
      City fromCity = entry.getKey();
      List<Connection> cityConnections = entry.getValue();
//
//      // Convert Connections to ConnectionEdge objects
//      List<Connection> edges = cityConnections.stream()
//          .map(conn -> new Connection(conn.from(), conn.to(), conn.color(), conn.segmentLength()))
//          .collect(Collectors.toList());

      // Update the graph: append to existing list or create new list
      directedGraph.merge(fromCity, cityConnections, (existing, newList) -> {
        List<Connection> combined = new ArrayList<>(existing);
        combined.addAll(newList);
        return combined;
      });
    }
    return directedGraph;
  }

    public City findCity(String name) {
    return this.cityPlaces.stream()
        .filter(city -> city.name().equals(name))
        .findFirst()
        .orElse(null);
  }
}
