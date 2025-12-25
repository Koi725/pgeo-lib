# Contributing to PGeo Library

Thank you for your interest in contributing to PGeo Library!

## Development Setup

### Prerequisites

```bash
# Run the requirements installer
./install-requirements.sh

# Or install manually
# Java 17+, Maven 3.8+, Git 2.30+
```

### Getting Started

```bash
# Clone the repository
git clone https://github.com/yourusername/pgeo-lib.git
cd pgeo-lib

# Build the project
./build.sh build

# Run tests
./build.sh test
```

## Code Style

### Java Conventions

- Use 4 spaces for indentation
- Maximum line length: 120 characters
- Use meaningful variable and method names
- Follow Java naming conventions (camelCase for methods/variables, PascalCase for classes)

### Clean Code Principles

- Single Responsibility Principle
- Keep methods small and focused
- Self-documenting code (minimal comments)
- Immutable objects where possible
- Defensive programming

### Example

```java
// Good
public double calculateArea() {
    return Math.abs(calculateSignedArea());
}

// Bad
public double calc() {
    return Math.abs(csa());
}
```

## Testing

### Test-Driven Development (TDD)

1. Write a failing test
2. Write minimal code to pass
3. Refactor

### Test Naming Convention

```java
@Test
@DisplayName("should calculate area of right triangle")
void shouldCalculateAreaOfRightTriangle() {
    // Arrange
    // Act
    // Assert
}
```

### Running Tests

```bash
# Run all tests
./build.sh test

# Run specific test class
mvn test -Dtest=PGeoTest

# Run with coverage
./build.sh coverage
```

## Git Workflow

### Branch Naming

- `feat/feature-name` - New features
- `fix/bug-description` - Bug fixes
- `refactor/description` - Code refactoring
- `test/test-description` - Test additions
- `docs/description` - Documentation

### Commit Messages

Follow conventional commits:

```
type(scope): description

feat(core): add Triangle class with area calculation
fix(geometry): handle division by zero in intersection
test(pgeo): add edge case tests for collinear points
docs(readme): update installation instructions
refactor(client): extract input validation
```

### Pull Request Process

1. Create feature branch from `main`
2. Make changes with tests
3. Ensure all tests pass
4. Update documentation if needed
5. Submit PR with clear description

## Project Structure

```
pgeo-lib/
├── src/main/java/pgeo/
│   ├── core/           # Domain objects
│   ├── geometry/       # Main API
│   ├── util/           # Utilities
│   └── client/         # CLI interface
├── src/test/java/pgeo/ # Unit tests
├── build.sh            # Build script
└── pom.xml             # Maven config
```

## Questions?

Open an issue or contact the maintainers.
