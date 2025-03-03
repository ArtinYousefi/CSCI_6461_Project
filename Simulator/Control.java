package tryingtowork;
import java.io.*;
import java.util.*;

//note all shorts have been swapped to ints and will implement a check so that they don't go over the 16 bit size of 65,535
public class Control {
	
	public static Memory mem;
	public static String file;
	
	//constructor
	public Control() {
		 mem = new Memory();
	}

//	public static Memory mem = new Memory();
	
    private static String binaryToOctal(String binary) {
        long decimal = Long.parseLong(binary, 2); // Convert binary to decimal
        return String.format("%06o", decimal); // Convert decimal to octal format
    }
    
    private static String octalToBinary(String octal) {
        long decimal = Long.parseLong(octal, 8); // Convert binary to decimal
        
        //Converting decimal to binary -> padding to 16 bits with spaces and replacing them with 0s
        String binary = String.format("%16s",Long.toBinaryString(decimal)).replace(" ", "0");

        //binary = "0b" + binary; //needed to store correctly
        
        return binary;
    }


    
    //function to compute effective address - values are decimal
    public static int computeEA (byte ix, byte addr, byte indirect) {
    	int res = 0;    	
    	
    	if(ix == 0) {
    		res = addr;
    	}else if (ix == 1 || ix == 2 || ix == 3) {
    		res = (ix + addr); //was mem.readWord(addr) but that was incorrect - misread doc
    	}
    	
    	if(indirect ==  1) {
    		int tmp = mem.readWord(res);
    		res = tmp;
    	}
    	return res;
    }
    
    //loads loadfile into memory (runs when init is clicked)
    //converts octal to binary to store in the short
    public static void loadLF (String file) throws IOException {	//swap to boolean and return false on failure
    		
    	//swap res to be both the address line and the instructions
        //short tmp = Short.parseShort(String.valueOf(res), 2);//so could us this to translate the binary to store as a short

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        int counter = 0;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith(";")) continue; // Skip empty and comment lines
            
            String[] parts = line.split(" ");
            
            String addrStr = octalToBinary(parts[0]);
            String instStr = octalToBinary(parts[1]);
           
            int addr = Integer.parseInt(String.valueOf(addrStr), 2); //storing as a decimal
            int inst = Integer.parseInt(String.valueOf(instStr), 2);

            
            if (counter == 0) { //at first line, init the pc
            	mem.PC = addr;
            }
            
            mem.writeWord(addr,inst);  //store instructions at address in memory
            counter++;
//            System.out.println(String.format("%16s",Long.toBinaryString(mem.readWord(addr))).replace(" ", "0"));
        }
        reader.close();
    }
    
    //Load register from memory
	public static void ldr(byte r, byte ix, byte addr, byte indirect) {
		//short ea = computeEA(ix,addr,indirect);
		//System.out.println(mem.GPR[r]);	
		mem.GPR[r] = mem.readWord(mem.MAR); //might have to use MBR and stuff for this!
		//System.out.println(mem.GPR[r]);	
	}
	
	//store register to memory
	public static void str(byte r, byte ix, byte addr, byte indirect) {
		mem.writeWord(mem.MAR, mem.GPR[r]);
	}
	//load register with address
	public static void lda(byte r, byte ix, byte addr, byte indirect) {	
		mem.GPR[r] = mem.MAR;
	}
	//load index register from memory
	public static void ldx(byte ix, byte addr, byte indirect) {		//when loading and storing need -1 due to arrays starting at 0
		mem.IX[ix-1] = mem.readWord(mem.MAR);
	}
	//store index register to memory
	public static void stx(byte ix, byte addr, byte indirect) {
		mem.writeWord(mem.MAR, mem.IX[ix-1]);
	}
    
	//runs the simulator, populates the registers and executes the instructions
	public static void runSimulator() {
		//need to turn this into a while loop while mem.readword(MAR) != 0
		//also for now, pc will just be incremented, in future version will have to be an if or case since jumps can occur
		
		//read PC to get address of first instructions
		while (mem.readWord(mem.PC) != 0) { //for now, exit once you reach mem with no instructions
			mem.MAR = mem.PC;
			mem.MBR = mem.readWord(mem.MAR);
			mem.IR = mem.MBR;
			
			String binary = String.format("%16s",Long.toBinaryString(mem.IR)).replace(" ", "0");
			//convert instructions in IR to binary and set all the variables (need both for comparisons and functions)
			//System.out.println(binary);
			
			//SO rn breaks b/c data has a 000000 operand need to swap the HLT check to be here (and check if the entire instruction is 0)
			//then below if the operand is 000000 store data at that address
			if (binary.equals("0000000000000000")) { //HLT need to figure out better way later
	        	System.out.println("HLT found"); //not quite accurate but needed for now
				mem.PC = 0;
	        	return; //need to properly finish but for now just force an end
			}
			//operand
	        String op = binary.substring(0,6);
	        Byte op_val = Byte.parseByte(String.valueOf(op), 2);
	        //register (GPR)
	        String r = binary.substring(6,8);
	        Byte r_val = Byte.parseByte(String.valueOf(r), 2);
	        //index (IX)
	        String ix = binary.substring(8,10);
	        Byte ix_val = Byte.parseByte(String.valueOf(ix), 2);
	        //indirect
	        String i = binary.substring(10,11);
	        Byte i_val = Byte.parseByte(String.valueOf(i), 2);
	        //address
	        String addr = binary.substring(11,16);
	        Byte addr_val = Byte.parseByte(String.valueOf(addr), 2);
	        
			//get instruction, convert to binary, set all vars, do case to determine which function to call
	        int ea = computeEA(ix_val,addr_val,i_val);
	        mem.MAR = ea; //updating MAR to be effective address
	        
	        //swaped from case to use equals instead
	        if(op.equals("000001")) {
	        	ldr(r_val, ix_val, addr_val, i_val);
	        }else if (op.equals("000010")) {
	        	str(r_val, ix_val, addr_val, i_val);
	        }else if (op.equals("000011")) {
	        	lda(r_val, ix_val, addr_val, i_val);
	        }else if (op.equals("100001")) {
	        	ldx(ix_val, addr_val, i_val);
	        }else if (op.equals("100010")) {
	        	stx(ix_val, addr_val, i_val);
	        }else if (op.equals("000000")) { //DONT NEED THIS THIS HAS ALREADY BEEN DONE mem.writeWord(mem.PC, addr_val);
	        	System.out.println("data already loaded");
	        }else {
	        	System.out.println("operand not recognized");
	        }

	        mem.PC++;

		}
		System.out.println("file finished"); 
	}
    
    public static void main(String[] args) {
        // Create a new memory instance
        
//    	Control cont = new Control();
//        //the mem and loadLF can be called by the GUI when the file is "selected"/init is selected
//        String file = "src\\load4.txt"; //will swap to args[0] once it works, for others 
//        
//        try { //will swap to gui calling this on init (also need to add function to grab file from file input)
//			loadLF(file); 
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//        
//        runSimulator();  //should run when run is selected in GUI
//
//        
    }
	
	
}
