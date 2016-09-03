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
//

/**
 * This project implements a DoublyLinkedList and Hashtable to run a
 * memory manager which stores song and artists names in a way which
 * they can be easily retrieved.
 */

/**
 * The class containing the main method.
 * 
 * @author Jinwoo Yom, Adam Bishop
 * @version 1.0
 */

public class Memman {
    /**
     * @param args
     *              args[0] = initial hash size,
     *              args[1] = block size,
     *              args[2] = command file
     */
    public static void main(String[] args) {

        // args[0] = initial hash size
        // args[1] = block size
        // args[2] = command file
        if (args != null) {
            try {
                //Initialize the parser and parse the command file
                ParserClass parser = new ParserClass(Integer.parseInt(args[0]),
                        Integer.parseInt(args[1]),
                        args[2]);
                parser.run();
            } 
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
