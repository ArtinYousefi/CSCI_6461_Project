import java.io.*;
import java.util.*;

public class Assembler {
    // Maps operation mnemonics to their binary opcode values
    private static final Map<String, String> OPCODES = new HashMap<>();
    private static final Map<String, Integer> SYMBOL_TABLE = new HashMap<>();
    private static int locationCounter = 6; // Initial location counter based on ISA
    
    static {
        // Define the opcode mappings from ISA
        OPCODES.put("HLT", "000000");
        OPCODES.put("TRAP", "110000");
        OPCODES.put("LDR", "000001");
        OPCODES.put("STR", "000010");
        OPCODES.put("LDA", "000011");
        OPCODES.put("LDX", "100001");
        OPCODES.put("STX", "100010");
        OPCODES.put("JZ", "010000");
        OPCODES.put("JNE", "010001");
        OPCODES.put("JCC", "010010");
        OPCODES.put("JMA", "010011");
        OPCODES.put("JSR", "010100");
        OPCODES.put("RFS", "010101");
        OPCODES.put("SOB", "010110");
        OPCODES.put("JGE", "010111");
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
        
        locationCounter = 6;
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith(";")) continue;
            
            String[] parts = line.split(" ");
            String opcode = parts[0];
            String binaryCode = "";
            
            if (OPCODES.containsKey(opcode)) {
                binaryCode = OPCODES.get(opcode); // Retrieve binary opcode
                String operands = (parts.length > 1) ? String.join(" ", Arrays.copyOfRange(parts, 1, parts.length)) : "";
                binaryCode += parseOperands(operands); // Convert operands to binary
                
                while (binaryCode.length() < 16) {
                    binaryCode = "0" + binaryCode; // Ensure instructions are exactly 16 bits
                }
                if (binaryCode.length() > 16) {
                    binaryCode = binaryCode.substring(binaryCode.length() - 16);
                }
                
                String octalAddress = String.format("%06o", locationCounter); // Convert address to octal
                String octalInstruction = binaryToOctal(binaryCode); // Convert binary instruction to octal
                
                // Write formatted output to listing and load files
                listWriter.write(octalAddress + " " + octalInstruction + " " + line + "\n");
                loadWriter.write(octalAddress + " " + octalInstruction + "\n");
                locationCounter++;
            }
        }
        reader.close();
        listWriter.close();
        loadWriter.close();
    }
    
    // Converts operands to their binary representation
    private static String parseOperands(String operands) {
        String[] parts = operands.split("[ ,]");
        StringBuilder binaryOperands = new StringBuilder();
        for (String part : parts) {
            if (SYMBOL_TABLE.containsKey(part)) { // Replace label with its address
                binaryOperands.append(String.format("%05d", Integer.parseInt(Integer.toBinaryString(SYMBOL_TABLE.get(part)))));
            } else {
                try {
                    binaryOperands.append(String.format("%05d", Integer.parseInt(Integer.toBinaryString(Integer.parseInt(part)))));
                } catch (NumberFormatException e) {
                    binaryOperands.append("00000"); // Default to zero if operand is invalid
                }
            }
        }
        while (binaryOperands.length() < 10) {
            binaryOperands.append("0"); // Ensure operands fit in required bit size
        }
        return binaryOperands.toString();
    }
    
    // Converts a binary string to an octal representation
    private static String binaryToOctal(String binary) {
        long decimal = Long.parseLong(binary, 2); // Convert binary to decimal
        return String.format("%06o", decimal); // Convert decimal to octal format
    }
}
