public class CPU {
    private Memory memory;
    private int PC, MAR, MBR, IR, CC, MFR;  // Registers
    private int[] GPR = new int[4]; // General Purpose Registers
    private int[] IX = new int[3];  // Index Registers
    private boolean halted = false; // Halt flag

    public CPU(Memory memory) {
        this.memory = memory;
        this.PC = 0;
    }

    /** Fetch the instruction from memory */
    public void fetch() {
        MAR = PC;  // Set MAR to PC
        MBR = memory.readWord(MAR);  // Fetch instruction
        IR = MBR;  // Load into IR
        PC++;  // Increment PC to next instruction
    }

    /** Decode the instruction (basic implementation) */
    public void decode() {
        // Extract opcode and operands (assuming 16-bit instruction format)
        int opcode = (IR >> 12) & 0xF;  // Extract opcode (top 4 bits)
        int reg = (IR >> 10) & 0x3;  // Extract register (next 2 bits)
        int addr = IR & 0x3FF;  // Extract address (last 10 bits)
        
        System.out.println("Decoded: Opcode=" + opcode + " Reg=" + reg + " Addr=" + addr);
    }

    /** Execute the instruction */
    public void execute() {
        int opcode = (IR >> 12) & 0xF;
        int reg = (IR >> 10) & 0x3;
        int addr = IR & 0x3FF;

        switch (opcode) {
            case 1: // Load instruction
                GPR[reg] = memory.readWord(addr);
                break;
            case 2: // Store instruction
                memory.writeWord(addr, GPR[reg]);
                break;
            case 3: // Add
                GPR[reg] += memory.readWord(addr);
                break;
            case 4: // Halt instruction
                halted = true;
                System.out.println("CPU Halted.");
                break;
            default:
                System.out.println("Unknown Opcode: " + opcode);
                break;
        }
    }

    /** Check if CPU is halted */
    public boolean isHalted() {
        return halted;
    }

    /** Set the halted state */
    public void setHalted(boolean state) {
        this.halted = state;
    }

    // Getters for registers (for GUI updates)
    public int getPC() { return PC; }
    public int getMAR() { return MAR; }
    public int getMBR() { return MBR; }
    public int getIR() { return IR; }
    public int getCC() { return CC; }
    public int getMFR() { return MFR; }
    public int[] getGPR() { return GPR; }
    public int[] getIX() { return IX; }
}
