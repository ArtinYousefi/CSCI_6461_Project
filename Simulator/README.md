
# Simple Memory Implementation for CS6461 Simulator

This repository contains two parts of the CS6461 project simulator: the memory and the control. The first file, Memory.java is a Java implementation of a simple memory module. The module simulates a single-port memory designed to support basic load and store instructions on a 16-bit processor with 2048 words. The second file, Control.java, acts as the 'body' of the simulator, reading the loadfile, storing to memory, and executing instructions.

## Overview

-**Memory.java**
  - **Memory Capacity:** 2048 words (each 16-bit wide)
  - **Supported Operations:**
    - `readWord(int address)`: Reads a 16-bit word from the specified address.
    - `writeWord(int address, short data)`: Writes a 16-bit word to the specified address.
    - `reset()`: Resets the memory by setting all locations to zero.
  - **Purpose:** Provides a simple and robust simulation of a single-port memory (and registers) for executing basic load and store instructions.
  - 
-**Control.java**
  - **Supported Operations:**
    - `computeEA (byte ix, byte addr, byte indirect)`: Computes effective address based on provided inputs.
    - `loadLF (String file) `: Runs when user selects 'Init', loads loadfile into memory.
    - `ldr,str,lda,ldx,stx `: Functions to perform the load/store operands.
    - `runSimulator()`: Starts at memory location in the PC, reads instructions and performs operations until a HLT is read.
  - **Purpose:** Provides the simulator's function, connects with memory & registers to perform load/store operations.

## Files

- **Memory.java:** Contains the memory implementation along with a `main` method for basic testing.
- **Control.java:** Contains the 'body' of the simulator. Loads the loadfile, runs the simulator, performs the load/store operands.
- **DesignNote.tex:** LaTeX file containing the design notes for this module.
- **UserGuide.tex:** LaTeX file with the user guide for this module.

## Requirements

- **Java Development Kit (JDK):** Version 1.8 or later.
- **Operating System:** Windows, macOS, or Linux.
- **IDE/Text Editor:** Any Java IDE (e.g., Eclipse, NetBeans) or a text editor with command line tools.
- **Note: When importing, make sure all files are placed into a package. 

## Compilation

To compile the memory module, run the following command in your terminal:

```bash
javac Memory.java
