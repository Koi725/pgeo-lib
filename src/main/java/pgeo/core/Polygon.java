package pgeo.core;

import java.util.Arrays;
import java.util.Objects;

public final class Polygon {

    private final Point[] vertices;

    public Polygon(Point[] vertices) {
        validateVertices(vertices);
        this.vertices = copyVertices(vertices);
    }

    public Point[] getVertices() {
        return copyVertices(vertices);
    }

    public Point getVertex(int index) {
        if (index < 0 || index >= vertices.length) {
            throw new IndexOutOfBoundsException(
                String.format("Index %d out of bounds for polygon with %d vertices", index, vertices.length)
            );
        }
        return vertices[index];
    }

    public int getVertexCount() {
        return vertices.length;
    }

    public double calculateArea() {
        return Math.abs(calculateSignedArea());
    }

    public double calculateSignedArea() {
        double sum = 0.0;

        for (int i = 0; i < vertices.length; i++) {
            Point current = vertices[i];
            Point next = vertices[(i + 1) % vertices.length];

            sum += (current.getX() * next.getY()) - (next.getX() * current.getY());
        }

        return sum / 2.0;
    }

    public double calculatePerimeter() {
        double perimeter = 0.0;

        for (int i = 0; i < vertices.length; i++) {
            Point current = vertices[i];
            Point next = vertices[(i + 1) % vertices.length];
            perimeter += current.distanceTo(next);
        }

        return perimeter;
    }

    public boolean isConvex() {
        int vertexCount = vertices.length;
        if (vertexCount < 3) {
            return false;
        }

        Boolean isPositive = null;

        for (int i = 0; i < vertexCount; i++) {
            Point a = vertices[i];
            Point b = vertices[(i + 1) % vertexCount];
            Point c = vertices[(i + 2) % vertexCount];

            double crossProduct = calculateCrossProduct(a, b, c);

            if (Double.compare(crossProduct, 0.0) != 0) {
                boolean currentPositive = crossProduct > 0;

                if (isPositive == null) {
                    isPositive = currentPositive;
                } else if (isPositive != currentPositive) {
                    return false;
                }
            }
        }

        return true;
    }

    public static Polygon createQuadrilateral(Point a, Point b, Point c, Point d) {
        Objects.requireNonNull(a, "Point A cannot be null");
        Objects.requireNonNull(b, "Point B cannot be null");
        Objects.requireNonNull(c, "Point C cannot be null");
        Objects.requireNonNull(d, "Point D cannot be null");

        return new Polygon(new Point[]{a, b, c, d});
    }

    public static Polygon fromArray(double[][] coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates array cannot be null");
        }
        if (coordinates.length < 3) {
            throw new IllegalArgumentException("Polygon requires at least 3 vertices");
        }

        Point[] points = new Point[coordinates.length];
        for (int i = 0; i < coordinates.length; i++) {
            points[i] = Point.fromArray(coordinates[i]);
        }

        return new Polygon(points);
    }

    public double[][] toArray() {
        double[][] result = new double[vertices.length][];
        for (int i = 0; i < vertices.length; i++) {
            result[i] = vertices[i].toArray();
        }
        return result;
    }

    private double calculateCrossProduct(Point a, Point b, Point c) {
        double abX = b.getX() - a.getX();
        double abY = b.getY() - a.getY();
        double bcX = c.getX() - b.getX();
        double bcY = c.getY() - b.getY();

        return (abX * bcY) - (abY * bcX);
    }

    private void validateVertices(Point[] vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("Vertices array cannot be null");
        }
        if (vertices.length < 3) {
            throw new IllegalArgumentException("Polygon requires at least 3 vertices");
        }

        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i] == null) {
                throw new IllegalArgumentException(
                    String.format("Vertex at index %d cannot be null", i)
                );
            }
        }

        validateNoCoincidentVertices(vertices);
        validateNonCollinear(vertices);
    }

    private void validateNoCoincidentVertices(Point[] vertices) {
        for (int i = 0; i < vertices.length; i++) {
            for (int j = i + 1; j < vertices.length; j++) {
                if (vertices[i].isCoincident(vertices[j])) {
                    throw new IllegalArgumentException(
                        String.format("Vertices at index %d and %d are coincident", i, j)
                    );
                }
            }
        }
    }

    private void validateNonCollinear(Point[] vertices) {
        if (vertices.length == 3) {
            double area = Triangle.calculateSignedAreaFromPoints(
                vertices[0], vertices[1], vertices[2]
            );
            if (Double.compare(area, 0.0) == 0) {
                throw new IllegalArgumentException("All vertices are collinear");
            }
        }
    }

    private Point[] copyVertices(Point[] source) {
        Point[] copy = new Point[source.length];
        System.arraycopy(source, 0, copy, 0, source.length);
        return copy;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Polygon polygon = (Polygon) obj;
        return Arrays.equals(vertices, polygon.vertices);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(vertices);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Polygon[");
        for (int i = 0; i < vertices.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(vertices[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}