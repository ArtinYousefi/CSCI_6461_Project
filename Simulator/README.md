## CSCI 6461 Computer Simulator

**Description**  
A Java-based simulator for the CSCI 6461 instruction set architecture, designed to illustrate fundamental computer architecture concepts. It supports a memory/cache system, instruction decoding, register operations, and basic input/output.

### Features
- **Instruction Execution**: Fetch, decode, and execute instructions in the CSCI 6461 ISA.
- **Memory & Cache**: Fully associative cache layer with FIFO replacement.
- **Registers**: GPRs (R0–R3), IXRs (X1–X3), PC, IR, MAR, MBR, etc.
- **GUI Interface**: Load programs from a file, run or step through instructions, and view registers in real-time.
- **Test Programs**: 
  - Closest Number Finder (reads 20 numbers, finds the closest to a target)
  - Word Search (reads sentences, searches for a word)

### Usage
1. **Clone** the repository:
   ```bash
   git clone https://github.com/yourusername/CSCI6461-Simulator.git
   ```
2. **Open** the project in an IDE (or compile via command line).
3. **Run** `GUI.java` (or `java -jar Simulator.jar` if a JAR is available).
4. **Load** `load.txt` to import machine code instructions.
5. **Execute** the program via **Run** or **Step**.

### File Structure
- `CPU.java` - Handles instruction processing
- `Memory.java` - Implements memory and cache
- `Control.java` - Decodes instructions, manages the execution flow
- `GUI.java` - Graphical user interface
- `load.txt` - Machine code file (octal) for test programs

### Contributing
1. **Fork** the repository
2. Create a **feature branch**
3. **Commit** and **push** changes
4. Open a **Pull Request**
