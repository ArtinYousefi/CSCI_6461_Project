# CSCI 6461 Computer Simulator – Part III

**Description**  
A Java-based simulator for the CSCI 6461 instruction set architecture (ISA), developed for CS6461 at George Washington University. This simulator executes all instructions defined in the ISA, with a GUI for loading and running machine code programs. Part III specifically demonstrates the execution of a full instruction set, TRAP and machine fault handling, and a functional memory/cache subsystem.

---

## 🧠 Features

- ✅ **Full ISA Instruction Execution** – Supports all standard instructions: load/store, arithmetic, jump, logical, shift/rotate, I/O, and TRAP.
- ✅ **TRAP & Machine Fault Handling** – Properly routes illegal opcodes and faults to memory-defined routines.
- ✅ **Cache System** – Fully associative FIFO-based memory cache.
- ✅ **GUI Interface** – Java Swing GUI supports file loading, register views, step/run control, and I/O.
- ✅ **Program 2 Demonstration** – Implements a word search in a paragraph, with full use of I/O, TRR, and conditional jumps.

---

## 🚀 Usage

### 1. Clone the repository
```bash
git clone https://github.com/ArtinYousefi/CSCI_6461_Project.git
cd CSCI_6461_Project/Simulator
```

### 2. Run the Simulator
**Option A: Using IDE**
- Open in Eclipse or IntelliJ
- Run `GUI.java`

**Option B: Using Terminal**
```bash
java -jar Simulator.jar
```

---

## 🗂️ File Structure

| File | Description |
|------|-------------|
| `Simulator.jar` | Executable JAR to launch the simulator |
| `MANIFEST.MF` | Declares `Main-Class: GUI` |
| `CPU.java` | Manages registers, fetch/decode/execute |
| `Control.java` | Instruction decoding, TRAP, and flow control |
| `Memory.java` | Memory array + FIFO cache subsystem |
| `test2.asm` | Assembly source for Program 2 |
| `test2.txt` | Machine code (octal) for Program 2 |
| `UserGuide_Part3.pdf` | Guide for loading and running the simulator |
| `DesignNote_Part3.pdf` | Internal architecture and class design |
| `submit_log.txt` | GitHub commit history log for evaluation |

---

## 🧪 Included Test Programs

- **Program 1 – Closest Number Finder**  
  Inputs 20 integers and finds the closest match to a given target.

- **Program 2 – Word Search in Paragraph**  
  Reads six lines from a file, takes a word input, and searches for it using `TRR` and `JZ`.

---

## 🤝 Contributing

This project was developed collaboratively by Group 10 for CSCI 6461 at GWU.

To contribute:
1. Fork the repository
2. Create a new feature branch
3. Commit your changes
4. Open a pull request

---

## 🏁 License
For educational use in CSCI 6461 (Spring 2025) under Professor Lancaster.
