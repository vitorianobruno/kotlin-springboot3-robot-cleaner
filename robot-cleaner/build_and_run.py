#!/usr/bin/env python3
"""
Script to build, test and run the Robot Cleaner application in Docker.
"""

import subprocess
import sys
import time

def execute_command(cmd, description=None):
    """Execute a shell command with error handling."""
    if description:
        print(f"\n🔧 {description}...")
        print(f"   Command: {cmd}")

    try:
        result = subprocess.run(cmd, shell=True, check=True,
                              capture_output=True, text=True)
        if result.stdout:
            print(result.stdout)
        return True
    except subprocess.CalledProcessError as e:
        print(f"Error: {description} failed!")
        print(f"Exit code: {e.returncode}")
        if e.stdout:
            print(f"   Stdout: {e.stdout}")
        if e.stderr:
            print(f"   Stderr: {e.stderr}")
        return False

def main():
    print("Robot Cleaner - Build & Run Automation")
    print("=" * 50)

    # Step 1: Run tests locally
    if not execute_command("mvn test", "Running unit tests locally"):
        print("Tests failed! Aborting.")
        sys.exit(1)

    # Step 2: Build Docker image
    if not execute_command("docker build -t robot-cleaner .", "Building Docker image"):
        print("Docker build failed!")
        sys.exit(1)

    # Step 3: Stop any existing container
    execute_command("docker stop robot-cleaner-container || true", "Stopping existing container")
    execute_command("docker rm robot-cleaner-container || true", "Removing existing container")

    # Step 4: Run the container
    print(f"\nStarting application...")
    run_cmd = (
        "docker run -d --name robot-cleaner-container "
        "-p 8080:8080 "
        "--restart unless-stopped "
        "robot-cleaner"
    )

    if not execute_command(run_cmd, "Starting Docker container"):
        print("Failed to start container!")
        sys.exit(1)

    # Step 5: Wait for application to start
    print(f"\nWaiting for application to start...")
    time.sleep(8)

    # Step 7: Show application info
    print(f"\nApplication running at: http://localhost:8080")
    print(f"API: POST http://localhost:8080/api/simulate")
    print(f"\nStop: docker stop robot-cleaner-container")
    print(f"Logs: docker logs -f robot-cleaner-container")

if __name__ == "__main__":
    main()