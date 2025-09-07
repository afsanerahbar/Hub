package com.NCByTrain.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CityTest {

  // Correct City definitions
  City city1 = new City("Durham", 50 , 100);
  City city2 = City.at("Durham", 50 , 100);
  City city3 = City.at("Chapel Hill", 40 , 200);

  City city11 = new City("Durha", 50 , 100);
  City city12 = new City("Durham", 55 , 100);
  City city13 = new City("Durham", 50 , 10);


  @Test
  void isValidCity(){
    assertTrue(city1.isValidCity());
  }

  @Test
  void at(){
    assertEquals(city1, City.at("Durham", 50 , 100));
  }

  @Test
  void City(){
    Exception exception0 = assertThrows(IllegalArgumentException.class, () -> {
      City.at("D!", 10, 100);
    });

    String expectedMessage = "Invalid city name: D!";
    String actualMessage = exception0.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

    Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
      City.at("Durham", -1, 100);
    });

    expectedMessage = "City coordinates must be non-negative";
    actualMessage = exception1.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

    Exception exception11 = assertThrows(IllegalArgumentException.class, () -> {
      City.at("Durham", 10, -10);
    });

    expectedMessage = "City coordinates must be non-negative";
    actualMessage = exception11.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

    Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
      City.at("Durham", 10, 10000);
    });

    expectedMessage = "City coordinates must be within map bounds";
    actualMessage = exception2.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void equal(){
    assertTrue(city1.equals(city2));
    assertTrue(city1.equals(city1));

    assertFalse(city1.equals(city3));
    assertFalse(city1.equals(""));
    assertFalse(city1.equals(city11));
    assertFalse(city1.equals(city12));
    assertFalse(city1.equals(city13));

  }

  @Test
  void hashCodeTest(){
    assertEquals(city1.hashCode(), city2.hashCode());

    assertNotEquals(city1.hashCode(), city3.hashCode());
  }

  @Test
  void toStringTest(){
    assertEquals("City[Durham at (50, 100)]", city1.toString());
    assertEquals("City[Durham at (50, 100)]", city2.toString());
    assertEquals("City[Chapel Hill at (40, 200)]", city3.toString());


    assertNotEquals("City[Durham at (50, 100)]", city3.toString());
    assertNotEquals("City[Chapel Hill at (40, 200)]", city2.toString());
  }

}