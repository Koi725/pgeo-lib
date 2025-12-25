package pgeo.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Point")
class PointTest {

    private static final double DELTA = 1e-10;

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create point with valid coordinates")
        void shouldCreatePointWithValidCoordinates() {
            Point point = new Point(3.5, 4.5);

            assertEquals(3.5, point.getX(), DELTA);
            assertEquals(4.5, point.getY(), DELTA);
        }

        @Test
        @DisplayName("should create point at origin")
        void shouldCreatePointAtOrigin() {
            Point point = new Point(0, 0);

            assertEquals(0, point.getX(), DELTA);
            assertEquals(0, point.getY(), DELTA);
        }

        @Test
        @DisplayName("should create point with negative coordinates")
        void shouldCreatePointWithNegativeCoordinates() {
            Point point = new Point(-5.5, -10.2);

            assertEquals(-5.5, point.getX(), DELTA);
            assertEquals(-10.2, point.getY(), DELTA);
        }

        @Test
        @DisplayName("should reject NaN x coordinate")
        void shouldRejectNaNXCoordinate() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Point(Double.NaN, 5.0)
            );
            assertTrue(exception.getMessage().contains("x"));
        }

        @Test
        @DisplayName("should reject NaN y coordinate")
        void shouldRejectNaNYCoordinate() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Point(5.0, Double.NaN)
            );
            assertTrue(exception.getMessage().contains("y"));
        }

        @Test
        @DisplayName("should reject infinite x coordinate")
        void shouldRejectInfiniteXCoordinate() {
            assertThrows(
                IllegalArgumentException.class,
                () -> new Point(Double.POSITIVE_INFINITY, 5.0)
            );
        }

        @Test
        @DisplayName("should reject negative infinite y coordinate")
        void shouldRejectNegativeInfiniteYCoordinate() {
            assertThrows(
                IllegalArgumentException.class,
                () -> new Point(5.0, Double.NEGATIVE_INFINITY)
            );
        }
    }

    @Nested
    @DisplayName("Factory Method")
    class FactoryMethod {

        @Test
        @DisplayName("should create point from valid array")
        void shouldCreatePointFromValidArray() {
            double[] coords = {2.5, 3.5};

            Point point = Point.fromArray(coords);

            assertEquals(2.5, point.getX(), DELTA);
            assertEquals(3.5, point.getY(), DELTA);
        }

        @Test
        @DisplayName("should reject null array")
        void shouldRejectNullArray() {
            assertThrows(
                IllegalArgumentException.class,
                () -> Point.fromArray(null)
            );
        }

        @Test
        @DisplayName("should reject array with wrong size")
        void shouldRejectArrayWithWrongSize() {
            double[] coords = {1.0, 2.0, 3.0};

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Point.fromArray(coords)
            );
            assertTrue(exception.getMessage().contains("2 elements"));
        }

        @Test
        @DisplayName("should reject empty array")
        void shouldRejectEmptyArray() {
            double[] coords = {};

            assertThrows(
                IllegalArgumentException.class,
                () -> Point.fromArray(coords)
            );
        }
    }

    @Nested
    @DisplayName("Distance Calculation")
    class DistanceCalculation {

        @ParameterizedTest
        @CsvSource({
            "0, 0, 3, 4, 5.0",
            "0, 0, 0, 0, 0.0",
            "1, 1, 4, 5, 5.0",
            "-3, -4, 0, 0, 5.0"
        })
        @DisplayName("should calculate correct distance")
        void shouldCalculateCorrectDistance(
                double x1, double y1, double x2, double y2, double expected) {
            Point p1 = new Point(x1, y1);
            Point p2 = new Point(x2, y2);

            assertEquals(expected, p1.distanceTo(p2), DELTA);
        }

        @Test
        @DisplayName("should be symmetric")
        void shouldBeSymmetric() {
            Point p1 = new Point(1, 2);
            Point p2 = new Point(4, 6);

            assertEquals(p1.distanceTo(p2), p2.distanceTo(p1), DELTA);
        }

        @Test
        @DisplayName("should reject null point")
        void shouldRejectNullPoint() {
            Point point = new Point(0, 0);

            assertThrows(
                NullPointerException.class,
                () -> point.distanceTo(null)
            );
        }
    }

    @Nested
    @DisplayName("Coincidence Check")
    class CoincidenceCheck {

        @Test
        @DisplayName("should detect coincident points")
        void shouldDetectCoincidentPoints() {
            Point p1 = new Point(3.5, 4.5);
            Point p2 = new Point(3.5, 4.5);

            assertTrue(p1.isCoincident(p2));
        }

        @Test
        @DisplayName("should detect non-coincident points")
        void shouldDetectNonCoincidentPoints() {
            Point p1 = new Point(3.5, 4.5);
            Point p2 = new Point(3.5, 4.6);

            assertFalse(p1.isCoincident(p2));
        }

        @Test
        @DisplayName("should reject null point")
        void shouldRejectNullPoint() {
            Point point = new Point(0, 0);

            assertThrows(
                NullPointerException.class,
                () -> point.isCoincident(null)
            );
        }
    }

    @Nested
    @DisplayName("Array Conversion")
    class ArrayConversion {

        @Test
        @DisplayName("should convert to array correctly")
        void shouldConvertToArrayCorrectly() {
            Point point = new Point(5.5, 7.7);

            double[] array = point.toArray();

            assertEquals(2, array.length);
            assertEquals(5.5, array[0], DELTA);
            assertEquals(7.7, array[1], DELTA);
        }

        @Test
        @DisplayName("should return new array instance")
        void shouldReturnNewArrayInstance() {
            Point point = new Point(5.5, 7.7);

            double[] array1 = point.toArray();
            double[] array2 = point.toArray();

            assertNotSame(array1, array2);
        }
    }

    @Nested
    @DisplayName("Equality")
    class Equality {

        @Test
        @DisplayName("should be equal to itself")
        void shouldBeEqualToItself() {
            Point point = new Point(1, 2);

            assertEquals(point, point);
        }

        @Test
        @DisplayName("should be equal to point with same coordinates")
        void shouldBeEqualToPointWithSameCoordinates() {
            Point p1 = new Point(3.5, 4.5);
            Point p2 = new Point(3.5, 4.5);

            assertEquals(p1, p2);
        }

        @Test
        @DisplayName("should not be equal to point with different coordinates")
        void shouldNotBeEqualToPointWithDifferentCoordinates() {
            Point p1 = new Point(3.5, 4.5);
            Point p2 = new Point(3.5, 5.5);

            assertNotEquals(p1, p2);
        }

        @Test
        @DisplayName("should not be equal to null")
        void shouldNotBeEqualToNull() {
            Point point = new Point(1, 2);

            assertNotEquals(null, point);
        }

        @Test
        @DisplayName("should have consistent hash code")
        void shouldHaveConsistentHashCode() {
            Point p1 = new Point(3.5, 4.5);
            Point p2 = new Point(3.5, 4.5);

            assertEquals(p1.hashCode(), p2.hashCode());
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentation {

        @Test
        @DisplayName("should format correctly")
        void shouldFormatCorrectly() {
            Point point = new Point(3.5, 4.5);

            String result = point.toString();

            assertTrue(result.contains("3.5"));
            assertTrue(result.contains("4.5"));
            assertTrue(result.startsWith("Point"));
        }
    }
}