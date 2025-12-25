#═══════════════════════════════════════════════════════════════════════════════
# PGeo Library - Makefile
# University of Aveiro - ESTGA
#═══════════════════════════════════════════════════════════════════════════════

.PHONY: all build clean compile test package run install docs help

PROJECT_NAME := pgeo-lib
VERSION := 1.0.0
JAR_FILE := target/$(PROJECT_NAME)-$(VERSION).jar

#───────────────────────────────────────────────────────────────────────────────
# Default Target
#───────────────────────────────────────────────────────────────────────────────

all: build

#───────────────────────────────────────────────────────────────────────────────
# Build Targets
#───────────────────────────────────────────────────────────────────────────────

build: clean compile test package
	@echo "✓ Build completed successfully!"

quick: clean compile package
	@echo "✓ Quick build completed!"

clean:
	@echo "▶ Cleaning..."
	@mvn clean -q

compile:
	@echo "▶ Compiling..."
	@mvn compile -q

test:
	@echo "▶ Testing..."
	@mvn test

package:
	@echo "▶ Packaging..."
	@mvn package -q -DskipTests

install:
	@echo "▶ Installing..."
	@mvn install -q -DskipTests

#───────────────────────────────────────────────────────────────────────────────
# Run Target
#───────────────────────────────────────────────────────────────────────────────

run: $(JAR_FILE)
	@echo "▶ Running..."
	@java -jar $(JAR_FILE)

$(JAR_FILE):
	@$(MAKE) build

#───────────────────────────────────────────────────────────────────────────────
# Documentation
#───────────────────────────────────────────────────────────────────────────────

docs:
	@echo "▶ Generating docs..."
	@mvn javadoc:javadoc -q
	@echo "✓ Docs: target/site/apidocs/index.html"

#───────────────────────────────────────────────────────────────────────────────
# Help
#───────────────────────────────────────────────────────────────────────────────

help:
	@echo "PGeo Library - Make Targets"
	@echo ""
	@echo "  make build    - Full build (clean, compile, test, package)"
	@echo "  make quick    - Quick build (skip tests)"
	@echo "  make clean    - Remove build artifacts"
	@echo "  make compile  - Compile source code"
	@echo "  make test     - Run unit tests"
	@echo "  make package  - Create JAR file"
	@echo "  make install  - Install to local Maven repo"
	@echo "  make run      - Run the application"
	@echo "  make docs     - Generate Javadoc"
	@echo "  make help     - Show this help"