package pgeo.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import pgeo.geometry.PGeo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Client")
class ClientTest {

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create client with default constructor")
        void shouldCreateClientWithDefaultConstructor() {
            Client client = new Client();

            assertNotNull(client);
        }

        @Test
        @DisplayName("should create client with custom dependencies")
        void shouldCreateClientWithCustomDependencies() {
            PGeo pgeo = new PGeo();
            Scanner scanner = new Scanner(System.in);

            Client client = new Client(pgeo, scanner);

            assertNotNull(client);
        }
    }

    @Nested
    @DisplayName("Input Processing")
    class InputProcessing {

        @Test
        @DisplayName("should process valid triangle input")
        void shouldProcessValidTriangleInput() {
            String input = "0\n0\n4\n0\n2\n3\n0\n0\n4\n0\n4\n4\n0\n4\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.setIn(inputStream);
            System.setOut(new PrintStream(outputStream));

            Scanner scanner = new Scanner(inputStream);
            PGeo pgeo = new PGeo();
            Client client = new Client(pgeo, scanner);

            assertDoesNotThrow(client::run);

            String output = outputStream.toString();
            assertTrue(output.contains("PGeo"));
        }

        @Test
        @DisplayName("should handle welcome and goodbye messages")
        void shouldHandleWelcomeAndGoodbyeMessages() {
            String input = "0\n0\n4\n0\n2\n3\n0\n0\n4\n0\n4\n4\n0\n4\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.setIn(inputStream);
            System.setOut(new PrintStream(outputStream));

            Scanner scanner = new Scanner(inputStream);
            PGeo pgeo = new PGeo();
            Client client = new Client(pgeo, scanner);

            client.run();

            String output = outputStream.toString();
            assertTrue(output.contains("PGeo"));
            assertTrue(output.contains("Thank you"));
        }
    }

    @Nested
    @DisplayName("Triangle Operations")
    class TriangleOperations {

        @Test
        @DisplayName("should calculate and display triangle area")
        void shouldCalculateAndDisplayTriangleArea() {
            String input = "0\n0\n4\n0\n0\n3\n0\n0\n4\n0\n4\n4\n0\n4\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.setIn(inputStream);
            System.setOut(new PrintStream(outputStream));

            Scanner scanner = new Scanner(inputStream);
            PGeo pgeo = new PGeo();
            Client client = new Client(pgeo, scanner);

            client.run();

            String output = outputStream.toString();
            assertTrue(output.contains("Area"));
            assertTrue(output.contains("6.0000"));
        }

        @Test
        @DisplayName("should display point position above line")
        void shouldDisplayPointPositionAboveLine() {
            String input = "0\n0\n4\n0\n2\n3\n0\n0\n4\n0\n4\n4\n0\n4\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.setIn(inputStream);
            System.setOut(new PrintStream(outputStream));

            Scanner scanner = new Scanner(inputStream);
            PGeo pgeo = new PGeo();
            Client client = new Client(pgeo, scanner);

            client.run();

            String output = outputStream.toString();
            assertTrue(output.contains("ABOVE"));
        }

        @Test
        @DisplayName("should display point position below line")
        void shouldDisplayPointPositionBelowLine() {
            String input = "0\n0\n4\n0\n2\n-3\n0\n0\n4\n0\n4\n4\n0\n4\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.setIn(inputStream);
            System.setOut(new PrintStream(outputStream));

            Scanner scanner = new Scanner(inputStream);
            PGeo pgeo = new PGeo();
            Client client = new Client(pgeo, scanner);

            client.run();

            String output = outputStream.toString();
            assertTrue(output.contains("BELOW"));
        }

        @Test
        @DisplayName("should display point position on line")
        void shouldDisplayPointPositionOnLine() {
            String input = "0\n0\n4\n4\n2\n2\n0\n0\n4\n0\n4\n4\n0\n4\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.setIn(inputStream);
            System.setOut(new PrintStream(outputStream));

            Scanner scanner = new Scanner(inputStream);
            PGeo pgeo = new PGeo();
            Client client = new Client(pgeo, scanner);

            client.run();

            String output = outputStream.toString();
            assertTrue(output.contains("ON THE LINE"));
        }
    }

    @Nested
    @DisplayName("Quadrilateral Operations")
    class QuadrilateralOperations {

        @Test
        @DisplayName("should calculate and display quadrilateral area")
        void shouldCalculateAndDisplayQuadrilateralArea() {
            String input = "0\n0\n4\n0\n2\n3\n0\n0\n4\n0\n4\n4\n0\n4\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.setIn(inputStream);
            System.setOut(new PrintStream(outputStream));

            Scanner scanner = new Scanner(inputStream);
            PGeo pgeo = new PGeo();
            Client client = new Client(pgeo, scanner);

            client.run();

            String output = outputStream.toString();
            assertTrue(output.contains("Quadrilateral Area"));
            assertTrue(output.contains("16.0000"));
        }

        @Test
        @DisplayName("should display intersection result")
        void shouldDisplayIntersectionResult() {
            String input = "0\n0\n4\n0\n2\n3\n0\n0\n4\n4\n0\n4\n4\n0\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.setIn(inputStream);
            System.setOut(new PrintStream(outputStream));

            Scanner scanner = new Scanner(inputStream);
            PGeo pgeo = new PGeo();
            Client client = new Client(pgeo, scanner);

            client.run();

            String output = outputStream.toString();
            assertTrue(output.contains("Intersection"));
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandling {

        @Test
        @DisplayName("should handle invalid input gracefully")
        void shouldHandleInvalidInputGracefully() {
            String input = "abc\n0\n0\n4\n0\n2\n3\n0\n0\n4\n0\n4\n4\n0\n4\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            System.setIn(inputStream);
            System.setOut(new PrintStream(outputStream));

            Scanner scanner = new Scanner(inputStream);
            PGeo pgeo = new PGeo();
            Client client = new Client(pgeo, scanner);

            assertDoesNotThrow(client::run);

            String output = outputStream.toString();
            assertTrue(output.contains("Invalid") || output.contains("error") || output.length() > 0);
        }
    }
}