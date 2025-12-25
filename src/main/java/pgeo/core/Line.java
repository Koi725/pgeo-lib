package pgeo.core;

import java.util.Objects;

public final class Line {

    private final Point pointA;
    private final Point pointB;

    public Line(Point pointA, Point pointB) {
        Objects.requireNonNull(pointA, "Point A cannot be null");
        Objects.requireNonNull(pointB, "Point B cannot be null");
        validateDistinctPoints(pointA, pointB);
        this.pointA = pointA;
        this.pointB = pointB;
    }

    public Point getPointA() {
        return pointA;
    }

    public Point getPointB() {
        return pointB;
    }

    public double getSlope() {
        double dx = pointB.getX() - pointA.getX();
        double dy = pointB.getY() - pointA.getY();

        if (Double.compare(dx, 0.0) == 0) {
            return Double.POSITIVE_INFINITY;
        }

        return dy / dx;
    }

    public boolean isVertical() {
        return Double.compare(pointA.getX(), pointB.getX()) == 0;
    }

    public boolean isHorizontal() {
        return Double.compare(pointA.getY(), pointB.getY()) == 0;
    }

    public int positionOfPoint(Point point) {
        Objects.requireNonNull(point, "Point cannot be null");

        double signedArea = Triangle.calculateSignedAreaFromPoints(pointA, pointB, point);

        if (signedArea > 0) {
            return 1;
        } else if (signedArea < 0) {
            return -1;
        }
        return 0;
    }

    public boolean isPointAbove(Point point) {
        return positionOfPoint(point) > 0;
    }

    public boolean isPointBelow(Point point) {
        return positionOfPoint(point) < 0;
    }

    public boolean isPointOnLine(Point point) {
        return positionOfPoint(point) == 0;
    }

    public boolean isParallelTo(Line other) {
        Objects.requireNonNull(other, "Line cannot be null");

        double dx1 = this.pointB.getX() - this.pointA.getX();
        double dy1 = this.pointB.getY() - this.pointA.getY();
        double dx2 = other.pointB.getX() - other.pointA.getX();
        double dy2 = other.pointB.getY() - other.pointA.getY();

        double crossProduct = (dx1 * dy2) - (dy1 * dx2);

        return Double.compare(crossProduct, 0.0) == 0;
    }

    public static Line fromArray(double[][] coordinates) {
        validateCoordinatesArray(coordinates);
        Point a = Point.fromArray(coordinates[0]);
        Point b = Point.fromArray(coordinates[1]);
        return new Line(a, b);
    }

    public double[][] toArray() {
        return new double[][]{
            pointA.toArray(),
            pointB.toArray()
        };
    }

    private void validateDistinctPoints(Point a, Point b) {
        if (a.isCoincident(b)) {
            throw new IllegalArgumentException("Line requires two distinct points");
        }
    }

    private static void validateCoordinatesArray(double[][] coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates array cannot be null");
        }
        if (coordinates.length != 2) {
            throw new IllegalArgumentException(
                String.format("Expected 2 coordinate pairs, got %d", coordinates.length)
            );
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Line line = (Line) obj;
        return Objects.equals(pointA, line.pointA)
            && Objects.equals(pointB, line.pointB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pointA, pointB);
    }

    @Override
    public String toString() {
        return String.format("Line[%s -> %s]", pointA, pointB);
    }
}