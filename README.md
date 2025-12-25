# PGeo Library

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![Tests](https://img.shields.io/badge/Tests-210%20Passed-brightgreen.svg)]()
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

A robust computational geometry primitives library implementing fundamental geometric operations with clean architecture, TDD methodology, and comprehensive test coverage.

## 🎯 Features

- **Triangle Area Calculation** - Using determinant formula for numerical stability
- **Point-Line Position Detection** - Determine if a point lies above, below, or on a line
- **Line Segment Intersection** - Check if a segment intersects with a line
- **Intersection Point Calculation** - Compute exact intersection coordinates
- **Quadrilateral Area** - Calculate area of 4-vertex polygons using shoelace algorithm

## 🏗️ Architecture

```
pgeo-lib/
├── src/main/java/pgeo/
│   ├── core/           # Domain objects (Point, Triangle, Line, etc.)
│   ├── geometry/       # Main PGeo API
│   ├── util/           # Validation utilities
│   └── client/         # Interactive CLI interface
├── src/test/java/pgeo/ # Comprehensive unit tests (210 tests)
├── build.sh            # Build automation script
├── install-requirements.sh
└── pom.xml
```

## 🚀 Quick Start

### Prerequisites

- Java JDK 17+
- Maven 3.8+

### Installation

```bash
# Clone the repository
git clone https://github.com/yourusername/pgeo-lib.git
cd pgeo-lib

# Install requirements (if needed)
./install-requirements.sh

# Build the project
./build.sh build

# Run the application
./build.sh run
```

### Build Commands

| Command            | Description                                |
| ------------------ | ------------------------------------------ |
| `./build.sh build` | Full build (clean, compile, test, package) |
| `./build.sh quick` | Quick build (skip tests)                   |
| `./build.sh test`  | Run unit tests                             |
| `./build.sh run`   | Run the application                        |
| `./build.sh clean` | Clean build artifacts                      |
| `./build.sh docs`  | Generate Javadoc                           |
| `./build.sh help`  | Show all commands                          |

## 📖 API Usage

```java
PGeo geo = new PGeo();

// Triangle area
double[][] triangle = {{0, 0}, {4, 0}, {2, 3}};
double area = geo.area_triangulo(triangle);
// Result: 6.0

// Point position relative to line
double[][] data = {{0, 0}, {4, 0}, {2, 3}};
int position = geo.acima_abaixo(data);
// Result: 1 (above), -1 (below), 0 (on line)

// Check intersection
double[][] segments = {{0, 0}, {4, 4}, {0, 4}, {4, 0}};
boolean intersects = geo.intersecao(segments);
// Result: true

// Get intersection point
double[][] point = geo.ponto_intersecao(segments);
// Result: {{2.0, 2.0}}

// Quadrilateral area
double[][] quad = {{0, 0}, {4, 0}, {4, 3}, {0, 3}};
double quadArea = geo.area_P4(quad);
// Result: 12.0
```

## 🧪 Testing

This project follows **Test-Driven Development (TDD)** methodology with:

- **210 unit tests** covering all functionality
- Input domain partitioning
- Boundary value analysis
- Edge case coverage

```bash
# Run all tests
./build.sh test

# Run specific test class
mvn test -Dtest=PGeoTest

# Run with verbose output
mvn test
```

## 📐 Mathematical Background

### Triangle Area (Determinant Formula)

```
2 × Area = |ax  ay  1|
           |bx  by  1| = ax(by - cy) + bx(cy - ay) + cx(ay - by)
           |cx  cy  1|
```

### Point Position (Signed Area)

- `Area > 0` → Point is **above** (left of directed line)
- `Area < 0` → Point is **below** (right of directed line)
- `Area = 0` → Point is **on the line**

### Polygon Area (Shoelace Algorithm)

```
Area = ½|Σ(xi × yi+1 - xi+1 × yi)|
```

## Project Structure

```
src/
├── main/java/pgeo/
│   ├── core/
│   │   ├── Point.java          # Immutable 2D point
│   │   ├── Triangle.java       # Triangle with area calculation
│   │   ├── Line.java           # Infinite line representation
│   │   ├── LineSegment.java    # Bounded line segment
│   │   └── Polygon.java        # N-vertex polygon
│   ├── geometry/
│   │   └── PGeo.java           # Main API class
│   ├── util/
│   │   └── GeometryValidator.java
│   └── client/
│       └── Client.java         # CLI interface
└── test/java/pgeo/
    ├── core/
    │   ├── PointTest.java
    │   ├── TriangleTest.java
    │   ├── LineSegmentTest.java
    │   └── PolygonTest.java
    ├── geometry/
    │   └── PGeoTest.java
    ├── util/
    │   └── GeometryValidatorTest.java
    └── client/
        └── ClientTest.java
```
