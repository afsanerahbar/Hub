package com.NCByTrain.common;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class DestinationTest {

  City durCity = new City("Durham", 50 , 100);
  City chCity = City.at("Chapel Hill", 40 , 200);
  City ralCity = new City("Raleigh", 20, 100);
  City greenCity = new City("Greensboro", 70, 250);

  //Record creation using the factory method
  Destination d1 = Destination.of(durCity, ralCity);
  Destination d2 = Destination.of(ralCity, chCity);

  Destination d11 = Destination.of(ralCity, durCity);

  //Record creation using the constructor
  Destination d3 = new Destination(chCity, greenCity);


  @Test
  void destinationTest(){
    // Case 1: Throws exception
    Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
      Destination.of(durCity,durCity);
    });

    String expectedMessage = "Both cities must be valid";
    String actualMessage = exception1.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

    // Case 2: Does not throw exception
    assertDoesNotThrow(() -> {
      Destination.of(durCity,chCity);
    });

  }


  @Test
  void connects() {
    assertTrue(d1.connects(durCity, ralCity));
    assertTrue(d1.connects(ralCity, durCity));

    assertTrue(d2.connects(ralCity,chCity));
    assertTrue(d2.connects(chCity, ralCity));
    assertTrue(d3.connects(chCity, greenCity));
    assertTrue(d3.connects(greenCity, chCity ));

    assertFalse(d1.connects(ralCity, greenCity));
    assertFalse(d2.connects(ralCity, durCity));
  }

  @Test
  void getCities() {
    assertEquals(List.of("Durham", "Raleigh"), d1.getCities());
    assertEquals(List.of("Chapel Hill","Raleigh"), d2.getCities());
  }

  @Test
  void isOrdered(){
    assertTrue(d1.isOrdered());
    assertTrue(d2.isOrdered());
    assertTrue(d3.isOrdered());
  }



  @Test
  void testEquals() {
    assertEquals(d1,d11);

    assertNotEquals(d2, d1);
  }

  @Test
  void hashCodeTest(){
    assertEquals(d1.hashCode(), d11.hashCode());
    assertNotEquals(d1.hashCode(), d2.hashCode());
  }

  @Test
  void toStringTest() {

    assertEquals("Destination[City[Durham at (50, 100)] -> City[Raleigh at (20, 100)]]", d1.toString());

    assertNotEquals("Destination[Durham -> Raleigh]", d1.toString());
  }
}