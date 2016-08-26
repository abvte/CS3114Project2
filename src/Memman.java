//On my honor:
//
//- I have not used source code obtained from another student,
//or any other unauthorized source, either modified or
//unmodified.
//
//- All source code and documentation used in my program is
//either my original work, or was derived by me from the
//source code published in the textbook for this course.
//
//- I have not discussed coding details about this project with
//anyone other than my partner (in the case of a joint
//submission), instructor, ACM/UPE tutors or the TAs assigned
//to this course. I understand that I may discuss the concepts
//of this program with other students, and that another student
//may help me debug my program so long as neither of us writes
//anything during the discussion or modifies any computer file
//during the discussion. I have violated neither the spirit nor
//letter of this restriction.

import java.util.Arrays;

/**
 * {Project Description Here}
 */

/**
 * The class containing the main method.
 * 
 * @author Jinwoo Yom
 * @version 1.0
 */

public class Memman {
    /**
     * @param args
     * 	        The name of the command file passed in as a command
     * 	        line argument.
     */
    public static void main(String[] args) {
    	
    	// args[0] = initial hash size
    	// args[1] = block size
    	// args[2] = command file
    	if(args != null){
    		try{
		    	System.out.println(args[0]);
		    	System.out.println(args[1]);
		    	ParserClass parser = new ParserClass(args[2]);
		    	parser.run();
    		} catch(Exception e){
    			System.out.println(e.getMessage());
    		}
    	}
    	
    	String s = "hello";
    	System.out.println(s.getBytes().length);
        Byte[] bytes = new Byte[s.length()];
        Arrays.setAll(bytes, n -> s.getBytes()[n]);
    	MemoryBlock newMem = new MemoryBlock(bytes,s.length(),false);
    	System.out.println(newMem.getMemory());
    	System.out.println(newMem.getMemoryLength());
    	
//    	DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
//    	dll.append(1);
//    	dll.append(2);
//    	dll.append(3);
//    	dll.append(4);
//    	dll.stepBack();
//    	dll.stepBack();
//    	dll.stepForward();
//    	dll.remove();
//    	do{
//    		System.out.println(dll.getCurrent().getNodeData());
//    	} while(dll.stepBack());

    }
}
