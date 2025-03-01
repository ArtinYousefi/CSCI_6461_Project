package tryingtowork;
import java.io.*;
import java.util.*;

public class Control {

	public static Memory mem = new Memory();
	
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


	//use this as testing for now, THO need to create test files
	//rn need to add registers to memory: IR,PC,MBR,MFR,MAR
	//in here need to have a function that reads the input file line by line
	//each line will be converted to binary then read the correct bits to see what the function is
	//call function and pass info
	//then in function read the rest of the bits to do as needed.
	//for now, gonna do a case statement for the 5 functions
    
    //so load file needs to run and load the instructions into memory at the given addresses (First col)
    //then second loop goes through and actually executes the instructions and uses the IR,PC,MBR ETC
    
    
    //function to compute effective address - values are decimal
    public static short computeEA (byte ix, byte addr, byte indirect) {
    	short res = 0;    	
    	
    	if(ix == 0) {
    		res = mem.readWord(addr);
    	}else if (ix == 1 || ix == 2 || ix == 3) {
    		res = (short) (ix + mem.readWord(addr));
    	}
    	
    	if(indirect ==  1) {
    		short tmp = mem.readWord(res);
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
           
            short addr = Short.parseShort(String.valueOf(addrStr), 2);
            short inst = Short.parseShort(String.valueOf(instStr), 2);
            
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
		System.out.println(mem.GPR[r]);	
		mem.GPR[r] = mem.readWord(mem.MAR); //might have to use MBR and stuff for this!
		System.out.println(mem.GPR[r]);	
	}
	
	//store register to memory
	public static void str(byte r, byte ix, byte addr, byte indirect) {
		//short ea = computeEA(ix,addr,indirect);
		System.out.println(mem.readWord(mem.MAR));
		mem.writeWord(mem.MAR, mem.GPR[r]);
		System.out.println(mem.readWord(mem.MAR));
	}
	//load register with address
	public static void lda(byte r, byte ix, byte addr, byte indirect) {
		//short ea = computeEA(ix,addr,indirect);
		System.out.println(mem.GPR[r]);	
		mem.GPR[r] = mem.MAR;
		System.out.println(mem.GPR[r]);	
	}
	//load index register from memory
	public static void ldx(byte ix, byte addr, byte indirect) {		//when loading and storing need -1 due to arrays starting at 0
		//short ea = computeEA(ix,addr,indirect);
		System.out.println(mem.IX[ix-1]);	
		mem.IX[ix-1] = mem.readWord(mem.MAR);
		System.out.println(mem.IX[ix-1]);	
	}
	//store index register to memory
	public static void stx(byte ix, byte addr, byte indirect) {
		//short ea = computeEA(ix,addr,indirect);
		System.out.println(mem.readWord(mem.MAR));
		mem.writeWord(mem.MAR, mem.IX[ix-1]);
		System.out.println(mem.readWord(mem.MAR));
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
			System.out.println(binary);
			
			//SO rn breaks b/c data has a 000000 operand need to swap the HLT check to be here (and check if the entire instruction is 0)
			//then below if the operand is 000000 store data at that address
			if (binary == "0000000000000000") { //HLT need to figure out better way later
	        	mem.PC = 0;
	        	return; //need to properly finish but for now just force an end
			}
			//operand
	        String op = binary.substring(0,6);
	        Byte op_val = Byte.parseByte(String.valueOf(op), 2);
	        //index (IX)
	        String ix = binary.substring(6,8);
	        Byte ix_val = Byte.parseByte(String.valueOf(ix), 2);
	        //register (GPR)
	        String r = binary.substring(8,10);
	        Byte r_val = Byte.parseByte(String.valueOf(r), 2);
	        //indirect
	        String i = binary.substring(10,11);
	        Byte i_val = Byte.parseByte(String.valueOf(i), 2);
	        //address
	        String addr = binary.substring(11,16);
	        Byte addr_val = Byte.parseByte(String.valueOf(addr), 2);
	        
			//get instruction, convert to binary, set all vars, do case to determine which function to call
	        short ea = computeEA(ix_val,addr_val,i_val);
	        mem.MAR = ea; //updating MAR to be effective address
	        
	        System.out.println("Before");
	       
	        System.out.println(op);
	        
	        System.out.println(Arrays.toString(mem.IX));
	        System.out.println(Arrays.toString(mem.GPR));
	        
	        switch (op) {
		        case "000001":
		        	ldr(r_val, ix_val, addr_val, i_val);
		        	break;
		        case "000010":
		        	str(r_val, ix_val, addr_val, i_val);
		        	break;
		        case "000011":
		        	lda(r_val, ix_val, addr_val, i_val);
		        	break;
		        case "100001":
		        	ldx(ix_val, addr_val, i_val);
		        	break;
		        case "100010":
		        	stx(ix_val, addr_val, i_val);
		        	break;
		        case "000000":
		        	//store data at address
		        	mem.writeWord(mem.PC, addr_val);
		        	
		        default:
		        	System.out.println("operand not recognized");        
	        }
	        
	        System.out.println("After");
	        
	        System.out.println(Arrays.toString(mem.IX));
	        System.out.println(Arrays.toString(mem.GPR));
	        
	        mem.PC++;
	     
			//DURING this set all registers properly
			//make this a do while mem.readword(address) != 0, if it = 0 exit (act as a HLT) 
		}
		System.out.println("file finished"); 
	}
    
    public static void main(String[] args) {
        // Create a new memory instance
        Memory mem = new Memory();
        
        
        String file = "src\\load4.txt"; //will swap to args[0] once it works, for others 
        try {
			loadLF(file); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        runSimulator();

        
    }
	
	
}
