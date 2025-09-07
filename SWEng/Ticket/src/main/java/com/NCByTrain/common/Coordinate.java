package com.NCByTrain.common;

// Coordinate record
public record Coordinate(int x, int y) {

  public Coordinate {
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("Coordinates must be non-negative: (%d, %d)".formatted(x, y));
    }
  }

  // Calculate distance to another coordinate
  public double distanceTo(Coordinate other) {
    int dx = this.x - other.x;
    int dy = this.y - other.y;
    return Math.sqrt(dx * dx + dy * dy);
  }

  // Check if within bounds
  public boolean isWithinBounds(int width, int height) {
    return x >= 0 && x <= width && y >= 0 && y <= height;
  }
}
