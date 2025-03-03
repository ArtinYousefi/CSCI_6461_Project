
/**
 * This class simulates a single-port memory for the CS6461 computer system.
 *
 * In a real hardware implementation, a single-port memory would:
 *   1) Accept an address on one clock cycle (placed into the MAR).
 *   2) On the next clock cycle, either provide the data (read) to the MBR 
 *      or accept data (write) from the MBR.
 *
 * For simplicity in software, we combine these steps into one method call
 * (readWord / writeWord). Each location is 16 bits, stored in a 'short' array.
 *
 * The memory is initialized to zero on "power up" (or reset).
 */
public class Memory {

    // The maximum size of our memory in words (16-bit units).
    private static final int MEM_SIZE = 2048; 
    private static final int IX_SIZE = 3; 
    private static final int GPR_SIZE = 4; 
    /**
     * Internal array to store the memory contents.
     * Each element is a 16-bit (short) value.
     */
    public int[] memory; //was private, need public for access w/ functions, could also use a var to avoid this but public is fine for this purpose
    
//NOTE: was shorts, but due to them being for signed/two's complement does not work for our application, instead we will use ints and limit
    //artificially
    //Index Registers 3x 1-3- 16 bits each  (could make them an array 0-0 instead of individual vars?)
    public int[] IX;
    //General Purpose Registers 4x 0-3 - 16 bits each
    public int[] GPR;
    //Program Counter -12 bits
    public int PC; //will have to check that it doesn't overflow/go past 12 bits
    //Condition Code - 4 bits  
    public byte CC; 
    //Instruction Register - 16 bits
    public int IR;
    //Memory Address Register - 12 bits
    public int MAR;
    //Memory Buffer Register - 16 bits
    public int MBR;
    //Machine Fault Register - 4 bits		
    public byte MFR;
    //May need additional registers in the future for calculations like Y and Z in diagram
    
    /**
     * Constructor that initializes the memory array
     * and sets all locations to zero.
     */
    public Memory() {
        memory = new int[MEM_SIZE];
        // Java initializes a new short array to zero by default,
        // but we explicitly set it here for clarity.
        for (int i = 0; i < MEM_SIZE; i++) {
            memory[i] = 0;
        }
        //Init registers here
        
        IX = new int[IX_SIZE];
        
        for (int i = 0; i < IX_SIZE; i++) {
        	IX[i] = 0;
        }
        GPR = new int[GPR_SIZE];
        
        for (int i = 0; i < GPR_SIZE; i++) {
            GPR[i] = 0;
        }
        PC = 0; 
        CC = 0; 
        IR = 0;
        MAR = 0;
        MBR = 0;
        MFR = 0;
    }

    /**
     * Reads a 16-bit word from the specified address.
     *
     * @param address The memory address to read from.
     * @return The 16-bit value stored at that address.
     * @throws IllegalArgumentException if the address is out of bounds.
     */
    public int readWord(int address) {
        // Check for valid address range
        if (address < 0 || address >= MEM_SIZE) {
            throw new IllegalArgumentException("Memory read out of bounds: " + address);
        }
        // Return the value stored at 'address'
        return memory[address];
    }

    /**
     * Writes a 16-bit word to the specified address.
     *
     * @param address The memory address to write to.
     * @param data    The 16-bit data value to store.
     * @throws IllegalArgumentException if the address is out of bounds.
     */
    public void writeWord(int address, int data) {
        // Check for valid address range
        if (address < 0 || address >= MEM_SIZE) {
            throw new IllegalArgumentException("Memory write out of bounds: " + address);
        }
        // Store the data at 'address'
        memory[address] = data;
    }

    /**
     * Resets (clears) the entire memory to zero.
     * Can be called if you want to simulate a "power-on" reset.
     */
    public void reset() {
        for (int i = 0; i < MEM_SIZE; i++) {
            memory[i] = 0;
        }
    }

    /**
     * Optional helper to get the total memory size.
     * Useful if your simulator needs to confirm valid addresses
     * elsewhere in the code.
     *
     * @return The number of 16-bit words this memory can hold.
     */
    public int getSize() {
        return MEM_SIZE;
    }

    /**
     * Main method for basic testing of the Memory class.
     * It creates an instance of Memory, writes a value to an address,
     * and reads it back.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
//        // Create a new memory instance
//        Memory mem = new Memory();
//        
//        // Read the initial value at address 0 (should be 0)
//        System.out.println("Initial value at address 0: " + mem.readWord(0));
//        
//        // Write a sample value (e.g., 12345) to address 0
//        mem.writeWord(0, (short) 12345);
//        
//        // Read and print the value at address 0 after writing
//        System.out.println("Value at address 0 after write: " + mem.readWord(0));
    }
}
