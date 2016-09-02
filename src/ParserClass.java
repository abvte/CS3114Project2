import java.io.File;
import java.util.Scanner;

/**
 * Parses a given input file and initializes the MemoryManager
 * @author Adam Bishop and Jinwoo Yom
 */
public class ParserClass {

	private String fileName;	//File name of the file containing the input commands
	MemoryManager memManager;	//Instantiation of the memory manager itself
	
	/**
	 * Constructor which defines initial hash table size, pool size, and location of input file
	 * @param hashSize
	 * @param blockSize
	 * @param filename
	 */
	public ParserClass(int hashSize, int blockSize, String filename) {
		memManager = new MemoryManager(hashSize, blockSize);
		fileName = filename;
	}
	
	/**
	 * Runs the parser on the file given
	 */
	public void run() {
		try{
			try(Scanner scanner1 = new Scanner(new File(fileName))) {
				while(scanner1.hasNextLine()){
					// Only split once because artist/song names may have spaces
					String[] tempSplit = scanner1.nextLine().split(" ",2);
					runCommand(tempSplit);
				}
			}
		}catch ( Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parses the artist and song combination
	 * @param line
	 * @return String[] containing the artist and song combination
	 */
	private String[] insertParse(String line){
		// Resulting string array declared
		String[] parsedInput = new String[2];
		
		// Parsing a line of string
		String[] tempSplit_2 = line.split("<SEP>",2);
		
		// Populating resulting string array
		parsedInput[0] = tempSplit_2[0];
		parsedInput[1] = tempSplit_2[1];
		
		return parsedInput;
	}
	
	/**
	 * Parses the input String[] and runs the memory manager's command
	 * @param x
	 */
	private void runCommand(String[] x){
		//x[0] is the command
		switch(x[0]){
			case "insert": {
				String[] info = insertParse(x[1]); //Parse the artist song combination
				memManager.insert(info[0], true);
				memManager.insert(info[1], false);
				break;
			}
			case "remove": {
				String[] processed = x[1].split(" ",2);
				if(processed[0].equals("artist")) {
					memManager.remove(processed[1], true);
				}
				else if (processed[0].equals("song")) {
					memManager.remove(processed[1], false);
				}	
				break;
			}
			case "print": {
				if(x[1].equals("artist")) {
					memManager.print(true, false, false);
				}
				else if (x[1].equals("song")) {
					memManager.print(false, true, false);
				}
				else if(x[1].equals("blocks")) {
					memManager.print(false, false, true);
				}
				break;
			}
			default: {
				System.out.println("Command not recognized");
				break;
			}
		}
	}

}
