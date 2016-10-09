import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Parses a given input file and initializes the MemoryManager
 * 
 * @author Adam Bishop
 * @author Jinwoo Yom
 * @version 1.0
 */
public class ParserClass {

    private String fileName; // File name of the file
                             // containing the input commands

    /**
     * Constructor which defines location of input file
     * 
     * @param filename
     *            Points to the command file
     */
    public ParserClass(String filename) {
        fileName = filename;
    }

    /**
     * Runs the parser on the file given
     */
    public void run() {
        Scanner scanner1 = null;
        try {
            scanner1 = new Scanner(new File(fileName));
            while (scanner1.hasNextLine()) {
                // Only split once because artist/song
                // names may have spaces
                String[] commandSplit = scanner1.nextLine().split(" ", 2);
                runCommand(commandSplit);
            }
            scanner1.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    /**
     * Parses the artist and song combination
     * 
     * @param line
     *            to parse
     * @return String[] containing the artist and song combination
     */
    private String[] parseArtistSong(String line) {
        // Resulting string array declared
        String[] parsedInput = new String[2];

        // Parsing a line of string
        String[] temp = line.split("<SEP>", 2);

        // Populating resulting string array
        parsedInput[0] = temp[0];
        parsedInput[1] = temp[1];

        return parsedInput;
    }

    /**
     * Parses the input String[] and runs the memory manager's command
     * 
     * @param x
     *            command
     */
    public void runCommand(String[] x) {
        // x[0] is the command
        if (x[0].equals("insert")) {
            // Parse the artist song combination
            String[] info = parseArtistSong(x[1]);
            SearchTree.world.insert(info[0], true);
            SearchTree.world.insert(info[1], false);
            SearchTree.world.insertToTree(info[0], info[1]);
        }
        else if (x[0].equals("remove")) {
            String[] processed = x[1].split(" ", 2);
            if (processed[0].equals("artist")) {
                SearchTree.world.remove(processed[1], true);
            }
            else if (processed[0].equals("song")) {
                SearchTree.world.remove(processed[1], false);
            }
            else {
                System.out.println("Unknown type in remove command");
            }
        }
        else if (x[0].equals("print")) {
            if (x[1].equals("artist")) {
                SearchTree.world.print(true, false, false);
            }
            else if (x[1].equals("song")) {
                SearchTree.world.print(false, true, false);
            }
            else if (x[1].equals("blocks")) {
                SearchTree.world.print(false, false, true);
            }
            else if (x[1].equals("tree")) {
                SearchTree.world.searchTree.print();
            }
            else {
                System.out.println("Unknown type in print command");
            }
        }
        else if (x[0].equals("list")) {
            String[] processed = x[1].split(" ", 2);
            if (processed[0].equals("artist")) {
                // List from tree
                SearchTree.world.listTree(processed[1],
                        SearchTree.world.memManager, true);
            }
            else if (processed[0].equals("song")) {
                // List from tree
                SearchTree.world.listTree(processed[1],
                        SearchTree.world.memManager, false);
            }
            else {
                System.out.println("Unknown type in list command");
            }
        }
        else if (x[0].equals("delete")) {
            // Parse the artist song combination
            String[] info = parseArtistSong(x[1]);
            // Delete from the tree and possibly delete from hashtables
            SearchTree.world.deleteTree(info[0], info[1]);
        }
        else {
            System.out.println("Command not recognized");
        }
    }

}