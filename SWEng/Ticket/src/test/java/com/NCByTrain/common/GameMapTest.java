package com.NCByTrain.common;

import org.junit.jupiter.api.Test;
import java.util.List;

class GameMapTest {

  City durCity = new City("Durham", 50 , 100);
  City chCity = City.at("Chapel Hill", 40 , 200);
  City ralCity = new City("Raleigh", 20, 100);
  City greenCity = new City("Greensboro", 70, 250);

  Connection conn1 = ConnectionFactory.connection(chCity, ralCity, Color.RED, 5);
  Connection conn2 = ConnectionFactory.connection(ralCity, durCity,  Color.BLUE, 4);
  Connection conn3 = ConnectionFactory.connection(greenCity, chCity,  Color.GREEN, 5);
  Connection conn4 = ConnectionFactory.connection(ralCity, chCity,  Color.GREEN, 5);
  Connection conn5 = ConnectionFactory.connection(durCity, chCity,  Color.GREEN, 3);

  @Test
  void mapTest(){

    Map m1 = new Map(700, 700, List.of(durCity,chCity, ralCity, greenCity), List.of(conn1,conn2,conn3,conn4,conn5));

//    Exception exception1 = assertThrows(IllegalArgumentException.class, () ->
//    {new Map(1000, 1000, List.of(durCity,chCity, ralCity, greenCity), List.of(conn1,conn2,conn3,conn4,conn5));}
//    );
//
//    String expectedMessageContains = "Invalid map dimensions:";
//    String actualMessage = exception1.getMessage();
//
//    assertTrue(actualMessage.contains(expectedMessageContains));

  }

  void addOneDirection(){
    Map m1 = new Map(700, 700, List.of(durCity,chCity, ralCity, greenCity), List.of(conn1,conn2,conn3,conn4,conn5));
   // m1.addOneDirection();
  }

}