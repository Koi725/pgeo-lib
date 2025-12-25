#!/bin/bash

#═══════════════════════════════════════════════════════════════════════════════
# PGeo Library - Requirements Installation Script
# University of Aveiro - ESTGA
#═══════════════════════════════════════════════════════════════════════════════

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

print_banner() {
    echo -e "${CYAN}"
    echo "╔═══════════════════════════════════════════════════════════════════╗"
    echo "║              PGeo Library - Requirements Installer                ║"
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

detect_os() {
    if [ -f /etc/os-release ]; then
        . /etc/os-release
        OS=$ID
    elif [ -f /etc/debian_version ]; then
        OS="debian"
    elif [ -f /etc/redhat-release ]; then
        OS="rhel"
    elif [[ "$OSTYPE" == "darwin"* ]]; then
        OS="macos"
    else
        OS="unknown"
    fi
    echo "$OS"
}

install_debian() {
    print_step "Installing dependencies for Debian/Ubuntu..."
    
    sudo apt-get update -qq
    
    print_step "Installing OpenJDK 17..."
    sudo apt-get install -y -qq openjdk-17-jdk
    
    print_step "Installing Maven..."
    sudo apt-get install -y -qq maven
    
    print_step "Installing Git..."
    sudo apt-get install -y -qq git
}

install_fedora() {
    print_step "Installing dependencies for Fedora/RHEL..."
    
    print_step "Installing OpenJDK 17..."
    sudo dnf install -y java-17-openjdk-devel
    
    print_step "Installing Maven..."
    sudo dnf install -y maven
    
    print_step "Installing Git..."
    sudo dnf install -y git
}

install_arch() {
    print_step "Installing dependencies for Arch Linux..."
    
    print_step "Installing OpenJDK 17..."
    sudo pacman -S --noconfirm jdk17-openjdk
    
    print_step "Installing Maven..."
    sudo pacman -S --noconfirm maven
    
    print_step "Installing Git..."
    sudo pacman -S --noconfirm git
}

install_macos() {
    print_step "Installing dependencies for macOS..."
    
    if ! command -v brew &> /dev/null; then
        print_error "Homebrew not found. Install from https://brew.sh"
        exit 1
    fi
    
    print_step "Installing OpenJDK 17..."
    brew install openjdk@17
    
    print_step "Installing Maven..."
    brew install maven
    
    print_step "Installing Git..."
    brew install git
    
    print_info "You may need to add Java to PATH:"
    print_info "  echo 'export PATH=\"/usr/local/opt/openjdk@17/bin:\$PATH\"' >> ~/.zshrc"
}

install_manual() {
    print_warning "Automatic installation not supported for your OS."
    echo
    print_info "Please install manually:"
    echo "  1. Java JDK 17+: https://adoptium.net/"
    echo "  2. Maven 3.8+:   https://maven.apache.org/download.cgi"
    echo "  3. Git:          https://git-scm.com/downloads"
}

verify_installation() {
    print_step "Verifying installation..."
    
    local all_ok=true
    
    if command -v java &> /dev/null; then
        local java_version=$(java -version 2>&1 | head -n 1)
        print_success "Java: $java_version"
    else
        print_error "Java not found"
        all_ok=false
    fi
    
    if command -v mvn &> /dev/null; then
        local mvn_version=$(mvn -version 2>&1 | head -n 1)
        print_success "Maven: $mvn_version"
    else
        print_error "Maven not found"
        all_ok=false
    fi
    
    if command -v git &> /dev/null; then
        local git_version=$(git --version)
        print_success "Git: $git_version"
    else
        print_error "Git not found"
        all_ok=false
    fi
    
    echo
    
    if [ "$all_ok" = true ]; then
        print_success "All requirements satisfied!"
        print_info "Run: ./build.sh build"
    else
        print_error "Some requirements missing. Please install manually."
        exit 1
    fi
}

set_java_home() {
    print_step "Setting JAVA_HOME..."
    
    local java_path=$(which java)
    local java_home=$(dirname $(dirname $(readlink -f "$java_path" 2>/dev/null || echo "$java_path")))
    
    if [ -d "$java_home" ]; then
        export JAVA_HOME="$java_home"
        print_success "JAVA_HOME=$JAVA_HOME"
        
        print_info "Add to your shell profile:"
        print_info "  export JAVA_HOME=\"$java_home\""
    fi
}

main() {
    print_banner
    
    local os=$(detect_os)
    print_info "Detected OS: $os"
    echo
    
    case "$os" in
        ubuntu|debian|linuxmint|pop)
            install_debian
            ;;
        fedora|rhel|centos|rocky|alma)
            install_fedora
            ;;
        arch|manjaro|endeavouros)
            install_arch
            ;;
        macos)
            install_macos
            ;;
        *)
            install_manual
            exit 0
            ;;
    esac
    
    echo
    set_java_home
    echo
    verify_installation
}

main "$@"