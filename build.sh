#!/bin/bash

#═══════════════════════════════════════════════════════════════════════════════
# PGeo Library - Build Script
# University of Aveiro - ESTGA
# Software Engineering - Practical Assignment
#═══════════════════════════════════════════════════════════════════════════════

set -e

#───────────────────────────────────────────────────────────────────────────────
# Configuration
#───────────────────────────────────────────────────────────────────────────────

PROJECT_NAME="pgeo-lib"
VERSION="1.0.0"
JAR_FILE="target/${PROJECT_NAME}-${VERSION}.jar"
MAIN_CLASS="pgeo.client.Client"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

#───────────────────────────────────────────────────────────────────────────────
# Helper Functions
#───────────────────────────────────────────────────────────────────────────────

print_banner() {
    echo -e "${CYAN}"
    echo "╔═══════════════════════════════════════════════════════════════════╗"
    echo "║                    PGeo Library Build System                      ║"
    echo "║                   University of Aveiro - ESTGA                    ║"
    echo "╚═══════════════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
}

print_step() {
    echo -e "${BLUE}▶ $1${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${CYAN}ℹ $1${NC}"
}

print_separator() {
    echo -e "${CYAN}───────────────────────────────────────────────────────────────────${NC}"
}

check_requirements() {
    print_step "Checking requirements..."
    
    local missing_deps=()
    
    if ! command -v java &> /dev/null; then
        missing_deps+=("java")
    fi
    
    if ! command -v mvn &> /dev/null; then
        missing_deps+=("maven")
    fi
    
    if [ ${#missing_deps[@]} -ne 0 ]; then
        print_error "Missing dependencies: ${missing_deps[*]}"
        print_info "Run: ./install-requirements.sh"
        exit 1
    fi
    
    print_success "All requirements satisfied"
}

check_java_version() {
    print_step "Checking Java version..."
    
    local java_version=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    
    if [ "$java_version" -lt 17 ]; then
        print_error "Java 17 or higher required. Found: Java $java_version"
        exit 1
    fi
    
    print_success "Java $java_version detected"
}

#───────────────────────────────────────────────────────────────────────────────
# Build Commands
#───────────────────────────────────────────────────────────────────────────────

cmd_clean() {
    print_step "Cleaning project..."
    mvn clean -q
    print_success "Project cleaned"
}

cmd_compile() {
    print_step "Compiling source code..."
    mvn compile -q
    print_success "Compilation successful"
}

cmd_test() {
    print_step "Running unit tests..."
    print_separator
    mvn test
    print_separator
    print_success "All tests passed"
}

cmd_test_quiet() {
    print_step "Running unit tests (quiet mode)..."
    mvn test -q
    print_success "All tests passed"
}

cmd_package() {
    print_step "Packaging JAR file..."
    mvn package -q -DskipTests
    print_success "JAR created: $JAR_FILE"
}

cmd_install() {
    print_step "Installing to local Maven repository..."
    mvn install -q -DskipTests
    print_success "Installed to local repository"
}

cmd_run() {
    if [ ! -f "$JAR_FILE" ]; then
        print_warning "JAR not found. Building first..."
        cmd_build
    fi
    
    print_step "Running application..."
    print_separator
    java -jar "$JAR_FILE"
}

cmd_build() {
    print_step "Full build process..."
    echo
    cmd_clean
    cmd_compile
    cmd_test_quiet
    cmd_package
    echo
    print_success "Build completed successfully!"
    print_info "Run with: ./build.sh run"
}

cmd_quick() {
    print_step "Quick build (skip tests)..."
    echo
    cmd_clean
    cmd_compile
    cmd_package
    echo
    print_success "Quick build completed!"
}

cmd_coverage() {
    print_step "Generating test coverage report..."
    mvn jacoco:prepare-agent test jacoco:report -q 2>/dev/null || {
        print_warning "JaCoCo not configured. Running tests only..."
        cmd_test
        return
    }
    print_success "Coverage report: target/site/jacoco/index.html"
}

cmd_docs() {
    print_step "Generating Javadoc..."
    mvn javadoc:javadoc -q
    print_success "Documentation: target/site/apidocs/index.html"
}

cmd_verify() {
    print_step "Running full verification..."
    echo
    check_requirements
    check_java_version
    cmd_clean
    cmd_compile
    cmd_test
    cmd_package
    echo
    print_success "Verification completed!"
}

cmd_info() {
    print_info "Project: $PROJECT_NAME"
    print_info "Version: $VERSION"
    print_info "Main Class: $MAIN_CLASS"
    print_info "JAR File: $JAR_FILE"
    echo
    print_step "Java Version:"
    java -version 2>&1 | head -n 1
    echo
    print_step "Maven Version:"
    mvn -version | head -n 1
}

cmd_help() {
    echo "Usage: ./build.sh [command]"
    echo
    echo "Commands:"
    echo "  build      Full build (clean, compile, test, package)"
    echo "  quick      Quick build (skip tests)"
    echo "  clean      Remove build artifacts"
    echo "  compile    Compile source code"
    echo "  test       Run unit tests"
    echo "  package    Create JAR file"
    echo "  install    Install to local Maven repository"
    echo "  run        Run the application"
    echo "  coverage   Generate test coverage report"
    echo "  docs       Generate Javadoc"
    echo "  verify     Full verification pipeline"
    echo "  info       Show project information"
    echo "  help       Show this help message"
    echo
    echo "Examples:"
    echo "  ./build.sh build    # Full build"
    echo "  ./build.sh run      # Run application"
    echo "  ./build.sh test     # Run tests only"
}

#───────────────────────────────────────────────────────────────────────────────
# Main
#───────────────────────────────────────────────────────────────────────────────

main() {
    print_banner
    
    local command="${1:-build}"
    
    case "$command" in
        build)    cmd_build ;;
        quick)    cmd_quick ;;
        clean)    cmd_clean ;;
        compile)  cmd_compile ;;
        test)     cmd_test ;;
        package)  cmd_package ;;
        install)  cmd_install ;;
        run)      cmd_run ;;
        coverage) cmd_coverage ;;
        docs)     cmd_docs ;;
        verify)   cmd_verify ;;
        info)     cmd_info ;;
        help)     cmd_help ;;
        *)
            print_error "Unknown command: $command"
            echo
            cmd_help
            exit 1
            ;;
    esac
}

main "$@"