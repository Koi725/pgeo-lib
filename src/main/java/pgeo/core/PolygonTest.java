package pgeo.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Polygon")
class PolygonTest {

    private static final double DELTA = 1e-10;

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create polygon with valid vertices")
        void shouldCreatePolygonWithValidVertices() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(4, 0),
                new Point(4, 3),
                new Point(0, 3)
            };

            Polygon polygon = new Polygon(vertices);

            assertEquals(4, polygon.getVertexCount());
        }

        @Test
        @DisplayName("should create triangle")
        void shouldCreateTriangle() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(4, 0),
                new Point(2, 3)
            };

            Polygon polygon = new Polygon(vertices);

            assertEquals(3, polygon.getVertexCount());
        }

        @Test
        @DisplayName("should reject null vertices array")
        void shouldRejectNullVerticesArray() {
            assertThrows(
                IllegalArgumentException.class,
                () -> new Polygon(null)
            );
        }

        @Test
        @DisplayName("should reject less than 3 vertices")
        void shouldRejectLessThan3Vertices() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(4, 0)
            };

            assertThrows(
                IllegalArgumentException.class,
                () -> new Polygon(vertices)
            );
        }

        @Test
        @DisplayName("should reject null vertex in array")
        void shouldRejectNullVertexInArray() {
            Point[] vertices = {
                new Point(0, 0),
                null,
                new Point(2, 3)
            };

            assertThrows(
                IllegalArgumentException.class,
                () -> new Polygon(vertices)
            );
        }

        @Test
        @DisplayName("should reject coincident vertices")
        void shouldRejectCoincidentVertices() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(4, 0),
                new Point(4, 0),
                new Point(0, 3)
            };

            assertThrows(
                IllegalArgumentException.class,
                () -> new Polygon(vertices)
            );
        }

        @Test
        @DisplayName("should reject collinear triangle vertices")
        void shouldRejectCollinearTriangleVertices() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(2, 2),
                new Point(4, 4)
            };

            assertThrows(
                IllegalArgumentException.class,
                () -> new Polygon(vertices)
            );
        }
    }

    @Nested
    @DisplayName("Vertex Access")
    class VertexAccess {

        @Test
        @DisplayName("should return correct vertex by index")
        void shouldReturnCorrectVertexByIndex() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(4, 0),
                new Point(4, 3),
                new Point(0, 3)
            };
            Polygon polygon = new Polygon(vertices);

            assertEquals(new Point(4, 0), polygon.getVertex(1));
            assertEquals(new Point(0, 3), polygon.getVertex(3));
        }

        @Test
        @DisplayName("should throw exception for invalid index")
        void shouldThrowExceptionForInvalidIndex() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(4, 0),
                new Point(2, 3)
            };
            Polygon polygon = new Polygon(vertices);

            assertThrows(
                IndexOutOfBoundsException.class,
                () -> polygon.getVertex(5)
            );
        }

        @Test
        @DisplayName("should throw exception for negative index")
        void shouldThrowExceptionForNegativeIndex() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(4, 0),
                new Point(2, 3)
            };
            Polygon polygon = new Polygon(vertices);

            assertThrows(
                IndexOutOfBoundsException.class,
                () -> polygon.getVertex(-1)
            );
        }

        @Test
        @DisplayName("should return defensive copy of vertices")
        void shouldReturnDefensiveCopyOfVertices() {
            Point[] original = {
                new Point(0, 0),
                new Point(4, 0),
                new Point(2, 3)
            };
            Polygon polygon = new Polygon(original);

            Point[] retrieved = polygon.getVertices();

            assertNotSame(original, retrieved);
        }
    }

    @Nested
    @DisplayName("Area Calculation")
    class AreaCalculation {

        @Test
        @DisplayName("should calculate area of square")
        void shouldCalculateAreaOfSquare() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(4, 0),
                new Point(4, 4),
                new Point(0, 4)
            };
            Polygon polygon = new Polygon(vertices);

            assertEquals(16.0, polygon.calculateArea(), DELTA);
        }

        @Test
        @DisplayName("should calculate area of rectangle")
        void shouldCalculateAreaOfRectangle() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(6, 0),
                new Point(6, 4),
                new Point(0, 4)
            };
            Polygon polygon = new Polygon(vertices);

            assertEquals(24.0, polygon.calculateArea(), DELTA);
        }

        @Test
        @DisplayName("should calculate area of triangle")
        void shouldCalculateAreaOfTriangle() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(4, 0),
                new Point(0, 3)
            };
            Polygon polygon = new Polygon(vertices);

            assertEquals(6.0, polygon.calculateArea(), DELTA);
        }

        @Test
        @DisplayName("should calculate area with negative coordinates")
        void shouldCalculateAreaWithNegativeCoordinates() {
            Point[] vertices = {
                new Point(-2, -2),
                new Point(2, -2),
                new Point(2, 2),
                new Point(-2, 2)
            };
            Polygon polygon = new Polygon(vertices);

            assertEquals(16.0, polygon.calculateArea(), DELTA);
        }

        @ParameterizedTest
        @MethodSource("provideQuadrilateralAreaTestCases")
        @DisplayName("should calculate correct area for various quadrilaterals")
        void shouldCalculateCorrectAreaForVariousQuadrilaterals(
                double[][] coords, double expectedArea) {
            Polygon polygon = Polygon.fromArray(coords);

            assertEquals(expectedArea, polygon.calculateArea(), DELTA);
        }

        static Stream<Arguments> provideQuadrilateralAreaTestCases() {
            return Stream.of(
                Arguments.of(
                    new double[][]{{0, 0}, {5, 0}, {5, 5}, {0, 5}},
                    25.0
                ),
                Arguments.of(
                    new double[][]{{0, 0}, {10, 0}, {10, 5}, {0, 5}},
                    50.0
                ),
                Arguments.of(
                    new double[][]{{0, 0}, {3, 0}, {3, 3}, {0, 3}},
                    9.0
                )
            );
        }
    }

    @Nested
    @DisplayName("Signed Area")
    class SignedArea {

        @Test
        @DisplayName("should return positive for counter-clockwise vertices")
        void shouldReturnPositiveForCounterClockwise() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(4, 0),
                new Point(4, 4),
                new Point(0, 4)
            };
            Polygon polygon = new Polygon(vertices);

            assertTrue(polygon.calculateSignedArea() > 0);
        }

        @Test
        @DisplayName("should return negative for clockwise vertices")
        void shouldReturnNegativeForClockwise() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(0, 4),
                new Point(4, 4),
                new Point(4, 0)
            };
            Polygon polygon = new Polygon(vertices);

            assertTrue(polygon.calculateSignedArea() < 0);
        }
    }

    @Nested
    @DisplayName("Perimeter Calculation")
    class PerimeterCalculation {

        @Test
        @DisplayName("should calculate perimeter of square")
        void shouldCalculatePerimeterOfSquare() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(4, 0),
                new Point(4, 4),
                new Point(0, 4)
            };
            Polygon polygon = new Polygon(vertices);

            assertEquals(16.0, polygon.calculatePerimeter(), DELTA);
        }

        @Test
        @DisplayName("should calculate perimeter of rectangle")
        void shouldCalculatePerimeterOfRectangle() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(6, 0),
                new Point(6, 4),
                new Point(0, 4)
            };
            Polygon polygon = new Polygon(vertices);

            assertEquals(20.0, polygon.calculatePerimeter(), DELTA);
        }

        @Test
        @DisplayName("should calculate perimeter of triangle")
        void shouldCalculatePerimeterOfTriangle() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(3, 0),
                new Point(0, 4)
            };
            Polygon polygon = new Polygon(vertices);

            assertEquals(12.0, polygon.calculatePerimeter(), DELTA);
        }
    }

    @Nested
    @DisplayName("Convexity Check")
    class ConvexityCheck {

        @Test
        @DisplayName("should detect convex square")
        void shouldDetectConvexSquare() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(4, 0),
                new Point(4, 4),
                new Point(0, 4)
            };
            Polygon polygon = new Polygon(vertices);

            assertTrue(polygon.isConvex());
        }

        @Test
        @DisplayName("should detect convex triangle")
        void shouldDetectConvexTriangle() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(4, 0),
                new Point(2, 3)
            };
            Polygon polygon = new Polygon(vertices);

            assertTrue(polygon.isConvex());
        }

        @Test
        @DisplayName("should detect non-convex polygon")
        void shouldDetectNonConvexPolygon() {
            Point[] vertices = {
                new Point(0, 0),
                new Point(4, 0),
                new Point(2, 1),
                new Point(4, 4),
                new Point(0, 4)
            };
            Polygon polygon = new Polygon(vertices);

            assertFalse(polygon.isConvex());
        }
    }

    @Nested
    @DisplayName("Quadrilateral Factory")
    class QuadrilateralFactory {

        @Test
        @DisplayName("should create quadrilateral from four points")
        void shouldCreateQuadrilateralFromFourPoints() {
            Point a = new Point(0, 0);
            Point b = new Point(4, 0);
            Point c = new Point(4, 3);
            Point d = new Point(0, 3);

            Polygon quad = Polygon.createQuadrilateral(a, b, c, d);

            assertEquals(4, quad.getVertexCount());
            assertEquals(a, quad.getVertex(0));
            assertEquals(b, quad.getVertex(1));
            assertEquals(c, quad.getVertex(2));
            assertEquals(d, quad.getVertex(3));
        }

        @Test
        @DisplayName("should reject null points")
        void shouldRejectNullPoints() {
            Point a = new Point(0, 0);
            Point b = new Point(4, 0);
            Point c = new Point(4, 3);

            assertThrows(
                NullPointerException.class,
                () -> Polygon.createQuadrilateral(a, b, c, null)
            );
        }
    }

    @Nested
    @DisplayName("Factory Method")
    class FactoryMethod {

        @Test
        @DisplayName("should create polygon from valid array")
        void shouldCreatePolygonFromValidArray() {
            double[][] coords = {
                {0, 0},
                {4, 0},
                {4, 3},
                {0, 3}
            };

            Polygon polygon = Polygon.fromArray(coords);

            assertEquals(4, polygon.getVertexCount());
        }

        @Test
        @DisplayName("should reject null array")
        void shouldRejectNullArray() {
            assertThrows(
                IllegalArgumentException.class,
                () -> Polygon.fromArray(null)
            );
        }

        @Test
        @DisplayName("should reject array with less than 3 vertices")
        void shouldRejectArrayWithLessThan3Vertices() {
            double[][] coords = {
                {0, 0},
                {4, 0}
            };

            assertThrows(
                IllegalArgumentException.class,
                () -> Polygon.fromArray(coords)
            );
        }
    }

    @Nested
    @DisplayName("Array Conversion")
    class ArrayConversion {

        @Test
        @DisplayName("should convert to array correctly")
        void shouldConvertToArrayCorrectly() {
            Point[] vertices = {
                new Point(1, 2),
                new Point(3, 4),
                new Point(5, 6)
            };
            Polygon polygon = new Polygon(vertices);

            double[][] array = polygon.toArray();

            assertEquals(3, array.length);
            assertEquals(1, array[0][0], DELTA);
            assertEquals(2, array[0][1], DELTA);
            assertEquals(3, array[1][0], DELTA);
            assertEquals(4, array[1][1], DELTA);
            assertEquals(5, array[2][0], DELTA);
            assertEquals(6, array[2][1], DELTA);
        }
    }

    @Nested
    @DisplayName("Equality")
    class Equality {

        @Test
        @DisplayName("should be equal to polygon with same vertices")
        void shouldBeEqualToPolygonWithSameVertices() {
            Point[] vertices1 = {
                new Point(0, 0),
                new Point(4, 0),
                new Point(2, 3)
            };
            Point[] vertices2 = {
                new Point(0, 0),
                new Point(4, 0),
                new Point(2, 3)
            };

            Polygon p1 = new Polygon(vertices1);
            Polygon p2 = new Polygon(vertices2);

            assertEquals(p1, p2);
            assertEquals(p1.hashCode(), p2.hashCode());
        }

        @Test
        @DisplayName("should not be equal to polygon with different vertices")
        void shouldNotBeEqualToPolygonWithDifferentVertices() {
            Point[] vertices1 = {
                new Point(0, 0),
                new Point(4, 0),
                new Point(2, 3)
            };
            Point[] vertices2 = {
                new Point(0, 0),
                new Point(4, 0),
                new Point(2, 4)
            };

            Polygon p1 = new Polygon(vertices1);
            Polygon p2 = new Polygon(vertices2);

            assertNotEquals(p1, p2);
        }
    }
}