package com.NCByTrain.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.lang.RuntimeException;
import java.util.ListIterator;
import java.util.stream.Collectors;

// Destination card specifies two cities;
// there is guaranteed to be a path between them
public record Destination(City fromCity, City toCity) {

  // Destination constructor with validation and ordering
  public Destination{
    // Ensure this Destination's cities are valid
    if (!isValidDestination(fromCity,toCity)) {
      throw new IllegalArgumentException("Both cities must be valid");
    }

    // Enforce ordering contract: symbol<?(city1, city2)
    List<City> ordered = this.orderingHelper(fromCity,toCity);
    fromCity= ordered.get(0);
    toCity = ordered.get(1);
  }

  private List<City> orderingHelper(City fromCity, City toCity){
    List<City> orderedCities = List.of(fromCity,toCity);
    orderedCities = orderedCities.stream()
        .sorted(Comparator.comparing(City::name))
        .collect(Collectors.toList());
    return orderedCities;
  }

  // Alternative factory method for creating from unordered cities
  public static Destination of(City city1, City city2) {
    return new Destination(city1, city2);
  }

  // Contract validation method - equivalent to destination? predicate
  public static boolean isValidDestination(City city1, City city2) {
    return !city1.equals(city2);
  }

  // Check if this destination connects the given cities (order-independent)
  public boolean connects(City city1, City city2) {
    var ordered = BasicConstants.listCities(city1, city2);
    return fromCity.equals(ordered.get(0)) && toCity.equals(ordered.get(1));
  }


  //Get name of cities on this destination as a list (ordered)
  public List<String> getCities() {
    return List.of(fromCity.name(), toCity.name());
  }

  // Verify the ordering contract is maintained
  public boolean isOrdered() {
    return fromCity.name().compareTo(toCity.name()) <= 0;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Destination)) return false;
    Destination other = (Destination) obj;
    return fromCity.equals(other.fromCity) && toCity.equals(other.toCity);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(fromCity, toCity);
  }

  @Override
  public String toString() {
    return String.format("Destination[%s -> %s]", fromCity, toCity);
  }
}