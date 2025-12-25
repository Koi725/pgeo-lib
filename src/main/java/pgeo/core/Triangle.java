package pgeo.core;

import java.util.Objects;

public final class Triangle {

    private final Point vertexA;
    private final Point vertexB;
    private final Point vertexC;

    public Triangle(Point vertexA, Point vertexB, Point vertexC) {
        Objects.requireNonNull(vertexA, "Vertex A cannot be null");
        Objects.requireNonNull(vertexB, "Vertex B cannot be null");
        Objects.requireNonNull(vertexC, "Vertex C cannot be null");
        validateNonCollinear(vertexA, vertexB, vertexC);
        this.vertexA = vertexA;
        this.vertexB = vertexB;
        this.vertexC = vertexC;
    }

    public Point getVertexA() {
        return vertexA;
    }

    public Point getVertexB() {
        return vertexB;
    }

    public Point getVertexC() {
        return vertexC;
    }

    public double calculateSignedArea() {
        return calculateSignedAreaFromPoints(vertexA, vertexB, vertexC);
    }

    public double calculateArea() {
        return Math.abs(calculateSignedArea());
    }

    public static double calculateSignedAreaFromPoints(Point a, Point b, Point c) {
        Objects.requireNonNull(a, "Point A cannot be null");
        Objects.requireNonNull(b, "Point B cannot be null");
        Objects.requireNonNull(c, "Point C cannot be null");

        double ax = a.getX();
        double ay = a.getY();
        double bx = b.getX();
        double by = b.getY();
        double cx = c.getX();
        double cy = c.getY();

        double twiceSignedArea = (ax * by) - (ay * bx) 
                               + (ay * cx) - (ax * cy) 
                               + (bx * cy) - (cx * by);

        return twiceSignedArea / 2.0;
    }

    public static double calculateAreaFromPoints(Point a, Point b, Point c) {
        return Math.abs(calculateSignedAreaFromPoints(a, b, c));
    }

    public static Triangle fromArray(double[][] coordinates) {
        validateCoordinatesArray(coordinates, 3);
        Point a = Point.fromArray(coordinates[0]);
        Point b = Point.fromArray(coordinates[1]);
        Point c = Point.fromArray(coordinates[2]);
        return new Triangle(a, b, c);
    }

    public double[][] toArray() {
        return new double[][]{
            vertexA.toArray(),
            vertexB.toArray(),
            vertexC.toArray()
        };
    }

    private void validateNonCollinear(Point a, Point b, Point c) {
        double signedArea = calculateSignedAreaFromPoints(a, b, c);
        if (Double.compare(signedArea, 0.0) == 0) {
            throw new IllegalArgumentException("Points are collinear and cannot form a triangle");
        }
    }

    private static void validateCoordinatesArray(double[][] coordinates, int expectedRows) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates array cannot be null");
        }
        if (coordinates.length != expectedRows) {
            throw new IllegalArgumentException(
                String.format("Expected %d coordinate pairs, got %d", expectedRows, coordinates.length)
            );
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Triangle triangle = (Triangle) obj;
        return Objects.equals(vertexA, triangle.vertexA)
            && Objects.equals(vertexB, triangle.vertexB)
            && Objects.equals(vertexC, triangle.vertexC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertexA, vertexB, vertexC);
    }

    @Override
    public String toString() {
        return String.format("Triangle[A=%s, B=%s, C=%s]", vertexA, vertexB, vertexC);
    }
}