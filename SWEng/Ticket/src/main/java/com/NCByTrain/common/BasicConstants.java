package com.NCByTrain.common;

// Basic Constants and functions for the game
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.NCByTrain.common.Color;

public class BasicConstants {
  // Game structure constants
  public static final int CITY_COUNT = 20;           // CITY#
  public static final int CONNECTION_COUNT = 40;     // CONN#

  // Game dimensions
  public static final int MIN_WIDTH = 10;
  public static final int MAX_WIDTH = 800;
  public static final int MIN_HEIGHT = 10;
  public static final int MAX_HEIGHT = 800;

  // Game parameters
  public static final int RAILS_PER = 45;    // rails a player receives during set-up
  public static final int CARD0_PER = 4;     // colored cards a player receives during set-up
  public static final int CARDS_PER = 2;     // colored cards a player receives during a turn
  public static final int DESTS_PER = 2;     // destinations a player must pick during set-up
  public static final int PICKS_PER = 5;     // number of destination cards offered during set-up
  public static final int RAILS_MIN = 3;     // minimum rails a player must own to continue

  // Scoring
  public static final int POINTS_PER = 10;   // points for completing a destination
  public static final int LONG_PATH = 20;    // bonus for longest path

  // Player constraints
  public static final int MIN_PLAYER_PER_GAME = 2;
  public static final int MAX_PLAYER_PER_GAME = 8;
  public static final int CARDS_PER_GAME = 250;
  public static final int MAX_PLAYER_NAME = 50;

  // City constraints
  public static final int CITY_LENGTH = 25;
  public static final String CITY_NAME_PATTERN = "[a-zA-Z0-9\\ \\.\\,]+";
  public static final String PLAYER_NAME_PATTERN = "[a-zA-Z]";

  // Available colors and segment lengths (matching Racket vectors)
  //public static final String[] COLORS = {"red", "blue", "green", "white"};
  public static final List<Color> COLORS = List.of(
      Color.RED, Color.BLUE, Color.GREEN, Color.WHITE
  );
  public static final int[] SEGMENT_LENGTHS = {3, 4, 5};    // SEG#

  // Compiled patterns for validation
  private static final Pattern CITY_PATTERN = Pattern.compile(CITY_NAME_PATTERN);
  private static final Pattern PLAYER_PATTERN = Pattern.compile(PLAYER_NAME_PATTERN);

  // Color validation - handles case-insensitive matching like Racket
  public static boolean isValidColor(String color) {
    if (color == null) return false;
    String lowerColor = color.toLowerCase();
    for (Color c : COLORS) {
      if (c.toString().equals(lowerColor)) return true;
    }
    return false;
  }

  // Segment length validation
  public static boolean isValidSegmentLength(int length) {
    for (int len : SEGMENT_LENGTHS) {
      if (len == length) return true;
    }
    return false;
  }

  // Width validation
  public static boolean isValidWidth(int width) {
    return width > 0 && width >= MIN_WIDTH && width <= MAX_WIDTH;
  }

  // Height validation
  public static boolean isValidHeight(int height) {
    return height > 0 && height >= MIN_HEIGHT && height <= MAX_HEIGHT;
  }

  // Check if coordinate is within map boundaries
  public static boolean isCoordWithinBounds(int x, int y) {
    return BasicConstants.isValidWidth(x) && BasicConstants.isValidHeight(y);
  }

  // City name validation
  public static boolean isValidCityName(String cityName) {
    if (cityName == null) return false;

    // Check length constraints
    int length = cityName.length();
    if (length < 1 || length > CITY_LENGTH) {
      return false;
    }

    // Check pattern match - entire string must match the pattern
    return CITY_PATTERN.matcher(cityName).matches();
  }

  // Player name validation
  public static boolean isValidPlayerName(String playerName) {
    if (playerName == null) return false;

    // Check length constraints
    if (playerName.length() > MAX_PLAYER_NAME) {
      return false;
    }

    // Check that it starts with a letter
    return PLAYER_PATTERN.matcher(playerName.substring(0, 1)).matches();
  }

  // City ordering function - equivalent to list-cities in Racket
  public static List<City> listCities(City start, City end) {
    List<City> ordered = List.of(start,end);
    ordered = ordered.stream()
        .sorted(Comparator.comparing(City::name))
        .collect(Collectors.toList());
    return ordered;
//    if (start.compareTo(end) <= 0) {
//      return Arrays.asList(start, end);
//    } else {
//      return Arrays.asList(end, start);
//    }
  }

  // Get all available colors as a list
  public static List<String> getAllColors() {
    return Arrays.asList(COLORS);
  }

  // Get all available segment lengths as a list
  public static List<Integer> getAllSegmentLengths() {
    return Arrays.asList(3, 4, 5);
  }

  // Convenience method to get a random color
  public static String getRandomColor(Random random) {
    return COLORS[random.nextInt(COLORS.length)];
  }

  // Convenience method to get a random segment length
  public static int getRandomSegmentLength(Random random) {
    return SEGMENT_LENGTHS[random.nextInt(SEGMENT_LENGTHS.length)];
  }
}
