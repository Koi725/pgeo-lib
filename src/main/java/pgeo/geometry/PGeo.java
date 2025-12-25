package pgeo.geometry;

import pgeo.core.Line;
import pgeo.core.LineSegment;
import pgeo.core.Point;
import pgeo.core.Polygon;
import pgeo.core.Triangle;
import pgeo.util.GeometryValidator;

public class PGeo {

    private static final int POSITION_ABOVE = 1;
    private static final int POSITION_ON_LINE = 0;
    private static final int POSITION_BELOW = -1;

    public double area_triangulo(double[][] A) {
        GeometryValidator.validateTriangleArray(A);
        validateDistinctPoints(A);

        Point a = Point.fromArray(A[0]);
        Point b = Point.fromArray(A[1]);
        Point c = Point.fromArray(A[2]);

        return Triangle.calculateAreaFromPoints(a, b, c);
    }

    public int acima_abaixo(double[][] A) {
        GeometryValidator.validateTriangleArray(A);

        Point lineStart = Point.fromArray(A[0]);
        Point lineEnd = Point.fromArray(A[1]);
        Point testPoint = Point.fromArray(A[2]);

        validateDistinctLinePoints(lineStart, lineEnd);

        Line line = new Line(lineStart, lineEnd);
        int position = line.positionOfPoint(testPoint);

        if (position > 0) {
            return POSITION_ABOVE;
        } else if (position < 0) {
            return POSITION_BELOW;
        }
        return POSITION_ON_LINE;
    }

    public boolean intersecao(double[][] A) {
        GeometryValidator.validateSegmentWithLineArray(A);

        Point segmentStart = Point.fromArray(A[0]);
        Point segmentEnd = Point.fromArray(A[1]);
        Point lineStart = Point.fromArray(A[2]);
        Point lineEnd = Point.fromArray(A[3]);

        validateDistinctLinePoints(segmentStart, segmentEnd);
        validateDistinctLinePoints(lineStart, lineEnd);

        LineSegment segment = new LineSegment(segmentStart, segmentEnd);
        Line line = new Line(lineStart, lineEnd);

        return segment.intersectsLine(line);
    }

    public double[][] ponto_intersecao(double[][] A) {
        GeometryValidator.validateSegmentWithLineArray(A);

        Point segmentStart = Point.fromArray(A[0]);
        Point segmentEnd = Point.fromArray(A[1]);
        Point lineStart = Point.fromArray(A[2]);
        Point lineEnd = Point.fromArray(A[3]);

        validateDistinctLinePoints(segmentStart, segmentEnd);
        validateDistinctLinePoints(lineStart, lineEnd);

        LineSegment segment = new LineSegment(segmentStart, segmentEnd);
        Line line = new Line(lineStart, lineEnd);

        if (!segment.intersectsLine(line)) {
            return null;
        }

        Point intersection = segment.findIntersectionWithLine(line);

        if (intersection == null) {
            return null;
        }

        return new double[][]{{intersection.getX(), intersection.getY()}};
    }

    public double area_P4(double[][] A) {
        GeometryValidator.validateQuadrilateralArray(A);
        validateDistinctPoints(A);

        Point a = Point.fromArray(A[0]);
        Point b = Point.fromArray(A[1]);
        Point c = Point.fromArray(A[2]);
        Point d = Point.fromArray(A[3]);

        Polygon quadrilateral = Polygon.createQuadrilateral(a, b, c, d);

        return quadrilateral.calculateArea();
    }

    public double calculateTriangleSignedArea(double[][] A) {
        GeometryValidator.validateTriangleArray(A);

        Point a = Point.fromArray(A[0]);
        Point b = Point.fromArray(A[1]);
        Point c = Point.fromArray(A[2]);

        return Triangle.calculateSignedAreaFromPoints(a, b, c);
    }

    public boolean arePointsCollinear(double[][] A) {
        GeometryValidator.validateTriangleArray(A);

        Point a = Point.fromArray(A[0]);
        Point b = Point.fromArray(A[1]);
        Point c = Point.fromArray(A[2]);

        double signedArea = Triangle.calculateSignedAreaFromPoints(a, b, c);

        return Double.compare(signedArea, 0.0) == 0;
    }

    public boolean isPointOnLine(double[][] A) {
        GeometryValidator.validateTriangleArray(A);

        return acima_abaixo(A) == POSITION_ON_LINE;
    }

    public boolean isPointAboveLine(double[][] A) {
        GeometryValidator.validateTriangleArray(A);

        return acima_abaixo(A) == POSITION_ABOVE;
    }

    public boolean isPointBelowLine(double[][] A) {
        GeometryValidator.validateTriangleArray(A);

        return acima_abaixo(A) == POSITION_BELOW;
    }

    public String getPointPositionDescription(double[][] A) {
        int position = acima_abaixo(A);

        switch (position) {
            case POSITION_ABOVE:
                return "ABOVE";
            case POSITION_BELOW:
                return "BELOW";
            case POSITION_ON_LINE:
                return "ON_LINE";
            default:
                return "UNKNOWN";
        }
    }

    private void validateDistinctPoints(double[][] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (arePointsCoincident(points[i], points[j])) {
                    throw new IllegalArgumentException(
                        String.format("Points at index %d and %d are coincident", i, j)
                    );
                }
            }
        }
    }

    private void validateDistinctLinePoints(Point a, Point b) {
        if (a.isCoincident(b)) {
            throw new IllegalArgumentException("Line requires two distinct points");
        }
    }

    private boolean arePointsCoincident(double[] p1, double[] p2) {
        return Double.compare(p1[0], p2[0]) == 0 
            && Double.compare(p1[1], p2[1]) == 0;
    }
}