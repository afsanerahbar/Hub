package com.NCByTrain.common;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import java.lang.String;

class BasicConstantsTest {

  @Test
  void listCities() {
    City durCity = new City("Durham", 50 , 100);
    City chCity = City.at("Chapel Hill", 40 , 200);
    City ralCity = new City("Raleigh", 20, 100);
    City greenCity = new City("Greensboro", 70, 250);

    BasicConstants bc = new BasicConstants();

    assertEquals(List.of(durCity, ralCity), bc.listCities(durCity, ralCity));
    assertEquals(List.of(durCity, ralCity), bc.listCities(ralCity, durCity));
    assertEquals(List.of(chCity, ralCity), bc.listCities(ralCity, chCity));
    assertEquals(List.of(chCity, ralCity), bc.listCities(chCity, ralCity));

    assertEquals(List.of(chCity, chCity), bc.listCities(chCity, chCity));

  }

  @Test
  void isValidCityName(){
    assertTrue(BasicConstants.isValidCityName("A"));
    assertTrue(BasicConstants.isValidCityName("a"));
    assertTrue(BasicConstants.isValidCityName("aA0"));
    assertTrue(BasicConstants.isValidCityName("0"));
    assertTrue(BasicConstants.isValidCityName(". a"));

    assertFalse(BasicConstants.isValidCityName(""));
    assertFalse(BasicConstants.isValidCityName("dhhhhdaskdhahkjdakhjdkuahduahudhuayudyuidyuaiyuidyaugduauigiua"));

  }
}