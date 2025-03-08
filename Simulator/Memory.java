public class Memory {
    private static final int MEM_SIZE = 2048; 
    private static final int IX_SIZE = 3;  
    private static final int GPR_SIZE = 4;  

    public int[] memory; 
    public int[] IX;  
    public int[] GPR;  
    public int PC, MAR, MBR, IR, CC, MFR;  

    public Memory() {
        memory = new int[MEM_SIZE];
        IX = new int[IX_SIZE];
        GPR = new int[GPR_SIZE];

        // Initialize all memory locations and registers to zero
        for (int i = 0; i < MEM_SIZE; i++) {
            memory[i] = 0;
        }
        for (int i = 0; i < IX_SIZE; i++) {
            IX[i] = 0;
        }
        for (int i = 0; i < GPR_SIZE; i++) {
            GPR[i] = 0;
        }

        PC = 0; MAR = 0; MBR = 0; IR = 0; CC = 0; MFR = 0;
    }

    public int readWord(int address) {
        if (address < 0 || address >= MEM_SIZE) {
            throw new IllegalArgumentException("Memory read out of bounds: " + address);
        }
        return memory[address];
    }

    public void writeWord(int address, int data) {
        if (address < 0 || address >= MEM_SIZE) {
            throw new IllegalArgumentException("Memory write out of bounds: " + address);
        }
        memory[address] = data;
    }

    public void reset() {
        for (int i = 0; i < MEM_SIZE; i++) {
            memory[i] = 0;
        }
        PC = 0; MAR = 0; MBR = 0; IR = 0; CC = 0; MFR = 0;
        for (int i = 0; i < IX_SIZE; i++) IX[i] = 0;
        for (int i = 0; i < GPR_SIZE; i++) GPR[i] = 0;
    }
}
