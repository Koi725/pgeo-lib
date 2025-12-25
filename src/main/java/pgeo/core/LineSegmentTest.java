package pgeo.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LineSegment")
class LineSegmentTest {

    private static final double DELTA = 1e-10;

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create segment with valid points")
        void shouldCreateSegmentWithValidPoints() {
            Point start = new Point(0, 0);
            Point end = new Point(4, 4);

            LineSegment segment = new LineSegment(start, end);

            assertEquals(start, segment.getStartPoint());
            assertEquals(end, segment.getEndPoint());
        }

        @Test
        @DisplayName("should reject null start point")
        void shouldRejectNullStartPoint() {
            Point end = new Point(4, 4);

            assertThrows(
                NullPointerException.class,
                () -> new LineSegment(null, end)
            );
        }

        @Test
        @DisplayName("should reject null end point")
        void shouldRejectNullEndPoint() {
            Point start = new Point(0, 0);

            assertThrows(
                NullPointerException.class,
                () -> new LineSegment(start, null)
            );
        }

        @Test
        @DisplayName("should reject coincident points")
        void shouldRejectCoincidentPoints() {
            Point p1 = new Point(3, 3);
            Point p2 = new Point(3, 3);

            assertThrows(
                IllegalArgumentException.class,
                () -> new LineSegment(p1, p2)
            );
        }
    }

    @Nested
    @DisplayName("Length Calculation")
    class LengthCalculation {

        @Test
        @DisplayName("should calculate horizontal length")
        void shouldCalculateHorizontalLength() {
            LineSegment segment = new LineSegment(
                new Point(0, 0),
                new Point(5, 0)
            );

            assertEquals(5.0, segment.getLength(), DELTA);
        }

        @Test
        @DisplayName("should calculate vertical length")
        void shouldCalculateVerticalLength() {
            LineSegment segment = new LineSegment(
                new Point(0, 0),
                new Point(0, 7)
            );

            assertEquals(7.0, segment.getLength(), DELTA);
        }

        @Test
        @DisplayName("should calculate diagonal length")
        void shouldCalculateDiagonalLength() {
            LineSegment segment = new LineSegment(
                new Point(0, 0),
                new Point(3, 4)
            );

            assertEquals(5.0, segment.getLength(), DELTA);
        }
    }

    @Nested
    @DisplayName("Midpoint Calculation")
    class MidpointCalculation {

        @Test
        @DisplayName("should calculate midpoint correctly")
        void shouldCalculateMidpointCorrectly() {
            LineSegment segment = new LineSegment(
                new Point(0, 0),
                new Point(4, 6)
            );

            Point midpoint = segment.getMidpoint();

            assertEquals(2.0, midpoint.getX(), DELTA);
            assertEquals(3.0, midpoint.getY(), DELTA);
        }

        @Test
        @DisplayName("should calculate midpoint with negative coordinates")
        void shouldCalculateMidpointWithNegativeCoordinates() {
            LineSegment segment = new LineSegment(
                new Point(-4, -2),
                new Point(4, 2)
            );

            Point midpoint = segment.getMidpoint();

            assertEquals(0.0, midpoint.getX(), DELTA);
            assertEquals(0.0, midpoint.getY(), DELTA);
        }
    }

    @Nested
    @DisplayName("Line Intersection")
    class LineIntersection {

        @Test
        @DisplayName("should detect intersection when segment crosses line")
        void shouldDetectIntersectionWhenSegmentCrossesLine() {
            LineSegment segment = new LineSegment(
                new Point(0, 0),
                new Point(4, 4)
            );
            Line line = new Line(
                new Point(0, 4),
                new Point(4, 0)
            );

            assertTrue(segment.intersectsLine(line));
        }

        @Test
        @DisplayName("should detect no intersection when segment does not cross line")
        void shouldDetectNoIntersectionWhenSegmentDoesNotCrossLine() {
            LineSegment segment = new LineSegment(
                new Point(0, 0),
                new Point(1, 1)
            );
            Line line = new Line(
                new Point(5, 0),
                new Point(5, 10)
            );

            assertFalse(segment.intersectsLine(line));
        }

        @Test
        @DisplayName("should detect intersection when endpoint is on line")
        void shouldDetectIntersectionWhenEndpointIsOnLine() {
            LineSegment segment = new LineSegment(
                new Point(0, 0),
                new Point(2, 2)
            );
            Line line = new Line(
                new Point(0, 2),
                new Point(4, 2)
            );

            assertTrue(segment.intersectsLine(line));
        }

        @Test
        @DisplayName("should reject null line")
        void shouldRejectNullLine() {
            LineSegment segment = new LineSegment(
                new Point(0, 0),
                new Point(4, 4)
            );

            assertThrows(
                NullPointerException.class,
                () -> segment.intersectsLine(null)
            );
        }
    }

    @Nested
    @DisplayName("Intersection Point Calculation")
    class IntersectionPointCalculation {

        @Test
        @DisplayName("should find intersection point")
        void shouldFindIntersectionPoint() {
            LineSegment segment = new LineSegment(
                new Point(0, 0),
                new Point(4, 4)
            );
            Line line = new Line(
                new Point(0, 4),
                new Point(4, 0)
            );

            Point intersection = segment.findIntersectionWithLine(line);

            assertNotNull(intersection);
            assertEquals(2.0, intersection.getX(), DELTA);
            assertEquals(2.0, intersection.getY(), DELTA);
        }

        @Test
        @DisplayName("should return null when no intersection")
        void shouldReturnNullWhenNoIntersection() {
            LineSegment segment = new LineSegment(
                new Point(0, 0),
                new Point(1, 1)
            );
            Line line = new Line(
                new Point(10, 0),
                new Point(10, 10)
            );

            Point intersection = segment.findIntersectionWithLine(line);

            assertNull(intersection);
        }

        @Test
        @DisplayName("should find intersection at segment endpoint")
        void shouldFindIntersectionAtSegmentEndpoint() {
            LineSegment segment = new LineSegment(
                new Point(0, 0),
                new Point(4, 0)
            );
            Line line = new Line(
                new Point(4, -2),
                new Point(4, 2)
            );

            Point intersection = segment.findIntersectionWithLine(line);

            assertNotNull(intersection);
            assertEquals(4.0, intersection.getX(), DELTA);
            assertEquals(0.0, intersection.getY(), DELTA);
        }

        @Test
        @DisplayName("should handle vertical line intersection")
        void shouldHandleVerticalLineIntersection() {
            LineSegment segment = new LineSegment(
                new Point(0, 2),
                new Point(6, 2)
            );
            Line line = new Line(
                new Point(3, 0),
                new Point(3, 10)
            );

            Point intersection = segment.findIntersectionWithLine(line);

            assertNotNull(intersection);
            assertEquals(3.0, intersection.getX(), DELTA);
            assertEquals(2.0, intersection.getY(), DELTA);
        }

        @Test
        @DisplayName("should handle horizontal line intersection")
        void shouldHandleHorizontalLineIntersection() {
            LineSegment segment = new LineSegment(
                new Point(2, 0),
                new Point(2, 6)
            );
            Line line = new Line(
                new Point(0, 3),
                new Point(10, 3)
            );

            Point intersection = segment.findIntersectionWithLine(line);

            assertNotNull(intersection);
            assertEquals(2.0, intersection.getX(), DELTA);
            assertEquals(3.0, intersection.getY(), DELTA);
        }
    }

    @Nested
    @DisplayName("Factory Method")
    class FactoryMethod {

        @Test
        @DisplayName("should create segment from valid array")
        void shouldCreateSegmentFromValidArray() {
            double[][] coords = {
                {1, 2},
                {5, 6}
            };

            LineSegment segment = LineSegment.fromArray(coords);

            assertEquals(1, segment.getStartPoint().getX(), DELTA);
            assertEquals(2, segment.getStartPoint().getY(), DELTA);
            assertEquals(5, segment.getEndPoint().getX(), DELTA);
            assertEquals(6, segment.getEndPoint().getY(), DELTA);
        }

        @Test
        @DisplayName("should reject null array")
        void shouldRejectNullArray() {
            assertThrows(
                IllegalArgumentException.class,
                () -> LineSegment.fromArray(null)
            );
        }

        @Test
        @DisplayName("should reject array with wrong size")
        void shouldRejectArrayWithWrongSize() {
            double[][] coords = {{1, 2}};

            assertThrows(
                IllegalArgumentException.class,
                () -> LineSegment.fromArray(coords)
            );
        }
    }

    @Nested
    @DisplayName("Conversion")
    class Conversion {

        @Test
        @DisplayName("should convert to line")
        void shouldConvertToLine() {
            LineSegment segment = new LineSegment(
                new Point(0, 0),
                new Point(5, 5)
            );

            Line line = segment.toLine();

            assertEquals(segment.getStartPoint(), line.getPointA());
            assertEquals(segment.getEndPoint(), line.getPointB());
        }

        @Test
        @DisplayName("should convert to array")
        void shouldConvertToArray() {
            LineSegment segment = new LineSegment(
                new Point(1, 2),
                new Point(3, 4)
            );

            double[][] array = segment.toArray();

            assertEquals(2, array.length);
            assertEquals(1, array[0][0], DELTA);
            assertEquals(2, array[0][1], DELTA);
            assertEquals(3, array[1][0], DELTA);
            assertEquals(4, array[1][1], DELTA);
        }
    }

    @Nested
    @DisplayName("Equality")
    class Equality {

        @Test
        @DisplayName("should be equal to segment with same points")
        void shouldBeEqualToSegmentWithSamePoints() {
            LineSegment s1 = new LineSegment(
                new Point(0, 0),
                new Point(5, 5)
            );
            LineSegment s2 = new LineSegment(
                new Point(0, 0),
                new Point(5, 5)
            );

            assertEquals(s1, s2);
            assertEquals(s1.hashCode(), s2.hashCode());
        }

        @Test
        @DisplayName("should not be equal to segment with different points")
        void shouldNotBeEqualToSegmentWithDifferentPoints() {
            LineSegment s1 = new LineSegment(
                new Point(0, 0),
                new Point(5, 5)
            );
            LineSegment s2 = new LineSegment(
                new Point(0, 0),
                new Point(5, 6)
            );

            assertNotEquals(s1, s2);
        }
    }
}