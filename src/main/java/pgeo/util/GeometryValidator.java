package pgeo.util;

public final class GeometryValidator {

    private static final int MIN_TRIANGLE_POINTS = 3;
    private static final int MIN_QUADRILATERAL_POINTS = 4;
    private static final int COORDINATES_PER_POINT = 2;

    private GeometryValidator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static void validateTriangleArray(double[][] coordinates) {
        validateNotNull(coordinates, "Triangle coordinates");
        validateRowCount(coordinates, MIN_TRIANGLE_POINTS, "Triangle");
        validateAllPoints(coordinates);
    }

    public static void validateQuadrilateralArray(double[][] coordinates) {
        validateNotNull(coordinates, "Quadrilateral coordinates");
        validateRowCount(coordinates, MIN_QUADRILATERAL_POINTS, "Quadrilateral");
        validateAllPoints(coordinates);
    }

    public static void validateLineArray(double[][] coordinates) {
        validateNotNull(coordinates, "Line coordinates");
        validateRowCount(coordinates, 2, "Line");
        validateAllPoints(coordinates);
    }

    public static void validateSegmentWithLineArray(double[][] coordinates) {
        validateNotNull(coordinates, "Segment-Line coordinates");
        validateRowCount(coordinates, 4, "Segment-Line intersection");
        validateAllPoints(coordinates);
    }

    public static void validatePointArray(double[] coordinates) {
        validateNotNull(coordinates, "Point coordinates");
        if (coordinates.length != COORDINATES_PER_POINT) {
            throw new IllegalArgumentException(
                String.format("Point must have exactly %d coordinates, got %d",
                    COORDINATES_PER_POINT, coordinates.length)
            );
        }
        validateCoordinate(coordinates[0], "x");
        validateCoordinate(coordinates[1], "y");
    }

    public static void validateCoordinate(double value, String name) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(
                String.format("%s coordinate cannot be NaN", name)
            );
        }
        if (Double.isInfinite(value)) {
            throw new IllegalArgumentException(
                String.format("%s coordinate cannot be infinite", name)
            );
        }
    }

    public static boolean isValidTriangleArray(double[][] coordinates) {
        return isValidArray(coordinates, MIN_TRIANGLE_POINTS);
    }

    public static boolean isValidQuadrilateralArray(double[][] coordinates) {
        return isValidArray(coordinates, MIN_QUADRILATERAL_POINTS);
    }

    public static boolean isValidLineArray(double[][] coordinates) {
        return isValidArray(coordinates, 2);
    }

    public static boolean isValidSegmentWithLineArray(double[][] coordinates) {
        return isValidArray(coordinates, 4);
    }

    public static boolean isValidPointArray(double[] coordinates) {
        if (coordinates == null || coordinates.length != COORDINATES_PER_POINT) {
            return false;
        }
        return isValidCoordinate(coordinates[0]) && isValidCoordinate(coordinates[1]);
    }

    public static boolean isValidCoordinate(double value) {
        return !Double.isNaN(value) && !Double.isInfinite(value);
    }

    public static boolean arePointsDistinct(double[] point1, double[] point2) {
        if (!isValidPointArray(point1) || !isValidPointArray(point2)) {
            return false;
        }
        return Double.compare(point1[0], point2[0]) != 0
            || Double.compare(point1[1], point2[1]) != 0;
    }

    public static boolean areAllPointsDistinct(double[][] coordinates) {
        if (coordinates == null) {
            return false;
        }

        for (int i = 0; i < coordinates.length; i++) {
            for (int j = i + 1; j < coordinates.length; j++) {
                if (!arePointsDistinct(coordinates[i], coordinates[j])) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void validateNotNull(Object obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException(name + " cannot be null");
        }
    }

    private static void validateRowCount(double[][] coordinates, int expected, String shapeName) {
        if (coordinates.length != expected) {
            throw new IllegalArgumentException(
                String.format("%s requires exactly %d points, got %d",
                    shapeName, expected, coordinates.length)
            );
        }
    }

    private static void validateAllPoints(double[][] coordinates) {
        for (int i = 0; i < coordinates.length; i++) {
            if (coordinates[i] == null) {
                throw new IllegalArgumentException(
                    String.format("Point at index %d cannot be null", i)
                );
            }
            if (coordinates[i].length != COORDINATES_PER_POINT) {
                throw new IllegalArgumentException(
                    String.format("Point at index %d must have exactly %d coordinates, got %d",
                        i, COORDINATES_PER_POINT, coordinates[i].length)
                );
            }
            validateCoordinate(coordinates[i][0], String.format("Point[%d].x", i));
            validateCoordinate(coordinates[i][1], String.format("Point[%d].y", i));
        }
    }

    private static boolean isValidArray(double[][] coordinates, int expectedRows) {
        if (coordinates == null || coordinates.length != expectedRows) {
            return false;
        }

        for (double[] point : coordinates) {
            if (!isValidPointArray(point)) {
                return false;
            }
        }
        return true;
    }
}