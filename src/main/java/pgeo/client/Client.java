package pgeo.client;

import pgeo.geometry.PGeo;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {

    private static final String SEPARATOR = "═".repeat(50);
    private static final String THIN_SEPARATOR = "─".repeat(50);

    private final PGeo pgeo;
    private final Scanner scanner;

    public Client() {
        this.pgeo = new PGeo();
        this.scanner = new Scanner(System.in);
    }

    public Client(PGeo pgeo, Scanner scanner) {
        this.pgeo = pgeo;
        this.scanner = scanner;
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    public void run() {
        printWelcome();

        try {
            executeTriangleOperations();
            executeQuadrilateralOperations();
        } catch (Exception e) {
            printError("An unexpected error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }

        printGoodbye();
    }

    private void executeTriangleOperations() {
        printSectionHeader("TRIANGLE OPERATIONS");

        double[][] triangleCoords = readTriangleCoordinates();

        if (triangleCoords == null) {
            printError("Invalid triangle coordinates. Skipping triangle operations.");
            return;
        }

        calculateAndPrintTriangleArea(triangleCoords);
        calculateAndPrintPointPosition(triangleCoords);
    }

    private void executeQuadrilateralOperations() {
        printSectionHeader("QUADRILATERAL OPERATIONS");

        double[][] quadCoords = readQuadrilateralCoordinates();

        if (quadCoords == null) {
            printError("Invalid quadrilateral coordinates. Skipping quadrilateral operations.");
            return;
        }

        calculateAndPrintQuadrilateralArea(quadCoords);
        calculateAndPrintIntersection(quadCoords);
    }

    private double[][] readTriangleCoordinates() {
        printInfo("Enter 3 coordinates (x, y) for the triangle:");
        return readCoordinates(3);
    }

    private double[][] readQuadrilateralCoordinates() {
        printInfo("Enter 4 coordinates (x, y) for the quadrilateral:");
        printInfo("(First 2 points define a line segment, last 2 define a line)");
        return readCoordinates(4);
    }

    private double[][] readCoordinates(int count) {
        double[][] coordinates = new double[count][2];

        for (int i = 0; i < count; i++) {
            double[] point = readSinglePoint(i + 1);

            if (point == null) {
                return null;
            }

            coordinates[i] = point;
        }

        if (!validateCoordinates(coordinates)) {
            return null;
        }

        return coordinates;
    }

    private double[] readSinglePoint(int pointNumber) {
        System.out.printf("  Point %d:%n", pointNumber);

        Double x = readCoordinate("    x: ");
        if (x == null) return null;

        Double y = readCoordinate("    y: ");
        if (y == null) return null;

        return new double[]{x, y};
    }

    private Double readCoordinate(String prompt) {
        int maxAttempts = 3;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            System.out.print(prompt);

            try {
                double value = scanner.nextDouble();

                if (Double.isNaN(value) || Double.isInfinite(value)) {
                    printWarning("Invalid number. Please enter a valid coordinate.");
                    continue;
                }

                return value;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                if (attempt < maxAttempts) {
                    printWarning(String.format("Invalid input. Attempt %d/%d. Please enter a number.", 
                        attempt, maxAttempts));
                } else {
                    printError("Maximum attempts reached.");
                }
            }
        }

        return null;
    }

    private boolean validateCoordinates(double[][] coordinates) {
        for (int i = 0; i < coordinates.length; i++) {
            for (int j = i + 1; j < coordinates.length; j++) {
                if (arePointsCoincident(coordinates[i], coordinates[j])) {
                    printWarning(String.format("Points %d and %d are coincident.", i + 1, j + 1));
                    return false;
                }
            }
        }
        return true;
    }

    private boolean arePointsCoincident(double[] p1, double[] p2) {
        return Double.compare(p1[0], p2[0]) == 0 
            && Double.compare(p1[1], p2[1]) == 0;
    }

    private void calculateAndPrintTriangleArea(double[][] coords) {
        printSubsection("Triangle Area");

        try {
            double area = pgeo.area_triangulo(coords);
            printResult(String.format("Area: %.4f square units", area));
        } catch (IllegalArgumentException e) {
            printError("Cannot calculate area: " + e.getMessage());
        }
    }

    private void calculateAndPrintPointPosition(double[][] coords) {
        printSubsection("Point Position Relative to Line");

        printInfo(String.format("Line defined by: (%.2f, %.2f) -> (%.2f, %.2f)",
            coords[0][0], coords[0][1], coords[1][0], coords[1][1]));
        printInfo(String.format("Test point: (%.2f, %.2f)", coords[2][0], coords[2][1]));

        try {
            int position = pgeo.acima_abaixo(coords);
            String positionDescription = getPositionDescription(position);
            printResult(String.format("Position: %s", positionDescription));
        } catch (IllegalArgumentException e) {
            printError("Cannot determine position: " + e.getMessage());
        }
    }

    private void calculateAndPrintQuadrilateralArea(double[][] coords) {
        printSubsection("Quadrilateral Area");

        try {
            double area = pgeo.area_P4(coords);
            printResult(String.format("Area: %.4f square units", area));
        } catch (IllegalArgumentException e) {
            printError("Cannot calculate area: " + e.getMessage());
        }
    }

    private void calculateAndPrintIntersection(double[][] coords) {
        printSubsection("Line Segment / Line Intersection");

        printInfo(String.format("Segment: (%.2f, %.2f) -> (%.2f, %.2f)",
            coords[0][0], coords[0][1], coords[1][0], coords[1][1]));
        printInfo(String.format("Line: (%.2f, %.2f) -> (%.2f, %.2f)",
            coords[2][0], coords[2][1], coords[3][0], coords[3][1]));

        try {
            boolean intersects = pgeo.intersecao(coords);

            if (intersects) {
                printResult("Intersection: YES");

                double[][] intersectionPoint = pgeo.ponto_intersecao(coords);

                if (intersectionPoint != null) {
                    printResult(String.format("Intersection point: (%.4f, %.4f)",
                        intersectionPoint[0][0], intersectionPoint[0][1]));
                } else {
                    printInfo("Segment lies on the line (infinite intersection points)");
                }
            } else {
                printResult("Intersection: NO");
            }
        } catch (IllegalArgumentException e) {
            printError("Cannot calculate intersection: " + e.getMessage());
        }
    }

    private String getPositionDescription(int position) {
        switch (position) {
            case 1:
                return "ABOVE (left of directed line)";
            case -1:
                return "BELOW (right of directed line)";
            case 0:
                return "ON THE LINE";
            default:
                return "UNKNOWN";
        }
    }

    private void printWelcome() {
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println("     PGeo - Computational Geometry Library");
        System.out.println("     University of Aveiro - ESTGA");
        System.out.println(SEPARATOR);
        System.out.println();
    }

    private void printGoodbye() {
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println("     Thank you for using PGeo!");
        System.out.println(SEPARATOR);
        System.out.println();
    }

    private void printSectionHeader(String title) {
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.printf("  %s%n", title);
        System.out.println(SEPARATOR);
        System.out.println();
    }

    private void printSubsection(String title) {
        System.out.println();
        System.out.println(THIN_SEPARATOR);
        System.out.printf("  %s%n", title);
        System.out.println(THIN_SEPARATOR);
    }

    private void printInfo(String message) {
        System.out.printf("  ℹ %s%n", message);
    }

    private void printResult(String message) {
        System.out.printf("  ✓ %s%n", message);
    }

    private void printWarning(String message) {
        System.out.printf("  ⚠ %s%n", message);
    }

    private void printError(String message) {
        System.out.printf("  ✗ %s%n", message);
    }
}