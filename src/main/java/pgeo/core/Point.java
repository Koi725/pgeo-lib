package pgeo.core;

import java.util.Objects;

public final class Point {

    private final double x;
    private final double y;

    public Point(double x, double y) {
        validateCoordinate(x, "x");
        validateCoordinate(y, "y");
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distanceTo(Point other) {
        Objects.requireNonNull(other, "Point cannot be null");
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public boolean isCoincident(Point other) {
        Objects.requireNonNull(other, "Point cannot be null");
        return Double.compare(this.x, other.x) == 0 
            && Double.compare(this.y, other.y) == 0;
    }

    public static Point fromArray(double[] coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates array cannot be null");
        }
        if (coordinates.length != 2) {
            throw new IllegalArgumentException("Coordinates array must have exactly 2 elements");
        }
        return new Point(coordinates[0], coordinates[1]);
    }

    public double[] toArray() {
        return new double[]{x, y};
    }

    private void validateCoordinate(double value, String name) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(name + " coordinate cannot be NaN");
        }
        if (Double.isInfinite(value)) {
            throw new IllegalArgumentException(name + " coordinate cannot be infinite");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return Double.compare(point.x, x) == 0 
            && Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("Point(%.4f, %.4f)", x, y);
    }
}