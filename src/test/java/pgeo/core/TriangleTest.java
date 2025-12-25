package pgeo.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Triangle")
class TriangleTest {

    private static final double DELTA = 1e-10;

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create triangle with valid vertices")
        void shouldCreateTriangleWithValidVertices() {
            Point a = new Point(0, 0);
            Point b = new Point(4, 0);
            Point c = new Point(2, 3);

            Triangle triangle = new Triangle(a, b, c);

            assertEquals(a, triangle.getVertexA());
            assertEquals(b, triangle.getVertexB());
            assertEquals(c, triangle.getVertexC());
        }

        @Test
        @DisplayName("should reject null vertex A")
        void shouldRejectNullVertexA() {
            Point b = new Point(4, 0);
            Point c = new Point(2, 3);

            assertThrows(
                NullPointerException.class,
                () -> new Triangle(null, b, c)
            );
        }

        @Test
        @DisplayName("should reject null vertex B")
        void shouldRejectNullVertexB() {
            Point a = new Point(0, 0);
            Point c = new Point(2, 3);

            assertThrows(
                NullPointerException.class,
                () -> new Triangle(a, null, c)
            );
        }

        @Test
        @DisplayName("should reject null vertex C")
        void shouldRejectNullVertexC() {
            Point a = new Point(0, 0);
            Point b = new Point(4, 0);

            assertThrows(
                NullPointerException.class,
                () -> new Triangle(a, b, null)
            );
        }

        @Test
        @DisplayName("should reject collinear points")
        void shouldRejectCollinearPoints() {
            Point a = new Point(0, 0);
            Point b = new Point(2, 2);
            Point c = new Point(4, 4);

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Triangle(a, b, c)
            );
            assertTrue(exception.getMessage().contains("collinear"));
        }

        @Test
        @DisplayName("should reject coincident points")
        void shouldRejectCoincidentPoints() {
            Point a = new Point(1, 1);
            Point b = new Point(1, 1);
            Point c = new Point(2, 3);

            assertThrows(
                IllegalArgumentException.class,
                () -> new Triangle(a, b, c)
            );
        }
    }

    @Nested
    @DisplayName("Area Calculation")
    class AreaCalculation {

        @Test
        @DisplayName("should calculate area of right triangle")
        void shouldCalculateAreaOfRightTriangle() {
            Point a = new Point(0, 0);
            Point b = new Point(4, 0);
            Point c = new Point(0, 3);

            Triangle triangle = new Triangle(a, b, c);

            assertEquals(6.0, triangle.calculateArea(), DELTA);
        }

        @Test
        @DisplayName("should calculate area of equilateral triangle")
        void shouldCalculateAreaOfEquilateralTriangle() {
            Point a = new Point(0, 0);
            Point b = new Point(2, 0);
            Point c = new Point(1, Math.sqrt(3));

            Triangle triangle = new Triangle(a, b, c);

            double expected = Math.sqrt(3);
            assertEquals(expected, triangle.calculateArea(), DELTA);
        }

        @Test
        @DisplayName("should calculate area with negative coordinates")
        void shouldCalculateAreaWithNegativeCoordinates() {
            Point a = new Point(-2, -2);
            Point b = new Point(2, -2);
            Point c = new Point(0, 2);

            Triangle triangle = new Triangle(a, b, c);

            assertEquals(8.0, triangle.calculateArea(), DELTA);
        }

        @ParameterizedTest
        @MethodSource("provideTriangleAreaTestCases")
        @DisplayName("should calculate correct area for various triangles")
        void shouldCalculateCorrectArea(
                double ax, double ay,
                double bx, double by,
                double cx, double cy,
                double expectedArea) {
            Point a = new Point(ax, ay);
            Point b = new Point(bx, by);
            Point c = new Point(cx, cy);

            Triangle triangle = new Triangle(a, b, c);

            assertEquals(expectedArea, triangle.calculateArea(), DELTA);
        }

        static Stream<Arguments> provideTriangleAreaTestCases() {
            return Stream.of(
                Arguments.of(0, 0, 1, 0, 0, 1, 0.5),
                Arguments.of(0, 0, 6, 0, 3, 4, 12.0),
                Arguments.of(-1, -1, 1, -1, 0, 1, 2.0),
                Arguments.of(0, 0, 10, 0, 5, 8, 40.0)
            );
        }
    }

    @Nested
    @DisplayName("Signed Area")
    class SignedArea {

        @Test
        @DisplayName("should return positive for counter-clockwise vertices")
        void shouldReturnPositiveForCounterClockwise() {
            Point a = new Point(0, 0);
            Point b = new Point(4, 0);
            Point c = new Point(2, 3);

            Triangle triangle = new Triangle(a, b, c);

            assertTrue(triangle.calculateSignedArea() > 0);
        }

        @Test
        @DisplayName("should return negative for clockwise vertices")
        void shouldReturnNegativeForClockwise() {
            Point a = new Point(0, 0);
            Point b = new Point(2, 3);
            Point c = new Point(4, 0);

            Triangle triangle = new Triangle(a, b, c);

            assertTrue(triangle.calculateSignedArea() < 0);
        }
    }

    @Nested
    @DisplayName("Static Area Calculation")
    class StaticAreaCalculation {

        @Test
        @DisplayName("should calculate area from points")
        void shouldCalculateAreaFromPoints() {
            Point a = new Point(0, 0);
            Point b = new Point(4, 0);
            Point c = new Point(0, 3);

            double area = Triangle.calculateAreaFromPoints(a, b, c);

            assertEquals(6.0, area, DELTA);
        }

        @Test
        @DisplayName("should calculate signed area from points")
        void shouldCalculateSignedAreaFromPoints() {
            Point a = new Point(0, 0);
            Point b = new Point(4, 0);
            Point c = new Point(2, 3);

            double signedArea = Triangle.calculateSignedAreaFromPoints(a, b, c);

            assertEquals(6.0, signedArea, DELTA);
        }

        @Test
        @DisplayName("should return zero for collinear points")
        void shouldReturnZeroForCollinearPoints() {
            Point a = new Point(0, 0);
            Point b = new Point(2, 2);
            Point c = new Point(4, 4);

            double signedArea = Triangle.calculateSignedAreaFromPoints(a, b, c);

            assertEquals(0.0, signedArea, DELTA);
        }

        @Test
        @DisplayName("should reject null points")
        void shouldRejectNullPoints() {
            Point a = new Point(0, 0);
            Point b = new Point(4, 0);

            assertThrows(
                NullPointerException.class,
                () -> Triangle.calculateSignedAreaFromPoints(null, b, a)
            );
        }
    }

    @Nested
    @DisplayName("Factory Method")
    class FactoryMethod {

        @Test
        @DisplayName("should create triangle from valid array")
        void shouldCreateTriangleFromValidArray() {
            double[][] coords = {
                {0, 0},
                {4, 0},
                {2, 3}
            };

            Triangle triangle = Triangle.fromArray(coords);

            assertEquals(0, triangle.getVertexA().getX(), DELTA);
            assertEquals(4, triangle.getVertexB().getX(), DELTA);
            assertEquals(2, triangle.getVertexC().getX(), DELTA);
        }

        @Test
        @DisplayName("should reject null array")
        void shouldRejectNullArray() {
            assertThrows(
                IllegalArgumentException.class,
                () -> Triangle.fromArray(null)
            );
        }

        @Test
        @DisplayName("should reject array with wrong size")
        void shouldRejectArrayWithWrongSize() {
            double[][] coords = {
                {0, 0},
                {4, 0}
            };

            assertThrows(
                IllegalArgumentException.class,
                () -> Triangle.fromArray(coords)
            );
        }
    }

    @Nested
    @DisplayName("Array Conversion")
    class ArrayConversion {

        @Test
        @DisplayName("should convert to array correctly")
        void shouldConvertToArrayCorrectly() {
            Point a = new Point(0, 0);
            Point b = new Point(4, 0);
            Point c = new Point(2, 3);
            Triangle triangle = new Triangle(a, b, c);

            double[][] array = triangle.toArray();

            assertEquals(3, array.length);
            assertEquals(0, array[0][0], DELTA);
            assertEquals(0, array[0][1], DELTA);
            assertEquals(4, array[1][0], DELTA);
            assertEquals(0, array[1][1], DELTA);
            assertEquals(2, array[2][0], DELTA);
            assertEquals(3, array[2][1], DELTA);
        }
    }
    @Nested
    @DisplayName("Equality")
    class Equality {

        @Test
        @DisplayName("should be equal to triangle with same vertices")
        void shouldBeEqualToTriangleWithSameVertices() {
            Triangle t1 = new Triangle(
                new Point(0, 0),
                new Point(4, 0),
                new Point(2, 3)
            );
            Triangle t2 = new Triangle(
                new Point(0, 0),
                new Point(4, 0),
                new Point(2, 3)
            );

            assertEquals(t1, t2);
            assertEquals(t1.hashCode(), t2.hashCode());
        }

        @Test
        @DisplayName("should not be equal to triangle with different vertices")
        void shouldNotBeEqualToTriangleWithDifferentVertices() {
            Triangle t1 = new Triangle(
                new Point(0, 0),
                new Point(4, 0),
                new Point(2, 3)
            );
            Triangle t2 = new Triangle(
                new Point(0, 0),
                new Point(4, 0),
                new Point(2, 4)
            );

            assertNotEquals(t1, t2);
        }
    }
}