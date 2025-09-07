//package com.NCByTrain.common;
//
//import static org.junit.jupiter.api.Assertions.*;
//import java.util.List;
//
//class ConnectionFactoryTest {
//
//  void connectionTest(){
//    try {
//      // Various construction patterns
//      Color red = Color.RED;
//      Connection exp1 = new Connection("Raleigh", "Charlotte", red, 3);
//      var conn1 = ConnectionFactory.connection("Raleigh", "Charlotte", "red", 3);
//      assertEquals(exp1, conn1);
//
//      var conn2 = ConnectionFactory.connection("Durham", "Greensboro", Color.BLUE, 2);
//      Connection exp2 = new Connection("Durham", "Greensboro", Color.BLUE, 2);
//      assertEquals(exp2, conn2);
//
//
//      // With from-to pair
//      var fromTo = List.of("Chapel Hill", "Winston Salem");
//      var conn3 = ConnectionFactory.connection(fromTo, "green", 4);
//      Connection exp3 = new Connection("Chapel Hill", "Winston Salem", Color.GREEN, 4);
//      assertEquals(exp3, conn3);
//
//    } catch (Exception e) {
//      System.err.println("Error: " + e.getMessage());
//      e.printStackTrace();
//    }
//  }
//}