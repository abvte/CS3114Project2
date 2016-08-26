import java.io.File;
import java.util.Scanner;

public class ParserClass {

	private Scanner scanner1;
	private String fileName;
	
	public ParserClass(String filename) {
		// TODO Auto-generated constructor stub
		fileName = filename;
	}
	
	public void run() {
		try{
			scanner1 = new Scanner(new File(fileName));
			while(scanner1.hasNextLine()){
				
				String[] tempSplit = scanner1.nextLine().split(" ",2);;
				
				
				// for debugging
				checkCommand(tempSplit);
				System.out.println("--------------------");
			}
		}catch ( Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
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
	
	private void checkCommand(String[] x){
		switch(x[0]){
		case "insert":
			System.out.println("inserting");
			String[] info = insertParse(x[1]);
			System.out.println(info[0]);
			System.out.println(info[1]);
			// TODO do insert stuff here
			
			break;
		case "remove":
			System.out.println("removing");
			System.out.println(x[1]);
			// TODO do remove stuff here
			
			break;
		case "print":
			System.out.println("printing");
			System.out.println(x[1]);
			// TODO do print stuff here
			
			break;
		default:
			System.out.println("Command not recognized");
			break;
		}
	}

}
