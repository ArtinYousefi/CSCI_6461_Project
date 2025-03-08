import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

public class Control {
    public static Memory mem;
    private GUI gui;



    public static  CPU cpu;

    public Control(GUI gui) {
        this.gui = gui;
        this.mem = new Memory();
        this.cpu = new CPU(mem);
    }

    public void stepSimulator() {
        if (mem.readWord(mem.PC) == 0) {  
            System.out.println("[DEBUG] HLT Executed. Stopping Simulation.");
            JOptionPane.showMessageDialog(null, "Simulation Halted!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    
        mem.MAR = mem.PC;
        mem.MBR = mem.readWord(mem.MAR);
        mem.IR = mem.MBR;
    
        System.out.println("[DEBUG] Step Executing at PC: " + mem.PC + " -> " + Integer.toOctalString(mem.IR));
    
        // Decode instruction
        String binary = String.format("%16s", Integer.toBinaryString(mem.IR)).replace(" ", "0");
        String op = binary.substring(0, 6);
        int reg = Integer.parseInt(binary.substring(6, 8), 2);
        int ix = Integer.parseInt(binary.substring(8, 10), 2);
        int indirect = Integer.parseInt(binary.substring(10, 11), 2);
        int addr = Integer.parseInt(binary.substring(11, 16), 2);
    
        System.out.println("[DEBUG] Decoded: OPCODE=" + op + " REG=" + reg + " IX=" + ix + " INDIRECT=" + indirect + " ADDR=" + addr);
    
        int ea = computeEA((byte) ix, (byte) addr, (byte) indirect, Integer.parseInt(op, 2));
        mem.MAR = ea;
    
        // **Execute instruction (step by step)**
        switch (op) {
            case "000001": // LDR
                ldr((byte) reg, (byte) ix, (byte) addr, (byte) indirect);
                break;
            case "000010": // STR
                str((byte) reg, (byte) ix, (byte) addr, (byte) indirect);
                break;
            case "000011": // LDA
                lda((byte) reg, (byte) ix, (byte) addr, (byte) indirect);
                break;
            case "100001": // LDX
                ldx((byte) ix, (byte) addr, (byte) indirect);
                break;
            case "100010": // STX
                stx((byte) ix, (byte) addr, (byte) indirect);
                break;
            case "001000": // JZ (Jump if Zero)
                if (mem.GPR[reg] == 0) {
                    int jumpAddr = mem.readWord(ea);
                    System.out.println("[DEBUG] JZ Taken: Jumping to Mem[" + ea + "] -> " + jumpAddr);
                    
                    if (jumpAddr != 0) {  
                        mem.PC = jumpAddr;  
                        gui.updateGUI();
                        return;  
                    }
                }
                System.out.println("[DEBUG] JZ Not Taken");
                break;
            case "000000": // HLT (Halt)
                System.out.println("[DEBUG] HLT Executed. Stopping Simulation.");
                JOptionPane.showMessageDialog(null, "Simulation Halted!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            default:
                System.out.println("[ERROR] Unknown Opcode: " + op);
        }
    
        mem.PC++;
        printRegisters();
        gui.updateGUI();  
    }

  
    

    public void haltSimulator() {
        cpu.setHalted(true);
        JOptionPane.showMessageDialog(null, "Simulation Halted!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public static Memory getMemory() {
        return mem;
    }

    // **Compute Effective Address**
    public static int computeEA(byte ix, byte addr, byte indirect, int opcode) {
        int effectiveAddress = addr;
    
        // **Fix: JZ should fetch jump target from memory directly**
        if (opcode == 8) {  // JZ instruction
            effectiveAddress = mem.readWord(addr);  // Read the jump address from memory
            System.out.println("[DEBUG] JZ Address Fetched from Mem[" + addr + "] = " + effectiveAddress);
            return effectiveAddress;
        }
    
        // **Apply IXR only if it's not JZ**
        if (ix > 0 && opcode != 8) {  
            effectiveAddress += mem.IX[ix - 1];
        }
    
        // **Indirect Addressing Handling**
        if (indirect == 1) {
            int prevEA = effectiveAddress;
            effectiveAddress = mem.readWord(effectiveAddress);
            System.out.println("[DEBUG] Indirect Addressing: EA changed from " + prevEA + " to " + effectiveAddress);
        }
    
        System.out.println("[DEBUG] Final EA: " + effectiveAddress);
        return effectiveAddress;
    }
    
    
    
    
    

    // **Load file and initialize memory**
    public static void loadLF(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        boolean foundFirstInstruction = false;
    
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith(";")) continue;
    
            String[] parts = line.split(" ");
            if (parts.length < 2) continue;
    
            int addr = Integer.parseInt(parts[0], 8);  // Convert octal to decimal
            int value = Integer.parseInt(parts[1], 8); // Convert octal to decimal
    
            mem.writeWord(addr, value);
            System.out.println("Memory Updated -> Address: " + addr + " Value: " + value);
    
            // **Ensure we start execution at the first real instruction**
            if (!foundFirstInstruction && isInstruction(value, addr)) {
                mem.PC = addr;
                foundFirstInstruction = true;
                System.out.println("[DEBUG] PC initialized to first instruction: " + mem.PC);
            }
        }
        reader.close();
    }
    
    
    
    

    private static boolean isInstruction(int value, int address) {
        int opcode = (value >> 10) & 0b111111; // Extract first 6 bits (opcode)
    
        // Known valid opcodes
        Set<Integer> validOpcodes = new HashSet<>(Arrays.asList(
            1, 2, 3, 33, 34, 8, 17, 18, 19, 20, 21, 22, 23, 24, 
            25, 26, 27, 28, 29, 30, 31, 32, 9, 10, 11, 12, 13, 14, 15, 16
        ));
    
        // Ensure opcode is valid, non-zero, and in the expected instruction range
        return validOpcodes.contains(opcode) && opcode != 0 && address >= 13;
    }
    

    

    // **Instruction Implementations**
    public static void ldr(byte r, byte ix, byte addr, byte indirect) {
        int ea = computeEA(ix, addr, indirect, 1);
        mem.GPR[r] = mem.readWord(ea);
        mem.MAR = ea;
        mem.MBR = mem.GPR[r];
        System.out.println("[DEBUG] LDR -> GPR[" + r + "] = " + mem.GPR[r]);
    }

    public static void ldx(byte ix, byte addr, byte indirect) {
        int ea = computeEA(ix, addr, indirect, 33);
        if (ix > 0 && ix <= 3) {  // Ensure IX is valid
            mem.IX[ix - 1] = mem.readWord(ea);
            mem.MAR = ea;
            mem.MBR = mem.IX[ix - 1];
            System.out.println("[DEBUG] LDX -> IXR[" + (ix - 1) + "] = " + mem.IX[ix - 1]);
        } else {
            System.out.println("[ERROR] Invalid IXR index: " + ix);
        }
    }

    public static void stx(byte ix, byte addr, byte indirect) {
		mem.writeWord(mem.MAR, mem.IX[ix-1]);
	}

    public static void str(byte r, byte ix, byte addr, byte indirect) {
		mem.writeWord(mem.MAR, mem.GPR[r]);
	}
    


    public static void lda(byte r, byte ix, byte addr, byte indirect) {
        int ea = computeEA(ix, addr, indirect, 3);
        int value = mem.readWord(ea);  // ✅ Read memory at EA
        mem.GPR[r] = value;  // ✅ Store the fetched value, not EA
        mem.MAR = ea;
        mem.MBR = value;  // ✅ Store loaded value into MBR
        System.out.println("[DEBUG] LDA -> GPR[" + r + "] = " + mem.GPR[r]);
    }
    
    
    

    // **Main Execution Loop**
    public static void runSimulator() {
        while (mem.readWord(mem.PC) != 0) {
            mem.MAR = mem.PC;
            mem.MBR = mem.readWord(mem.MAR);
            mem.IR = mem.MBR;
    
            System.out.println("[DEBUG] Executing Instruction at PC: " + mem.PC + " -> " + Integer.toOctalString(mem.IR));
    
            String binary = String.format("%16s", Integer.toBinaryString(mem.IR)).replace(" ", "0");
            System.out.println("[DEBUG] Binary Instruction: " + binary);
    
            String op = binary.substring(0, 6);
            int reg = Integer.parseInt(binary.substring(6, 8), 2);
            int ix = Integer.parseInt(binary.substring(8, 10), 2);
            int indirect = Integer.parseInt(binary.substring(10, 11), 2);
            int addr = Integer.parseInt(binary.substring(11, 16), 2);
    
            System.out.println("[DEBUG] Decoded: OPCODE=" + op + " REG=" + reg + " IX=" + ix + " INDIRECT=" + indirect + " ADDR=" + addr);
    
            int ea = computeEA((byte) ix, (byte) addr, (byte) indirect, Integer.parseInt(op, 2));
            mem.MAR = ea;
    
            // **Instruction Execution using if-else**
            if (op.equals("000001")) {  // LDR
                ldr((byte) reg, (byte) ix, (byte) addr, (byte) indirect);
            } else if (op.equals("000010")) {  // STR
                str((byte) reg, (byte) ix, (byte) addr, (byte) indirect);
            } else if (op.equals("000011")) {  // LDA
                lda((byte) reg, (byte) ix, (byte) addr, (byte) indirect);
            } else if (op.equals("100001")) {  // LDX
                ldx((byte) ix, (byte) addr, (byte) indirect);
            } else if (op.equals("100010")) {  // STX
                stx((byte) ix, (byte) addr, (byte) indirect);
            } else if (op.equals("001000")) {  // JZ (Jump if Zero)
                if (mem.GPR[reg] == 0) {
                    int jumpAddr = mem.readWord(ea);
                    System.out.println("[DEBUG] JZ Taken: Jumping to Mem[" + ea + "] -> " + jumpAddr);
                    
                    if (jumpAddr != 0) {  
                        mem.PC = jumpAddr; 
                        continue; 
                    }
                }
                System.out.println("[DEBUG] JZ Not Taken");
            } else if (op.equals("000000")) {  // HLT (Halt)
                System.out.println("[DEBUG] HLT Executed. Stopping Simulation.");
                return;
            } else {
                System.out.println("[ERROR] Unknown Opcode: " + op);
            }
    
            mem.PC++;
            printRegisters();
        }
    
        System.out.println("[DEBUG] BEFORE EXECUTION: GPRs: " +
            mem.GPR[0] + ", " + mem.GPR[1] + ", " + mem.GPR[2] + ", " + mem.GPR[3]);
    
        System.out.println("[DEBUG] Simulation Finished.");
    }
    
    public void storeData() {
        try {
            int address = Integer.parseInt(gui.textField_9.getText()); // Get MAR value
            int data = Integer.parseInt(gui.textField_10.getText()); // Get MBR value
    
            mem.writeWord(address, data); // Store data in memory
            gui.updateMemoryDisplay(); // Refresh the memory table
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid Input!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // **Print Registers**
    public static void printRegisters() {
        System.out.println("[DEBUG] Registers:");
        System.out.println("PC: " + mem.PC + " | MAR: " + mem.MAR + " | MBR: " + mem.MBR + " | IR: " + mem.IR);
        System.out.println("GPRs: " + mem.GPR[0] + ", " + mem.GPR[1] + ", " + mem.GPR[2] + ", " + mem.GPR[3]);
        System.out.println("IXRs: " + mem.IX[0] + ", " + mem.IX[1] + ", " + mem.IX[2]);
    }
}
