package com.NCByTrain.common;

public enum Color {
  RED, BLUE, GREEN, WHITE;

  public static Color fromString(String colorStr) {
    try {
      return Color.valueOf(colorStr.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid color: " + colorStr);
    }
  }

  public static boolean isValidColor(String colorStr) {
    try {
      valueOf(colorStr.toUpperCase());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  @Override
  public String toString() {
    return name().toLowerCase();
  }

  //method that convert this color to awt color
  public java.awt.Color toAwtColor() {
    return switch (this) {
      case RED -> java.awt.Color.RED;
      case BLUE -> java.awt.Color.BLUE;
      case GREEN -> java.awt.Color.GREEN;
      case WHITE -> java.awt.Color.WHITE;
    };
  }
}
