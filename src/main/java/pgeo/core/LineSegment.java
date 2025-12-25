package pgeo.core;

import java.util.Objects;

public final class LineSegment {

    private final Point startPoint;
    private final Point endPoint;

    public LineSegment(Point startPoint, Point endPoint) {
        Objects.requireNonNull(startPoint, "Start point cannot be null");
        Objects.requireNonNull(endPoint, "End point cannot be null");
        validateDistinctPoints(startPoint, endPoint);
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public double getLength() {
        return startPoint.distanceTo(endPoint);
    }

    public Point getMidpoint() {
        double midX = (startPoint.getX() + endPoint.getX()) / 2.0;
        double midY = (startPoint.getY() + endPoint.getY()) / 2.0;
        return new Point(midX, midY);
    }

    public boolean intersectsLine(Line line) {
        Objects.requireNonNull(line, "Line cannot be null");

        int positionStart = line.positionOfPoint(startPoint);
        int positionEnd = line.positionOfPoint(endPoint);

        if (positionStart == 0 || positionEnd == 0) {
            return true;
        }

        return positionStart != positionEnd;
    }

    public Point findIntersectionWithLine(Line line) {
        Objects.requireNonNull(line, "Line cannot be null");

        if (!intersectsLine(line)) {
            return null;
        }

        double x1 = startPoint.getX();
        double y1 = startPoint.getY();
        double x2 = endPoint.getX();
        double y2 = endPoint.getY();

        double x3 = line.getPointA().getX();
        double y3 = line.getPointA().getY();
        double x4 = line.getPointB().getX();
        double y4 = line.getPointB().getY();

        double denominator = ((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4));

        if (Double.compare(denominator, 0.0) == 0) {
            return null;
        }

        double t = (((x1 - x3) * (y3 - y4)) - ((y1 - y3) * (x3 - x4))) / denominator;

        double intersectX = x1 + (t * (x2 - x1));
        double intersectY = y1 + (t * (y2 - y1));

        return new Point(intersectX, intersectY);
    }

    public Line toLine() {
        return new Line(startPoint, endPoint);
    }

    public static LineSegment fromArray(double[][] coordinates) {
        validateCoordinatesArray(coordinates);
        Point start = Point.fromArray(coordinates[0]);
        Point end = Point.fromArray(coordinates[1]);
        return new LineSegment(start, end);
    }

    public double[][] toArray() {
        return new double[][]{
            startPoint.toArray(),
            endPoint.toArray()
        };
    }

    private void validateDistinctPoints(Point a, Point b) {
        if (a.isCoincident(b)) {
            throw new IllegalArgumentException("Line segment requires two distinct points");
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
        LineSegment segment = (LineSegment) obj;
        return Objects.equals(startPoint, segment.startPoint)
            && Objects.equals(endPoint, segment.endPoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPoint, endPoint);
    }

    @Override
    public String toString() {
        return String.format("LineSegment[%s -> %s]", startPoint, endPoint);
    }
}