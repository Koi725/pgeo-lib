package pgeo.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GeometryValidator")
class GeometryValidatorTest {

    @Nested
    @DisplayName("Utility Class")
    class UtilityClass {

        @Test
        @DisplayName("should not allow instantiation")
        void shouldNotAllowInstantiation() throws NoSuchMethodException {
            Constructor<GeometryValidator> constructor = 
                GeometryValidator.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
            );
        }
    }

    @Nested
    @DisplayName("Triangle Validation")
    class TriangleValidation {

        @Test
        @DisplayName("should accept valid triangle array")
        void shouldAcceptValidTriangleArray() {
            double[][] coords = {{0, 0}, {4, 0}, {2, 3}};

            assertDoesNotThrow(() -> GeometryValidator.validateTriangleArray(coords));
        }

        @Test
        @DisplayName("should reject null triangle array")
        void shouldRejectNullTriangleArray() {
            assertThrows(
                IllegalArgumentException.class,
                () -> GeometryValidator.validateTriangleArray(null)
            );
        }

        @Test
        @DisplayName("should reject triangle with wrong point count")
        void shouldRejectTriangleWithWrongPointCount() {
            double[][] coords = {{0, 0}, {4, 0}};

            assertThrows(
                IllegalArgumentException.class,
                () -> GeometryValidator.validateTriangleArray(coords)
            );
        }

        @Test
        @DisplayName("should reject triangle with null point")
        void shouldRejectTriangleWithNullPoint() {
            double[][] coords = {{0, 0}, null, {2, 3}};

            assertThrows(
                IllegalArgumentException.class,
                () -> GeometryValidator.validateTriangleArray(coords)
            );
        }

        @Test
        @DisplayName("should reject triangle with NaN coordinate")
        void shouldRejectTriangleWithNaNCoordinate() {
            double[][] coords = {{0, 0}, {Double.NaN, 0}, {2, 3}};

            assertThrows(
                IllegalArgumentException.class,
                () -> GeometryValidator.validateTriangleArray(coords)
            );
        }

        @Test
        @DisplayName("should validate triangle array correctly")
        void shouldValidateTriangleArrayCorrectly() {
            double[][] valid = {{0, 0}, {4, 0}, {2, 3}};
            double[][] invalid = {{0, 0}, {4, 0}};

            assertTrue(GeometryValidator.isValidTriangleArray(valid));
            assertFalse(GeometryValidator.isValidTriangleArray(invalid));
            assertFalse(GeometryValidator.isValidTriangleArray(null));
        }
    }

    @Nested
    @DisplayName("Quadrilateral Validation")
    class QuadrilateralValidation {

        @Test
        @DisplayName("should accept valid quadrilateral array")
        void shouldAcceptValidQuadrilateralArray() {
            double[][] coords = {{0, 0}, {4, 0}, {4, 3}, {0, 3}};

            assertDoesNotThrow(() -> GeometryValidator.validateQuadrilateralArray(coords));
        }

        @Test
        @DisplayName("should reject null quadrilateral array")
        void shouldRejectNullQuadrilateralArray() {
            assertThrows(
                IllegalArgumentException.class,
                () -> GeometryValidator.validateQuadrilateralArray(null)
            );
        }

        @Test
        @DisplayName("should reject quadrilateral with wrong point count")
        void shouldRejectQuadrilateralWithWrongPointCount() {
            double[][] coords = {{0, 0}, {4, 0}, {2, 3}};

            assertThrows(
                IllegalArgumentException.class,
                () -> GeometryValidator.validateQuadrilateralArray(coords)
            );
        }

        @Test
        @DisplayName("should validate quadrilateral array correctly")
        void shouldValidateQuadrilateralArrayCorrectly() {
            double[][] valid = {{0, 0}, {4, 0}, {4, 3}, {0, 3}};
            double[][] invalid = {{0, 0}, {4, 0}, {2, 3}};

            assertTrue(GeometryValidator.isValidQuadrilateralArray(valid));
            assertFalse(GeometryValidator.isValidQuadrilateralArray(invalid));
        }
    }

    @Nested
    @DisplayName("Line Validation")
    class LineValidation {

        @Test
        @DisplayName("should accept valid line array")
        void shouldAcceptValidLineArray() {
            double[][] coords = {{0, 0}, {4, 4}};

            assertDoesNotThrow(() -> GeometryValidator.validateLineArray(coords));
        }

        @Test
        @DisplayName("should reject line with wrong point count")
        void shouldRejectLineWithWrongPointCount() {
            double[][] coords = {{0, 0}};

            assertThrows(
                IllegalArgumentException.class,
                () -> GeometryValidator.validateLineArray(coords)
            );
        }

        @Test
        @DisplayName("should validate line array correctly")
        void shouldValidateLineArrayCorrectly() {
            double[][] valid = {{0, 0}, {4, 4}};
            double[][] invalid = {{0, 0}};

            assertTrue(GeometryValidator.isValidLineArray(valid));
            assertFalse(GeometryValidator.isValidLineArray(invalid));
        }
    }

    @Nested
    @DisplayName("Segment-Line Validation")
    class SegmentLineValidation {

        @Test
        @DisplayName("should accept valid segment-line array")
        void shouldAcceptValidSegmentLineArray() {
            double[][] coords = {{0, 0}, {4, 4}, {0, 4}, {4, 0}};

            assertDoesNotThrow(() -> GeometryValidator.validateSegmentWithLineArray(coords));
        }

        @Test
        @DisplayName("should reject segment-line with wrong point count")
        void shouldRejectSegmentLineWithWrongPointCount() {
            double[][] coords = {{0, 0}, {4, 4}, {0, 4}};

            assertThrows(
                IllegalArgumentException.class,
                () -> GeometryValidator.validateSegmentWithLineArray(coords)
            );
        }

        @Test
        @DisplayName("should validate segment-line array correctly")
        void shouldValidateSegmentLineArrayCorrectly() {
            double[][] valid = {{0, 0}, {4, 4}, {0, 4}, {4, 0}};
            double[][] invalid = {{0, 0}, {4, 4}};

            assertTrue(GeometryValidator.isValidSegmentWithLineArray(valid));
            assertFalse(GeometryValidator.isValidSegmentWithLineArray(invalid));
        }
    }

    @Nested
    @DisplayName("Point Validation")
    class PointValidation {

        @Test
        @DisplayName("should accept valid point array")
        void shouldAcceptValidPointArray() {
            double[] coords = {3.5, 4.5};

            assertDoesNotThrow(() -> GeometryValidator.validatePointArray(coords));
        }

        @Test
        @DisplayName("should reject null point array")
        void shouldRejectNullPointArray() {
            assertThrows(
                IllegalArgumentException.class,
                () -> GeometryValidator.validatePointArray(null)
            );
        }

        @Test
        @DisplayName("should reject point with wrong coordinate count")
        void shouldRejectPointWithWrongCoordinateCount() {
            double[] coords = {1.0, 2.0, 3.0};

            assertThrows(
                IllegalArgumentException.class,
                () -> GeometryValidator.validatePointArray(coords)
            );
        }

        @Test
        @DisplayName("should reject point with NaN")
        void shouldRejectPointWithNaN() {
            double[] coords = {Double.NaN, 4.5};

            assertThrows(
                IllegalArgumentException.class,
                () -> GeometryValidator.validatePointArray(coords)
            );
        }

        @Test
        @DisplayName("should reject point with infinity")
        void shouldRejectPointWithInfinity() {
            double[] coords = {3.5, Double.POSITIVE_INFINITY};

            assertThrows(
                IllegalArgumentException.class,
                () -> GeometryValidator.validatePointArray(coords)
            );
        }

        @Test
        @DisplayName("should validate point array correctly")
        void shouldValidatePointArrayCorrectly() {
            assertTrue(GeometryValidator.isValidPointArray(new double[]{1, 2}));
            assertFalse(GeometryValidator.isValidPointArray(null));
            assertFalse(GeometryValidator.isValidPointArray(new double[]{1}));
            assertFalse(GeometryValidator.isValidPointArray(new double[]{Double.NaN, 2}));
        }
    }

    @Nested
    @DisplayName("Coordinate Validation")
    class CoordinateValidation {

        @Test
        @DisplayName("should accept valid coordinate")
        void shouldAcceptValidCoordinate() {
            assertDoesNotThrow(() -> GeometryValidator.validateCoordinate(5.5, "test"));
        }

        @Test
        @DisplayName("should accept zero")
        void shouldAcceptZero() {
            assertDoesNotThrow(() -> GeometryValidator.validateCoordinate(0, "test"));
        }

        @Test
        @DisplayName("should accept negative coordinate")
        void shouldAcceptNegativeCoordinate() {
            assertDoesNotThrow(() -> GeometryValidator.validateCoordinate(-100.5, "test"));
        }

        @Test
        @DisplayName("should reject NaN")
        void shouldRejectNaN() {
            assertThrows(
                IllegalArgumentException.class,
                () -> GeometryValidator.validateCoordinate(Double.NaN, "test")
            );
        }

        @Test
        @DisplayName("should reject positive infinity")
        void shouldRejectPositiveInfinity() {
            assertThrows(
                IllegalArgumentException.class,
                () -> GeometryValidator.validateCoordinate(Double.POSITIVE_INFINITY, "test")
            );
        }

        @Test
        @DisplayName("should reject negative infinity")
        void shouldRejectNegativeInfinity() {
            assertThrows(
                IllegalArgumentException.class,
                () -> GeometryValidator.validateCoordinate(Double.NEGATIVE_INFINITY, "test")
            );
        }

        @Test
        @DisplayName("should validate coordinate correctly")
        void shouldValidateCoordinateCorrectly() {
            assertTrue(GeometryValidator.isValidCoordinate(5.5));
            assertTrue(GeometryValidator.isValidCoordinate(0));
            assertTrue(GeometryValidator.isValidCoordinate(-100));
            assertFalse(GeometryValidator.isValidCoordinate(Double.NaN));
            assertFalse(GeometryValidator.isValidCoordinate(Double.POSITIVE_INFINITY));
        }
    }

    @Nested
    @DisplayName("Distinct Points Validation")
    class DistinctPointsValidation {

        @Test
        @DisplayName("should detect distinct points")
        void shouldDetectDistinctPoints() {
            double[] p1 = {0, 0};
            double[] p2 = {1, 1};

            assertTrue(GeometryValidator.arePointsDistinct(p1, p2));
        }

        @Test
        @DisplayName("should detect coincident points")
        void shouldDetectCoincidentPoints() {
            double[] p1 = {3.5, 4.5};
            double[] p2 = {3.5, 4.5};

            assertFalse(GeometryValidator.arePointsDistinct(p1, p2));
        }

        @Test
        @DisplayName("should handle invalid points")
        void shouldHandleInvalidPoints() {
            double[] valid = {0, 0};
            double[] invalid = {Double.NaN, 0};

            assertFalse(GeometryValidator.arePointsDistinct(valid, invalid));
            assertFalse(GeometryValidator.arePointsDistinct(null, valid));
        }

        @Test
        @DisplayName("should validate all points distinct")
        void shouldValidateAllPointsDistinct() {
            double[][] distinct = {{0, 0}, {1, 1}, {2, 2}};
            double[][] coincident = {{0, 0}, {1, 1}, {0, 0}};

            assertTrue(GeometryValidator.areAllPointsDistinct(distinct));
            assertFalse(GeometryValidator.areAllPointsDistinct(coincident));
            assertFalse(GeometryValidator.areAllPointsDistinct(null));
        }
    }
}