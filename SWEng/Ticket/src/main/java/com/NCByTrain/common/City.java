package com.NCByTrain.common;

import java.util.List;

// City - represents a city with name and coordinates
public record City(String name, int x, int y) {

  // Compact constructor with validation using BasicConstants.isValidCity()
  public City {
    if (!BasicConstants.isValidCityName(name)) {
      throw new IllegalArgumentException("Invalid city name: " + name);
    }
    // validation for coordinates (ensure they're non-negative)
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("City coordinates must be non-negative");
    }

    // validation for coordinates to be within bounds
    if(!BasicConstants.isCoordWithinBounds(x, y)) {
      throw new IllegalArgumentException("City coordinates must be within map bounds");
    }
  }

  public String name(){
    return this.name;
  }

  public Coordinate getCoordinate(){
    return new Coordinate(this.x, this.y);
  }

  public List<Integer> getCoordinatePositionList(){
    return List.of(this.x, this.y);
  }

  public boolean isValidCity(){
    return BasicConstants.isValidCityName(this.name) && BasicConstants.isCoordWithinBounds(this.x, this.y);
  }

  // Factory method for easier creation - equivalent to construction pattern
  public static City at(String name, int x, int y) {
    return new City(name, x, y);
  }

    @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof City)) return false;
    City other = (City) obj;
    return name.equals(other.name) && x == other.x && y == other.y;
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(name, x, y);
  }

  @Override
  public String toString() {
    return String.format("City[%s at (%d, %d)]", name, x, y);
  }
}
