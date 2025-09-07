package com.NCByTrain.common;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import java.util.List;

class ConnectionTest {

  City durCity = new City("Durham", 50 , 100);
  City chCity = City.at("Chapel Hill", 40 , 200);
  City ralCity = new City("Raleigh", 20, 100);
  City greenCity = new City("Greensboro", 70, 250);

  Connection conn1 = ConnectionFactory.connection(chCity, ralCity, Color.RED, 3);
  Connection conn2 = ConnectionFactory.connection(ralCity, chCity,  Color.RED, 3);

  @Test
  void createCheckedConnection() {
    // Case 1: Throws exception: "from, to, and color cannot be null"
    Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
      Connection.createCheckedConnection(null,chCity, Color.BLUE, 10);
    });

    String expectedMessage = "from, to, and color cannot be null";
    String actualMessage = exception1.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));


    Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
      Connection.createCheckedConnection(chCity,null, Color.BLUE, 10);
    });

    expectedMessage = "from, to, and color cannot be null";
    actualMessage = exception2.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));


    Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
      Connection.createCheckedConnection(chCity,durCity, null, 10);
    });

    expectedMessage = "from, to, and color cannot be null";
    actualMessage = exception3.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void isGoodConnection(){
    assertTrue(conn1.isGoodConnection());
    assertFalse(conn2.isGoodConnection());
  }

  @Test
  void flip() {
    assertEquals(conn1, conn2.flip());
  }

//  @Test
//  void getFromTo() {
//    List<String> exp = List.of("Charlotte", "Raleigh");
//    assertEquals(exp, conn1.getFromTo());
//  }
}