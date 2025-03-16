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

    /** Fetch the instruction from memory (with cache integration) */
    public void fetch() {
        MAR = PC;  // Set MAR to PC
        MBR = memory.readWord(MAR);  // Fetch instruction (now cache-aware)
        IR = MBR;  // Load into IR
        PC++;  // Increment PC to next instruction

        System.out.println("[CPU FETCH] PC=" + MAR + " | Fetched (octal): " + Integer.toOctalString(IR));
    }



    /** Decode the instruction (basic implementation) */
    public void decode() {
        int opcode = (IR >> 12) & 0xF;  // Extract opcode (top 4 bits)
        int reg = (IR >> 10) & 0x3;  // Extract register (next 2 bits)
        int addr = IR & 0x3FF;  // Extract address (last 10 bits)
        
        System.out.println("[CPU DECODE] Opcode=" + opcode + " | Reg=" + reg + " | Addr=" + addr);
    }

    /** Execute the instruction */
    public void execute() {
        int opcode = (IR >> 12) & 0xF;
        int reg = (IR >> 10) & 0x3;
        int addr = IR & 0x3FF;

        switch (opcode) {
            case 1: // Load instruction
                GPR[reg] = memory.readWord(addr);  // Now cache-aware
                System.out.println("[CPU EXECUTE] LOAD -> GPR[" + reg + "] = " + GPR[reg]);
                break;
            case 2: // Store instruction
                memory.writeWord(addr, GPR[reg]);  // Now cache-aware
                System.out.println("[CPU EXECUTE] STORE -> Mem[" + addr + "] = " + GPR[reg]);
                break;
            case 3: // Add
                GPR[reg] += memory.readWord(addr);
                System.out.println("[CPU EXECUTE] ADD -> GPR[" + reg + "] = " + GPR[reg]);
                break;
            case 4: // Halt instruction
                halted = true;
                System.out.println("[CPU EXECUTE] CPU Halted.");
                break;
            default:
                System.out.println("[CPU ERROR] Unknown Opcode: " + opcode);
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
