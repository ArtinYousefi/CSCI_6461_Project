package tryingtowork;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

public class Control {
    public static Memory mem;
    private GUI gui;

    //EN/DECODING SETS - EACH SET IS ENCODED DIFF 
    private static List<String> firstset = Arrays.asList ("000001","000010","000011","001000","010001","010110","010111","000100","000101", "010010","000110","000111",
    		"100001","100010","010011","010100"); //JCC acts the same as the rest and cc just replaces register
    private static List<String> secondset = Arrays.asList ("111000","111001","111010","111011","111100","111101"); //Or,NOT etc.
    private static List<String> thirdset = Arrays.asList("011001","011010"); //bit shifts
    private static List<String> fourthset = Arrays.asList("110001","110010","110011"); //devices

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
    
        // Decode instruction --NEED TO UPDATE THIS BASED ON NEW PARSING, USE SAME METHOD BREAK INTO 4 GROUPS.
        String binary = String.format("%16s", Integer.toBinaryString(mem.IR)).replace(" ", "0");
        String op = binary.substring(0, 6);
        
        int reg = 0; //Note for JCC reg = cc and r for the bitshifts
        int ix = 0; //= Integer.parseInt(binary.substring(8, 10), 2);
        int indirect = 0; //= Integer.parseInt(binary.substring(10, 11), 2);
        int addr = 0; //for RFS,AIR,SIR addr = Immed
        int rx = 0;
        int ry = 0;
        int lr = 0;
        int al = 0;
        int count = 0; //YES we could techincally reuse some of these vars, but this is better for clarity
        int devid = 0;
        
        if(firstset.contains(op)) { //first ones, 'normal' encoding
    	    reg = Integer.parseInt(binary.substring(6, 8), 2);
            ix = Integer.parseInt(binary.substring(8, 10), 2);
            indirect = Integer.parseInt(binary.substring(10, 11), 2);
            addr = Integer.parseInt(binary.substring(11, 16), 2);
            System.out.println("[DEBUG] Decoded: OPCODE=" + op + " REG=" + reg + " IX=" + ix + " INDIRECT=" + indirect + " ADDR=" + addr);
        }else if (secondset.contains(op)) { //second, rx,ry //0-5 opcode, 6-7 rx, 8-9 ry, rest is ignored
    	    rx = Integer.parseInt(binary.substring(6, 8), 2);
            ry = Integer.parseInt(binary.substring(8, 10), 2); //for NOT this will just be 0/ignored
            System.out.println("[DEBUG] Decoded: OPCODE=" + op + " RX=" + rx + " RY=" + ry );
        }else if (thirdset.contains(op)) { //third, r,count,lr,al NOTE: ENCODED IN DIFF ORDER! ALSO only one ignore bit instead of 2
        	//2, 1,1, ignore 1, 5
    	    reg = Integer.parseInt(binary.substring(6, 8), 2);
            al = Integer.parseInt(binary.substring(8, 9), 2);
            lr = Integer.parseInt(binary.substring(9, 10), 2); //10 is skipped b/c it's the ignore bit
            count = Integer.parseInt(binary.substring(11, 16), 2);
            System.out.println("[DEBUG] Decoded: OPCODE=" + op + " REG=" + reg + " A/L=" + al + " L/R=" + lr + " COUNT=" + count);
        }else if (fourthset.contains(op)) { //fourth r and device id
    	    reg = Integer.parseInt(binary.substring(6, 8), 2);
            devid = Integer.parseInt(binary.substring(11, 16), 2);
            System.out.println("[DEBUG] Decoded: OPCODE=" + op + " REG=" + reg + " DEVID=" + devid);
        }else {//    0-5 opcode, 6-7 R, 8-11 ignore?, last 5 = Devid, 2,3ignore,5
        	System.out.println("OPCODE NOT RECOGNIZED AS PART OF A SET");
        }
        
//        int reg = Integer.parseInt(binary.substring(6, 8), 2);
//        int ix = Integer.parseInt(binary.substring(8, 10), 2);
//        int indirect = Integer.parseInt(binary.substring(10, 11), 2);
//        int addr = Integer.parseInt(binary.substring(11, 16), 2);
    
        //System.out.println("[DEBUG] Decoded: OPCODE=" + op + " REG=" + reg + " IX=" + ix + " INDIRECT=" + indirect + " ADDR=" + addr);
    
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
                    int jumpAddr = ea;//mem.readWord(ea); //I think it's supposed to be the EA not the value at the EA (see the loadstore instructions vs this one)
                    System.out.println("[DEBUG] JZ Taken: Jumping to Mem[" + ea + "] -> " + jumpAddr);
                    
                    if (jumpAddr != 0) {  
                        mem.PC = jumpAddr;  
                        gui.updateGUI();
                        return;  
                    }
                }
                System.out.println("[DEBUG] JZ Not Taken");
                break;
            case "010001": //JNE (Jump If Not Equal)
                if (mem.GPR[reg] != 0) {
                    int jumpAddr = ea;//mem.readWord(ea);
                    System.out.println("[DEBUG] JNE Taken: Jumping to Mem[" + ea + "] -> " + jumpAddr);
                    
                    if (jumpAddr != 0) {  
                        mem.PC = jumpAddr;  
                        gui.updateGUI();
                        return;  
                    }
                }
                System.out.println("[DEBUG] JNE Not Taken");
                break;
            case "010010": 
                jcc((byte) reg, (byte) ix, (byte) addr, (byte) indirect);
            	break;
            case "010011":	//JMA (Unconditional Jump to Address)  --will test all of these with single line load files first!
            	mem.PC = ea;
            	break;
            case "010100":	//JSR (Jump and Save Return Address)  
                jsr((byte) ix, (byte) addr, (byte) indirect) ;
            	break;
            case "010101":	//RFS (Return From Subroutine)  //R0 <- immed(address), PC <- c(R3)
            	mem.GPR[0] = addr;
            	mem.PC = mem.GPR[3];
            	break;
            case "010110":	//SOB (Subtract One and Branch)  
                sob((byte) reg, (byte) ix, (byte) addr, (byte) indirect);
            	break;
            case "010111":	//JGE (Jump If Greater Than or Equal To)  
            	if (mem.GPR[reg] >= 0) {
            		mem.PC = ea;
            		gui.updateGUI();
            		return;
            	}
            	System.out.println("[DEBUG] JGE Op, Not greater than or equal to.");
            	break;
            case "000100":	//AMR (Add Memory to Register)  
            	amr((byte) reg, (byte) ix, (byte) addr, (byte) indirect);		//HEY NEED TO GO THROUGH AND MAKE SURE IM USING MBR AND MAR CORRECTLY
            	break;
            case "000101":	//SMR (Subtract Memory from Register)  
            	smr((byte) reg, (byte) ix, (byte) addr, (byte) indirect);
            	break;
            case "000110":	//AIR (Add Immediate to Register)  
            	air((byte)reg,(byte)addr);
            	break;
            case "000111":	//SIR (Subtract Immediate from Register)  
            	sir((byte)reg,(byte)addr);
            	break;
            //so below is next encoding set! rx ry
            case "111000":	//MVT (Multiply Register by Register)  
            	mvt((byte)rx,(byte)ry);
            	break;
            case "111001":	//DVD (Divide Register by Register)  
            	dvd((byte)rx,(byte)ry);
            	break;
            case "111010":	//TRR (Test the Equality of Register and Register)  
            	trr((byte)rx,(byte)ry);
            	break;
            case "111011":	//AND (AND Register by Register) 
            	and((byte)rx,(byte)ry);
            	break;
            case "111100":	//ORR (Logical ORR Register by Register)
            	orr((byte)rx,(byte)ry);
            	break;	
            case "111101":	//NOT (Logical NOT of Register to Register)  
            	not((byte)rx);
            	break;		
            //third set is below 
            case "011001":	//SRC (Shift Register By Count )  //HEY also need to deal with underflow and see what to do for it
	            src((byte)reg,(byte)count,(byte)al,(byte)lr);
            	break;		
            case "011010":	//RRC (Rotate Register By Count)  
	            rrc((byte)reg,(byte)count,(byte)al,(byte)lr);
            	break;		
            //fourth set r, devid
            case "110001":	//IN (Input Character to Register From Device)  
            	 in((byte)reg, (byte)devid);
            	break;		
            case "110010":	//OUT (Output Character to Device From Register)  
            	 out((byte)reg, (byte)devid);
            	break;		
            case "110011":	//CHK (Check Device Status to Register)  
            	System.out.println("CHK IS NOT REQUIRED FOR THIS PART OF THE PROJECT.");
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

//will move these below once they're all done



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
        Set<Integer> validOpcodes = new HashSet<>(Arrays.asList( //need to make sure all are included I think these are all in decimal atm
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
    

    
    public static void jcc(byte cc, byte ix, byte addr, byte indirect) { 

        int ea = computeEA(ix, addr, indirect, 10);
		int ccstored = mem.CC;
		String ccstr = Integer.toBinaryString(ccstored);		
		
		if (ccstr.length() < 4) {
			for (int i = ccstr.length(); i < 4; i++) {
				ccstr = "0" + ccstr; //appending 0 to the front 
			}
		}
		
		if (ccstr.substring(cc,cc+1).equals("1")) {
			mem.PC = ea;
		}//else do nothing, the PC will increment anyway for this and sob, is this okay or should I increment here too????
		
    }
    
    public static void jsr(byte ix, byte addr, byte indirect) {//NOT IMPLEMENTED YET
    	int ea = computeEA(ix, addr, indirect, 12);
    	mem.GPR[3] = mem.PC + 1;
    	mem.PC = ea;
    	mem.GPR[0] = addr; //don't think this is right?
//    	R3 <- PC+1;
//    	PC <- EA
//    	R0 should contain pointer to arguments
//    	Argument list should end with –1 (all 1s) value
    }

    public static void sob(byte r, byte ix, byte addr, byte indirect) {
    	 int ea = computeEA(ix, addr, indirect, 14);
    	 mem.GPR[r] = mem.GPR[r] - 1;
    	 if(mem.GPR[r] > 0) {
    		 mem.PC = ea;
    	 }//else do nothing, PC will increment anyway
    }

    //add memory to register
    public static void amr(byte r, byte ix, byte addr, byte indirect) {
        int ea = computeEA(ix, addr, indirect, 4);
        int val = mem.readWord(ea);
        mem.GPR[r] += val; //c(r) + c(ea) 
    }
    public static void smr(byte r, byte ix, byte addr, byte indirect) {
        int ea = computeEA(ix, addr, indirect, 4);
        int val = mem.readWord(ea);
        mem.GPR[r] -= val; 
    }
	private static void air(byte r, byte immed) { //need to add MBR stuff here
		if(immed == 0) {
			return; //do nothing
		}
	
		int val = mem.GPR[r]; //will be 0 if nothing in it
		
		mem.GPR[r] = val + immed;
	}
    
	private static void sir(byte r, byte immed) {
		if(immed == 0) {
			return; //do nothing
		}
		int val = mem.GPR[r]; //will be 0 if nothing in it
		
		mem.GPR[r] = val - immed; //gonna leave as is for now BUT unsure about 0-Immed  since assuming unsigned?
	}
    
	private static void mvt(byte rx, byte ry) {
		//upper 8 bits and lower 8 bits in result are put into rx and rx + 1 respectively
		int val1 = mem.GPR[rx];
		int val2 = mem.GPR[ry];
		int res = val1 * val2;
		String s = Integer.toBinaryString(res);
		int len = s.length();
		int cc = mem.CC;
		String ccstr = Integer.toBinaryString(cc);

		//rn if overflow, set cc and just truncate upper bits
		String highbits = s.substring(len-16,len-8); //upper 8
		String lowbits = s.substring(len-8,len); //lower 8
			
		if (ccstr.length() < 4) {
			for (int i = ccstr.length(); i < 4; i++) {
				ccstr = "0" + ccstr; //appending 0 to the front 
			}
		}
		
		if(len > 16) { //OVERFLOW
			//set cc(0) = 1
			ccstr = "1" + ccstr.substring(1,3); //appending OVERFLOW value == cc(0) = 1
			mem.CC =  Integer.parseInt(ccstr,2); 
		}else {
			//set cc(0) = 0
			ccstr = "0" + ccstr.substring(1,3);
			mem.CC =  Integer.parseInt(ccstr,2);
		}
		
		mem.GPR[rx] = Integer.parseInt(highbits,2);
		mem.GPR[rx+1] = Integer.parseInt(lowbits,2);
		
	}

	private static void dvd(byte rx, byte ry) {
		if(rx == 1 || rx == 3 || ry == 1 || ry == 3) { //invalid values
			return; 
		}
		
		int val1 = mem.GPR[rx];
		int val2 = mem.GPR[ry];
		int cc = mem.CC;
		String ccstr = Integer.toBinaryString(cc);
		int quotient;
		int remainder;

		if (ccstr.length() < 4) {
			for (int i = ccstr.length(); i < 4; i++) {
				ccstr = "0" + ccstr; //appending 0 to the front 
			}
		}
		
		if(val2 == 0) { //DIVZERIO
			//set cc(2) = 1
			ccstr = ccstr.substring(0,2) + "1" + ccstr.substring(3,3); 
			mem.CC =  Integer.parseInt(ccstr,2); 
			return; //divde by zero error? idk if this should throw an exception
		}else {
			//set cc(2) = 0
			ccstr = ccstr.substring(0,2) + "0" + ccstr.substring(3,3);
			mem.CC =  Integer.parseInt(ccstr,2);
		}
		
		quotient = val1 / val2;
		remainder = val1 - (val2 * quotient);
		
		mem.GPR[rx] = quotient;
		mem.GPR[rx+1] = remainder;

	}

	private static void trr(byte rx, byte ry) { //this sets the condition code in memory could either convert it to binary and set the bit
		//descrip uses cc(4) but this will use cc(3) since thats what most of the doc uses and it's easier to index at 0
		int val1 = mem.GPR[rx];
		int val2 = mem.GPR[ry];
		int cc = mem.CC;
		String s = Integer.toBinaryString(cc);
		
		if (s.length() < 4) {
			for (int i = s.length(); i < 4; i++) {
				s = "0" + s; //appending 0 to the front 
			}
		}
		
		if(val1 == val2) {
			//set cc(3) = 1, idea is grab from memory, convert to binary, set last bit (right) to 1
			s = s.substring(0,3) + "1";
			mem.CC =  Integer.parseInt(s,2); //need to make sure this is working right (ie i'm doing above correctly)
		}else {
			//set cc(3) = 0
			s = s.substring(0,3) + "0";
			mem.CC =  Integer.parseInt(s,2);
		}
	}

	private static void and(byte rx, byte ry) {
		int val1 = mem.GPR[rx];
		int val2 = mem.GPR[ry];
		int res = val1 & val2;
		
		mem.GPR[rx] = res;		
	}

	private static void orr(byte rx, byte ry) {
		int val1 = mem.GPR[rx];
		int val2 = mem.GPR[ry];
		int res = val1 | val2;
		
		mem.GPR[rx] = res;
	}

	private static void not(byte rx) {
		int val = mem.GPR[rx];
		mem.GPR[rx] = ~val;
	}
 
    private static void src(byte r, byte count, byte al, byte lr) {
    	int tbs = mem.GPR[r];
    	
        if(count < 1 || count > 15) { 
        	return; //invalid amount
        }
    	
    	if(al == 0) { //arithmetic >>
        	if(lr == 0) { //right 
        		tbs = tbs >> count;
        	}else { //left
        		tbs = tbs << count;
        	}
    	}else {	//logic >>>
        	if(lr == 0) { //right
        		tbs = tbs >>> count;
        	}else { //left
        		tbs = tbs << count; //based on documentation they're the same???
        	}
    	}
    	
	}

    //strictly logical so al gets ignored
    private static void rrc(byte r, byte count, byte al, byte lr) { //need to use MAR and MBR too UPDATE THIS
        int s=16;
        int tbr = mem.GPR[r];
        
        if(count < 1 || count > 15) { 
        	return; //invalid amount
        }
        
        if(lr == 0) { //right
        	mem.GPR[r]=((tbr>>count) | tbr<<(s-count)) & 0xFFFF;
        }else { //left
        	mem.GPR[r]= ((tbr<<count) | tbr>>(s-count)) & 0xFFFF;
        }
        
	}
    
    private static void in(byte r, byte devid) {
    	int val = cpu.devices[devid];
    	if(devid == 1) { //invalid
    		return;
    	}
    	mem.GPR[r] = val;
    }
    
    private static void out(byte r, byte devid) {
    	int val = mem.GPR[r];
    	if(devid == 0) { //invalid
    		return;
    	}
    	cpu.devices[devid] = val;
    }
    
    
    //need to add IN and OUT here
    
    // **Main Execution Loop**
    public static void runSimulator() {
        while (mem.readWord(mem.PC) != 0) {
            mem.MAR = mem.PC;
            mem.MBR = mem.readWord(mem.MAR);
            mem.IR = mem.MBR;
    
            System.out.println("[DEBUG] Executing Instruction at PC: " + mem.PC + " -> " + Integer.toOctalString(mem.IR));
    
            String binary = String.format("%16s", Integer.toBinaryString(mem.IR)).replace(" ", "0");
            System.out.println("[DEBUG] Binary Instruction: " + binary);
    
 //           String binary = String.format("%16s", Integer.toBinaryString(mem.IR)).replace(" ", "0");
            String op = binary.substring(0, 6);
            
            int reg = 0; //Note for JCC reg = cc and r for the bitshifts
            int ix = 0; //= Integer.parseInt(binary.substring(8, 10), 2);
            int indirect = 0; //= Integer.parseInt(binary.substring(10, 11), 2);
            int addr = 0; //for RFS,AIR,SIR addr = Immed
            int rx = 0;
            int ry = 0;
            int lr = 0;
            int al = 0;
            int count = 0; //YES we could techincally reuse some of these vars, but this is better for clarity
            int devid = 0;
            
            if(firstset.contains(op)) { //first ones, 'normal' encoding
        	    reg = Integer.parseInt(binary.substring(6, 8), 2);
                ix = Integer.parseInt(binary.substring(8, 10), 2);
                indirect = Integer.parseInt(binary.substring(10, 11), 2);
                addr = Integer.parseInt(binary.substring(11, 16), 2);
                System.out.println("[DEBUG] Decoded: OPCODE=" + op + " REG=" + reg + " IX=" + ix + " INDIRECT=" + indirect + " ADDR=" + addr);
            }else if (secondset.contains(op)) { //second, rx,ry //0-5 opcode, 6-7 rx, 8-9 ry, rest is ignored
        	    rx = Integer.parseInt(binary.substring(6, 8), 2);
                ry = Integer.parseInt(binary.substring(8, 10), 2); //for NOT this will just be 0/ignored
                System.out.println("[DEBUG] Decoded: OPCODE=" + op + " RX=" + rx + " RY=" + ry );
            }else if (thirdset.contains(op)) { //third, r,count,lr,al NOTE: ENCODED IN DIFF ORDER! ALSO only one ignore bit instead of 2
            	//2, 1,1, ignore 1, 5
        	    reg = Integer.parseInt(binary.substring(6, 8), 2);
                al = Integer.parseInt(binary.substring(8, 9), 2);
                lr = Integer.parseInt(binary.substring(9, 10), 2); //10 is skipped b/c it's the ignore bit
                count = Integer.parseInt(binary.substring(11, 16), 2);
                System.out.println("[DEBUG] Decoded: OPCODE=" + op + " REG=" + reg + " A/L=" + al + " L/R=" + lr + " COUNT=" + count);
            }else if (fourthset.contains(op)) { //fourth r and device id
        	    reg = Integer.parseInt(binary.substring(6, 8), 2);
                devid = Integer.parseInt(binary.substring(11, 16), 2);
                System.out.println("[DEBUG] Decoded: OPCODE=" + op + " REG=" + reg + " DEVID=" + devid);
            }else {//    0-5 opcode, 6-7 R, 8-11 ignore?, last 5 = Devid, 2,3ignore,5
            	System.out.println("OPCODE NOT RECOGNIZED AS PART OF A SET");
            }
            
        
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
                        int jumpAddr = ea;//mem.readWord(ea); //I think it's supposed to be the EA not the value at the EA (see the loadstore instructions vs this one)
                        System.out.println("[DEBUG] JZ Taken: Jumping to Mem[" + ea + "] -> " + jumpAddr);
                        
                        if (jumpAddr != 0) {  
                            mem.PC = jumpAddr;  
                            //gui.updateGUI();
                            continue;  
                        }
                    }
                    System.out.println("[DEBUG] JZ Not Taken");
                    break;
                case "010001": //JNE (Jump If Not Equal)
                    if (mem.GPR[reg] != 0) {
                        int jumpAddr = ea;//mem.readWord(ea);
                        System.out.println("[DEBUG] JNE Taken: Jumping to Mem[" + ea + "] -> " + jumpAddr);
                        
                        if (jumpAddr != 0) {  
                            mem.PC = jumpAddr;  
                            //gui.updateGUI();
                            continue;  
                        }
                    }
                    System.out.println("[DEBUG] JNE Not Taken");
                    break;
                case "010010": 
                    jcc((byte) reg, (byte) ix, (byte) addr, (byte) indirect);
                	break;
                case "010011":	//JMA (Unconditional Jump to Address)  --will test all of these with single line load files first!
                	mem.PC = ea;
                	break;
                case "010100":	//JSR (Jump and Save Return Address)  
                    jsr((byte) ix, (byte) addr, (byte) indirect) ;
                	break;
                case "010101":	//RFS (Return From Subroutine)  //R0 <- immed(address), PC <- c(R3)
                	mem.GPR[0] = addr;
                	mem.PC = mem.GPR[3];
                	break;
                case "010110":	//SOB (Subtract One and Branch)  
                    sob((byte) reg, (byte) ix, (byte) addr, (byte) indirect);
                	break;
                case "010111":	//JGE (Jump If Greater Than or Equal To)  
                	if (mem.GPR[reg] >= 0) {
                		mem.PC = ea;
                		//gui.updateGUI();
                		continue;
                	}
                	System.out.println("[DEBUG] JGE Op, Not greater than or equal to.");
                	break;
                case "000100":	//AMR (Add Memory to Register)  
                	amr((byte) reg, (byte) ix, (byte) addr, (byte) indirect);		//HEY NEED TO GO THROUGH AND MAKE SURE IM USING MBR AND MAR CORRECTLY
                	break;
                case "000101":	//SMR (Subtract Memory from Register)  
                	smr((byte) reg, (byte) ix, (byte) addr, (byte) indirect);
                	break;
                case "000110":	//AIR (Add Immediate to Register)  
                	air((byte)reg,(byte)addr);
                	break;
                case "000111":	//SIR (Subtract Immediate from Register)  
                	sir((byte)reg,(byte)addr);
                	break;
                //so below is next encoding set! rx ry
                case "111000":	//MVT (Multiply Register by Register)  
                	mvt((byte)rx,(byte)ry);
                	break;
                case "111001":	//DVD (Divide Register by Register)  
                	dvd((byte)rx,(byte)ry);
                	break;
                case "111010":	//TRR (Test the Equality of Register and Register)  
                	trr((byte)rx,(byte)ry);
                	break;
                case "111011":	//AND (AND Register by Register) 
                	and((byte)rx,(byte)ry);
                	break;
                case "111100":	//ORR (Logical ORR Register by Register)
                	orr((byte)rx,(byte)ry);
                	break;	
                case "111101":	//NOT (Logical NOT of Register to Register)  
                	not((byte)rx);
                	break;		
                //third set is below 
                case "011001":	//SRC (Shift Register By Count )  //HEY also need to deal with underflow and see what to do for it
    	            src((byte)reg,(byte)count,(byte)al,(byte)lr);
                	break;		
                case "011010":	//RRC (Rotate Register By Count)  
    	            rrc((byte)reg,(byte)count,(byte)al,(byte)lr);
                	break;		
                //fourth set r, devid
                case "110001":	//IN (Input Character to Register From Device)  
                	 in((byte)reg, (byte)devid);
                	break;		
                case "110010":	//OUT (Output Character to Device From Register)  
                	 out((byte)reg, (byte)devid);
                	break;		
                case "110011":	//CHK (Check Device Status to Register)  
                	System.out.println("CHK IS NOT REQUIRED FOR THIS PART OF THE PROJECT.");
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