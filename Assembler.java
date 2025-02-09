import java.io.*;
import java.util.*;

public class Assembler {
    // Maps operation mnemonics to their binary opcode values
    private static final Map<String, String> OPCODES = new HashMap<>();
    private static final Map<String, Integer> SYMBOL_TABLE = new HashMap<>();
    private static int locationCounter = 6; // Initial location counter based on ISA
    
    static {
        // Miscellaneous Instructions
        OPCODES.put("HLT", "000000");
        OPCODES.put("TRAP", "011000");
    
        // Load/Store Instructions
        OPCODES.put("LDR", "000001");
        OPCODES.put("STR", "000010");
        OPCODES.put("LDA", "000011");
        OPCODES.put("LDX", "100001");
        OPCODES.put("STX", "100010");
    
        // Transfer Instructions
        OPCODES.put("JZ", "001000");
        OPCODES.put("JNE", "010001");
        OPCODES.put("JCC", "010010");
        OPCODES.put("JMA", "010011");
        OPCODES.put("JSR", "010100");
        OPCODES.put("RFS", "010101");
        OPCODES.put("SOB", "010110");
        OPCODES.put("JGE", "010111");
    
        // Arithmetic and Logical Instructions
        OPCODES.put("AMR", "000100");
        OPCODES.put("SMR", "000101");
        OPCODES.put("AIR", "000110");
        OPCODES.put("SIR", "000111");
    
        // Register-to-Register Operations
        OPCODES.put("MLT", "111000");
        OPCODES.put("DVD", "111001");
        OPCODES.put("TRR", "111010");
        OPCODES.put("AND", "111011");
        OPCODES.put("ORR", "111100");
        OPCODES.put("NOT", "111101");
    
        // Shift and Rotate Instructions
        OPCODES.put("SRC", "011001");
        OPCODES.put("RRC", "011010");
    
        // I/O Instructions
        OPCODES.put("IN", "110001");
        OPCODES.put("OUT", "110010");
        OPCODES.put("CHK", "110011");
    }
    

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Assembler <assembly file>");
            return;
        }
        
        String assemblyFile = args[0];
        String listingFile = "listing.txt";
        String loadFile = "load.txt";
        
        try {
            passOne(assemblyFile); // First pass: construct the symbol table
            passTwo(assemblyFile, listingFile, loadFile); // Second pass: generate binary output
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // First pass: Process labels and populate the symbol table
    private static void passOne(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith(";")) continue; // Skip empty and comment lines
            
            String[] parts = line.split(" ");
            if (parts[0].endsWith(":")) { // Identify label definitions
                String label = parts[0].replace(":", "");
                SYMBOL_TABLE.put(label, locationCounter); // Store label address in the symbol table
                line = line.substring(parts[0].length()).trim();
            }
            
            if (!line.isEmpty()) {
                locationCounter++; // Increment location counter for each instruction
            }
        }
        reader.close();
    }
    
    // Second pass: Convert instructions to binary and write to output files
    private static void passTwo(String fileName, String listingFile, String loadFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        BufferedWriter listWriter = new BufferedWriter(new FileWriter(listingFile));
        BufferedWriter loadWriter = new BufferedWriter(new FileWriter(loadFile));
    
        locationCounter = 6; // Reset to start
    
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith(";")) continue;
    
            String[] parts = line.split("\\s+");
    
            if (parts[0].equalsIgnoreCase("Data")) {
                int value = 0;
                if (SYMBOL_TABLE.containsKey(parts[1])) {
                    value = SYMBOL_TABLE.get(parts[1]); 
                } else if (parts[1].matches("\\d+")) {
                    value = Integer.parseInt(parts[1]); 
                } else {
                    System.err.println("Error: Undefined label '" + parts[1] + "'");
                    value = 0; // Assign default if label is undefined
                }
                
    
                String octalAddress = String.format("%06o", locationCounter);
                String octalValue = String.format("%06o", value);
    
                listWriter.write(octalAddress + " " + octalValue + " " + line + "\n");
                loadWriter.write(octalAddress + " " + octalValue + "\n");
    
                locationCounter++;
            } else if (OPCODES.containsKey(parts[0])) {
                String opcode = OPCODES.get(parts[0]);
                String operands = (parts.length > 1) ? String.join(" ", Arrays.copyOfRange(parts, 1, parts.length)) : "";
                String binaryCode = opcode + parseOperands(operands);
    
                while (binaryCode.length() < 16) {
                    binaryCode = "0" + binaryCode;
                }
                if (binaryCode.length() > 16) {
                    binaryCode = binaryCode.substring(binaryCode.length() - 16);
                }
    
                String octalAddress = String.format("%06o", locationCounter);
                String octalInstruction = binaryToOctal(binaryCode);
    
                listWriter.write(octalAddress + " " + octalInstruction + " " + line + "\n");
                loadWriter.write(octalAddress + " " + octalInstruction + "\n");
    
                locationCounter++;
            } else if (parts[0].equalsIgnoreCase("LOC")) {
                locationCounter = Integer.parseInt(parts[1]);
            } else if (parts[0].endsWith(":")) {
                continue; // Ignore labels in second pass
            }
        }
    
        reader.close();
        listWriter.close();
        loadWriter.close();
    }
    
    // Converts operands to their binary representation
    private static String parseOperands(String operands) {
        if (operands.isEmpty()) return "0000000000"; // Default empty operands
    
        String[] parts = operands.split("[, ]+");
        StringBuilder binaryOperands = new StringBuilder();
    
        int reg = 0, index = 0, indirect = 0, address = 0;
    
        if (parts.length > 0) {
            try {
                reg = Integer.parseInt(parts[0]); // First operand is register
            } catch (NumberFormatException ignored) {}
        }
        if (parts.length > 1) {
            try {
                index = Integer.parseInt(parts[1]); // Second operand is index register
            } catch (NumberFormatException ignored) {}
        }
        if (parts.length > 2) {
            try {
                address = SYMBOL_TABLE.getOrDefault(parts[2], Integer.parseInt(parts[2])); // Third operand is address
            } catch (NumberFormatException ignored) {}
        }
        if (parts.length > 3) {
            try {
                indirect = Integer.parseInt(parts[3]); // Fourth operand (optional) is indirect flag
            } catch (NumberFormatException ignored) {}
        }
    
        // Convert values to binary with correct field widths
        binaryOperands.append(String.format("%02d", Integer.parseInt(Integer.toBinaryString(reg))));   // 2-bit register
        binaryOperands.append(String.format("%02d", Integer.parseInt(Integer.toBinaryString(index)))); // 2-bit index
        binaryOperands.append(indirect == 1 ? "1" : "0"); // 1-bit indirect
        binaryOperands.append(String.format("%05d", Integer.parseInt(Integer.toBinaryString(address)))); // 5-bit address
    
        return binaryOperands.toString();
    }
    
    
    // Converts a binary string to an octal representation
    private static String binaryToOctal(String binary) {
        long decimal = Long.parseLong(binary, 2); // Convert binary to decimal
        return String.format("%06o", decimal); // Convert decimal to octal format
    }
}
