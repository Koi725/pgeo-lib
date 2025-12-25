package pgeo.geometry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PGeo")
class PGeoTest {

    private static final double DELTA = 1e-10;

    private PGeo pgeo;

    @BeforeEach
    void setUp() {
        pgeo = new PGeo();
    }

    @Nested
    @DisplayName("area_triangulo")
    class AreaTriangulo {

        @Test
        @DisplayName("should calculate area of right triangle")
        void shouldCalculateAreaOfRightTriangle() {
            double[][] triangle = {
                {0, 0},
                {4, 0},
                {0, 3}
            };

            double area = pgeo.area_triangulo(triangle);

            assertEquals(6.0, area, DELTA);
        }

        @Test
        @DisplayName("should calculate area of equilateral triangle")
        void shouldCalculateAreaOfEquilateralTriangle() {
            double[][] triangle = {
                {0, 0},
                {2, 0},
                {1, Math.sqrt(3)}
            };

            double area = pgeo.area_triangulo(triangle);

            assertEquals(Math.sqrt(3), area, DELTA);
        }

        @Test
        @DisplayName("should calculate area with negative coordinates")
        void shouldCalculateAreaWithNegativeCoordinates() {
            double[][] triangle = {
                {-2, -2},
                {2, -2},
                {0, 2}
            };

            double area = pgeo.area_triangulo(triangle);

            assertEquals(8.0, area, DELTA);
        }

        @Test
        @DisplayName("should return positive area regardless of vertex order")
        void shouldReturnPositiveAreaRegardlessOfVertexOrder() {
            double[][] clockwise = {
                {0, 0},
                {0, 3},
                {4, 0}
            };
            double[][] counterClockwise = {
                {0, 0},
                {4, 0},
                {0, 3}
            };

            double area1 = pgeo.area_triangulo(clockwise);
            double area2 = pgeo.area_triangulo(counterClockwise);

            assertEquals(area1, area2, DELTA);
            assertTrue(area1 > 0);
        }

        @ParameterizedTest
        @MethodSource("provideTriangleTestCases")
        @DisplayName("should calculate correct area for various triangles")
        void shouldCalculateCorrectArea(double[][] triangle, double expectedArea) {
            double area = pgeo.area_triangulo(triangle);

            assertEquals(expectedArea, area, DELTA);
        }

        static Stream<Arguments> provideTriangleTestCases() {
            return Stream.of(
                Arguments.of(new double[][]{{0, 0}, {1, 0}, {0, 1}}, 0.5),
                Arguments.of(new double[][]{{0, 0}, {6, 0}, {3, 4}}, 12.0),
                Arguments.of(new double[][]{{-1, -1}, {1, -1}, {0, 1}}, 2.0),
                Arguments.of(new double[][]{{0, 0}, {10, 0}, {5, 8}}, 40.0)
            );
        }

        @Test
        @DisplayName("should reject null array")
        void shouldRejectNullArray() {
            assertThrows(
                IllegalArgumentException.class,
                () -> pgeo.area_triangulo(null)
            );
        }

        @Test
        @DisplayName("should reject array with wrong size")
        void shouldRejectArrayWithWrongSize() {
            double[][] invalid = {{0, 0}, {4, 0}};

            assertThrows(
                IllegalArgumentException.class,
                () -> pgeo.area_triangulo(invalid)
            );
        }

        @Test
        @DisplayName("should reject coincident points")
        void shouldRejectCoincidentPoints() {
            double[][] invalid = {
                {0, 0},
                {0, 0},
                {4, 3}
            };

            assertThrows(
                IllegalArgumentException.class,
                () -> pgeo.area_triangulo(invalid)
            );
        }

        @Test
        @DisplayName("should reject NaN coordinates")
        void shouldRejectNaNCoordinates() {
            double[][] invalid = {
                {Double.NaN, 0},
                {4, 0},
                {2, 3}
            };

            assertThrows(
                IllegalArgumentException.class,
                () -> pgeo.area_triangulo(invalid)
            );
        }

        @Test
        @DisplayName("should reject infinite coordinates")
        void shouldRejectInfiniteCoordinates() {
            double[][] invalid = {
                {0, 0},
                {Double.POSITIVE_INFINITY, 0},
                {2, 3}
            };

            assertThrows(
                IllegalArgumentException.class,
                () -> pgeo.area_triangulo(invalid)
            );
        }
    }

    @Nested
    @DisplayName("acima_abaixo")
    class AcimaAbaixo {

        @Test
        @DisplayName("should return 1 when point is above line")
        void shouldReturn1WhenPointIsAboveLine() {
            double[][] data = {
                {0, 0},
                {4, 0},
                {2, 3}
            };

            int result = pgeo.acima_abaixo(data);

            assertEquals(1, result);
        }

        @Test
        @DisplayName("should return -1 when point is below line")
        void shouldReturnMinus1WhenPointIsBelowLine() {
            double[][] data = {
                {0, 0},
                {4, 0},
                {2, -3}
            };

            int result = pgeo.acima_abaixo(data);

            assertEquals(-1, result);
        }

        @Test
        @DisplayName("should return 0 when point is on line")
        void shouldReturn0WhenPointIsOnLine() {
            double[][] data = {
                {0, 0},
                {4, 4},
                {2, 2}
            };

            int result = pgeo.acima_abaixo(data);

            assertEquals(0, result);
        }

        @Test
        @DisplayName("should handle vertical line with point to the left")
        void shouldHandleVerticalLineWithPointToLeft() {
            double[][] data = {
                {2, 0},
                {2, 4},
                {0, 2}
            };

            int result = pgeo.acima_abaixo(data);

            assertEquals(1, result);
        }

        @Test
        @DisplayName("should handle vertical line with point to the right")
        void shouldHandleVerticalLineWithPointToRight() {
            double[][] data = {
                {2, 0},
                {2, 4},
                {4, 2}
            };

            int result = pgeo.acima_abaixo(data);

            assertEquals(-1, result);
        }

        @Test
        @DisplayName("should handle horizontal line with point above")
        void shouldHandleHorizontalLineWithPointAbove() {
            double[][] data = {
                {0, 2},
                {4, 2},
                {2, 5}
            };

            int result = pgeo.acima_abaixo(data);

            assertEquals(1, result);
        }

        @Test
        @DisplayName("should handle horizontal line with point below")
        void shouldHandleHorizontalLineWithPointBelow() {
            double[][] data = {
                {0, 2},
                {4, 2},
                {2, 0}
            };

            int result = pgeo.acima_abaixo(data);

            assertEquals(-1, result);
        }

        @Test
        @DisplayName("should reject null array")
        void shouldRejectNullArray() {
            assertThrows(
                IllegalArgumentException.class,
                () -> pgeo.acima_abaixo(null)
            );
        }

        @Test
        @DisplayName("should reject coincident line points")
        void shouldRejectCoincidentLinePoints() {
            double[][] invalid = {
                {2, 2},
                {2, 2},
                {5, 5}
            };

            assertThrows(
                IllegalArgumentException.class,
                () -> pgeo.acima_abaixo(invalid)
            );
        }
    }

    @Nested
    @DisplayName("intersecao")
    class Intersecao {

        @Test
        @DisplayName("should return true when segment crosses line")
        void shouldReturnTrueWhenSegmentCrossesLine() {
            double[][] data = {
                {0, 0},
                {4, 4},
                {0, 4},
                {4, 0}
            };

            boolean result = pgeo.intersecao(data);

            assertTrue(result);
        }

        @Test
        @DisplayName("should return false when segment does not cross line")
        void shouldReturnFalseWhenSegmentDoesNotCrossLine() {
            double[][] data = {
                {0, 0},
                {1, 1},
                {5, 0},
                {5, 10}
            };

            boolean result = pgeo.intersecao(data);

            assertFalse(result);
        }

        @Test
        @DisplayName("should return true when segment endpoint touches line")
        void shouldReturnTrueWhenSegmentEndpointTouchesLine() {
            double[][] data = {
                {0, 0},
                {2, 2},
                {0, 2},
                {4, 2}
            };

            boolean result = pgeo.intersecao(data);

            assertTrue(result);
        }

        @Test
        @DisplayName("should return true when segment is on line")
        void shouldReturnTrueWhenSegmentIsOnLine() {
            double[][] data = {
                {1, 1},
                {3, 3},
                {0, 0},
                {4, 4}
            };

            boolean result = pgeo.intersecao(data);

            assertTrue(result);
        }

        @Test
        @DisplayName("should handle perpendicular intersection")
        void shouldHandlePerpendicularIntersection() {
            double[][] data = {
                {0, 2},
                {4, 2},
                {2, 0},
                {2, 4}
            };

            boolean result = pgeo.intersecao(data);

            assertTrue(result);
        }

        @Test
        @DisplayName("should reject null array")
        void shouldRejectNullArray() {
            assertThrows(
                IllegalArgumentException.class,
                () -> pgeo.intersecao(null)
            );
        }

        @Test
        @DisplayName("should reject array with wrong size")
        void shouldRejectArrayWithWrongSize() {
            double[][] invalid = {
                {0, 0},
                {4, 4},
                {0, 4}
            };

            assertThrows(
                IllegalArgumentException.class,
                () -> pgeo.intersecao(invalid)
            );
        }

        @Test
        @DisplayName("should reject coincident segment points")
        void shouldRejectCoincidentSegmentPoints() {
            double[][] invalid = {
                {2, 2},
                {2, 2},
                {0, 0},
                {4, 4}
            };

            assertThrows(
                IllegalArgumentException.class,
                () -> pgeo.intersecao(invalid)
            );
        }

        @Test
        @DisplayName("should reject coincident line points")
        void shouldRejectCoincidentLinePoints() {
            double[][] invalid = {
                {0, 0},
                {4, 4},
                {2, 2},
                {2, 2}
            };

            assertThrows(
                IllegalArgumentException.class,
                () -> pgeo.intersecao(invalid)
            );
        }
    }

    @Nested
    @DisplayName("ponto_intersecao")
    class PontoIntersecao {

        @Test
        @DisplayName("should find intersection point")
        void shouldFindIntersectionPoint() {
            double[][] data = {
                {0, 0},
                {4, 4},
                {0, 4},
                {4, 0}
            };

            double[][] result = pgeo.ponto_intersecao(data);

            assertNotNull(result);
            assertEquals(1, result.length);
            assertEquals(2.0, result[0][0], DELTA);
            assertEquals(2.0, result[0][1], DELTA);
        }

        @Test
        @DisplayName("should return null when no intersection")
        void shouldReturnNullWhenNoIntersection() {
            double[][] data = {
                {0, 0},
                {1, 1},
                {10, 0},
                {10, 10}
            };

            double[][] result = pgeo.ponto_intersecao(data);

            assertNull(result);
        }

        @Test
        @DisplayName("should find intersection at segment endpoint")
        void shouldFindIntersectionAtSegmentEndpoint() {
            double[][] data = {
                {0, 0},
                {4, 0},
                {4, -2},
                {4, 2}
            };

            double[][] result = pgeo.ponto_intersecao(data);

            assertNotNull(result);
            assertEquals(4.0, result[0][0], DELTA);
            assertEquals(0.0, result[0][1], DELTA);
        }

        @Test
        @DisplayName("should handle vertical line intersection")
        void shouldHandleVerticalLineIntersection() {
            double[][] data = {
                {0, 2},
                {6, 2},
                {3, 0},
                {3, 10}
            };

            double[][] result = pgeo.ponto_intersecao(data);

            assertNotNull(result);
            assertEquals(3.0, result[0][0], DELTA);
            assertEquals(2.0, result[0][1], DELTA);
        }

        @Test
        @DisplayName("should handle horizontal line intersection")
        void shouldHandleHorizontalLineIntersection() {
            double[][] data = {
                {2, 0},
                {2, 6},
                {0, 3},
                {10, 3}
            };

            double[][] result = pgeo.ponto_intersecao(data);

            assertNotNull(result);
            assertEquals(2.0, result[0][0], DELTA);
            assertEquals(3.0, result[0][1], DELTA);
        }

        @Test
        @DisplayName("should reject null array")
        void shouldRejectNullArray() {
            assertThrows(
                IllegalArgumentException.class,
                () -> pgeo.ponto_intersecao(null)
            );
        }
    }

    @Nested
    @DisplayName("area_P4")
    class AreaP4 {

        @Test
        @DisplayName("should calculate area of square")
        void shouldCalculateAreaOfSquare() {
            double[][] quad = {
                {0, 0},
                {4, 0},
                {4, 4},
                {0, 4}
            };

            double area = pgeo.area_P4(quad);

            assertEquals(16.0, area, DELTA);
        }

        @Test
        @DisplayName("should calculate area of rectangle")
        void shouldCalculateAreaOfRectangle() {
            double[][] quad = {
                {0, 0},
                {6, 0},
                {6, 4},
                {0, 4}
            };

            double area = pgeo.area_P4(quad);

            assertEquals(24.0, area, DELTA);
        }

        @Test
        @DisplayName("should calculate area with negative coordinates")
        void shouldCalculateAreaWithNegativeCoordinates() {
            double[][] quad = {
                {-2, -2},
                {2, -2},
                {2, 2},
                {-2, 2}
            };

            double area = pgeo.area_P4(quad);

            assertEquals(16.0, area, DELTA);
        }

        @Test
        @DisplayName("should calculate area of non-rectangular quadrilateral")
        void shouldCalculateAreaOfNonRectangularQuadrilateral() {
            double[][] quad = {
                {0, 0},
                {4, 0},
                {5, 3},
                {1, 3}
            };

            double area = pgeo.area_P4(quad);

            assertEquals(12.0, area, DELTA);
        }

        @ParameterizedTest
        @MethodSource("provideQuadrilateralTestCases")
        @DisplayName("should calculate correct area for various quadrilaterals")
        void shouldCalculateCorrectArea(double[][] quad, double expectedArea) {
            double area = pgeo.area_P4(quad);

            assertEquals(expectedArea, area, DELTA);
        }

        static Stream<Arguments> provideQuadrilateralTestCases() {
            return Stream.of(
                Arguments.of(new double[][]{{0, 0}, {5, 0}, {5, 5}, {0, 5}}, 25.0),
                Arguments.of(new double[][]{{0, 0}, {10, 0}, {10, 5}, {0, 5}}, 50.0),
                Arguments.of(new double[][]{{0, 0}, {3, 0}, {3, 3}, {0, 3}}, 9.0)
            );
        }

        @Test
        @DisplayName("should reject null array")
        void shouldRejectNullArray() {
            assertThrows(
                IllegalArgumentException.class,
                () -> pgeo.area_P4(null)
            );
        }

        @Test
        @DisplayName("should reject array with wrong size")
        void shouldRejectArrayWithWrongSize() {
            double[][] invalid = {
                {0, 0},
                {4, 0},
                {2, 3}
            };

            assertThrows(
                IllegalArgumentException.class,
                () -> pgeo.area_P4(invalid)
            );
        }

        @Test
        @DisplayName("should reject coincident points")
        void shouldRejectCoincidentPoints() {
            double[][] invalid = {
                {0, 0},
                {4, 0},
                {4, 0},
                {0, 4}
            };

            assertThrows(
                IllegalArgumentException.class,
                () -> pgeo.area_P4(invalid)
            );
        }
    }

    @Nested
    @DisplayName("Helper Methods")
    class HelperMethods {

        @Test
        @DisplayName("should calculate signed area")
        void shouldCalculateSignedArea() {
            double[][] counterClockwise = {
                {0, 0},
                {4, 0},
                {2, 3}
            };
            double[][] clockwise = {
                {0, 0},
                {2, 3},
                {4, 0}
            };

            double signedArea1 = pgeo.calculateTriangleSignedArea(counterClockwise);
            double signedArea2 = pgeo.calculateTriangleSignedArea(clockwise);

            assertTrue(signedArea1 > 0);
            assertTrue(signedArea2 < 0);
        }

        @Test
        @DisplayName("should detect collinear points")
        void shouldDetectCollinearPoints() {
            double[][] collinear = {
                {0, 0},
                {2, 2},
                {4, 4}
            };
            double[][] notCollinear = {
                {0, 0},
                {4, 0},
                {2, 3}
            };

            assertTrue(pgeo.arePointsCollinear(collinear));
            assertFalse(pgeo.arePointsCollinear(notCollinear));
        }

        @Test
        @DisplayName("should check if point is on line")
        void shouldCheckIfPointIsOnLine() {
            double[][] onLine = {
                {0, 0},
                {4, 4},
                {2, 2}
            };
            double[][] notOnLine = {
                {0, 0},
                {4, 4},
                {2, 3}
            };

            assertTrue(pgeo.isPointOnLine(onLine));
            assertFalse(pgeo.isPointOnLine(notOnLine));
        }

        @Test
        @DisplayName("should check if point is above line")
        void shouldCheckIfPointIsAboveLine() {
            double[][] above = {
                {0, 0},
                {4, 0},
                {2, 3}
            };
            double[][] below = {
                {0, 0},
                {4, 0},
                {2, -3}
            };

            assertTrue(pgeo.isPointAboveLine(above));
            assertFalse(pgeo.isPointAboveLine(below));
        }

        @Test
        @DisplayName("should check if point is below line")
        void shouldCheckIfPointIsBelowLine() {
            double[][] below = {
                {0, 0},
                {4, 0},
                {2, -3}
            };
            double[][] above = {
                {0, 0},
                {4, 0},
                {2, 3}
            };

            assertTrue(pgeo.isPointBelowLine(below));
            assertFalse(pgeo.isPointBelowLine(above));
        }

        @Test
        @DisplayName("should get position description")
        void shouldGetPositionDescription() {
            double[][] above = {{0, 0}, {4, 0}, {2, 3}};
            double[][] below = {{0, 0}, {4, 0}, {2, -3}};
            double[][] onLine = {{0, 0}, {4, 4}, {2, 2}};

            assertEquals("ABOVE", pgeo.getPointPositionDescription(above));
            assertEquals("BELOW", pgeo.getPointPositionDescription(below));
            assertEquals("ON_LINE", pgeo.getPointPositionDescription(onLine));
        }
    }
}