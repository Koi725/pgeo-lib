# PGeo Quick Start Guide

Get up and running in 2 minutes! 🚀

## Prerequisites

Make sure you have installed:

- Java 17+ (`java -version`)
- Maven 3.8+ (`mvn -version`)

Don't have them? Run:

```bash
./install-requirements.sh
```

## Build & Run

```bash
# 1. Build the project
./build.sh build

# 2. Run the application
./build.sh run
```

## Example Session

```
══════════════════════════════════════════════════
  TRIANGLE OPERATIONS
══════════════════════════════════════════════════

  ℹ Enter 3 coordinates (x, y) for the triangle:
  Point 1:
    x: 0
    y: 0
  Point 2:
    x: 4
    y: 0
  Point 3:
    x: 2
    y: 3

──────────────────────────────────────────────────
  Triangle Area
──────────────────────────────────────────────────
  ✓ Area: 6.0000 square units

──────────────────────────────────────────────────
  Point Position Relative to Line
──────────────────────────────────────────────────
  ℹ Line defined by: (0.00, 0.00) -> (4.00, 0.00)
  ℹ Test point: (2.00, 3.00)
  ✓ Position: ABOVE (left of directed line)
```

## Using as a Library

```java
import pgeo.geometry.PGeo;

public class MyApp {
    public static void main(String[] args) {
        PGeo geo = new PGeo();

        // Calculate triangle area
        double[][] triangle = {{0, 0}, {4, 0}, {2, 3}};
        System.out.println("Area: " + geo.area_triangulo(triangle));

        // Check point position
        int pos = geo.acima_abaixo(triangle);
        System.out.println("Position: " + (pos > 0 ? "ABOVE" : pos < 0 ? "BELOW" : "ON LINE"));
    }
}
```

## Available Methods

| Method                | Description            | Input                            |
| --------------------- | ---------------------- | -------------------------------- |
| `area_triangulo(A)`   | Triangle area          | 3 points                         |
| `acima_abaixo(A)`     | Point position         | 2 line points + 1 test point     |
| `intersecao(A)`       | Check intersection     | 2 segment points + 2 line points |
| `ponto_intersecao(A)` | Get intersection point | 2 segment points + 2 line points |
| `area_P4(A)`          | Quadrilateral area     | 4 points                         |

## Build Commands

```bash
./build.sh build    # Full build with tests
./build.sh quick    # Fast build, skip tests
./build.sh test     # Run tests only
./build.sh run      # Run application
./build.sh clean    # Clean artifacts
./build.sh help     # Show all commands
```

## Need Help?

- Check [README.md](README.md) for detailed documentation
- Check [CONTRIBUTING.md](CONTRIBUTING.md) for development guidelines
- Run `./build.sh help` for build commands
