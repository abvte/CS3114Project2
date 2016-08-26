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
    	
    	if (args == null) {
    		System.out.println("This is null!!!");
    	} else {
    		System.out.println("Args: " + Arrays.toString(args));
    		System.out.println("THis is not null!");
    	}
    	
    	ParserClass classs = new ParserClass("File 1.txt");
    	classs.run();
    }
}
