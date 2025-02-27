
# Simple Memory Implementation for CS6461 Simulator

This repository contains the Java implementation of a simple memory module developed as part of the CS6461 project simulator. The module simulates a single-port memory designed to support basic load and store instructions on a 16-bit processor with 2048 words.

## Overview

- **Memory Capacity:** 2048 words (each 16-bit wide)
- **Supported Operations:**
  - `readWord(int address)`: Reads a 16-bit word from the specified address.
  - `writeWord(int address, short data)`: Writes a 16-bit word to the specified address.
  - `reset()`: Resets the memory by setting all locations to zero.
- **Purpose:** Provides a simple and robust simulation of a single-port memory for executing basic load and store instructions.

## Files

- **Memory.java:** Contains the memory implementation along with a `main` method for basic testing.
- **DesignNote.tex:** LaTeX file containing the design notes for this module.
- **UserGuide.tex:** LaTeX file with the user guide for this module.

## Requirements

- **Java Development Kit (JDK):** Version 1.8 or later.
- **Operating System:** Windows, macOS, or Linux.
- **IDE/Text Editor:** Any Java IDE (e.g., Eclipse, NetBeans) or a text editor with command line tools.

## Compilation

To compile the memory module, run the following command in your terminal:

```bash
javac Memory.java
