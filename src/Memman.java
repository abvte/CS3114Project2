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

    	MemoryManager newManager = new MemoryManager(10, 32, "");
    	
//    	newManager.insert("Blind Lemon Jefferson",true);
//    	newManager.insert("Ma Rainey",true);
//    	newManager.insert("Long Lonesome Blues",false);
//    	
//    	newManager.print(false, false, true);
//    	newManager.print(false, true, true);
//    	newManager.print(true, false, true);
//    	
//    	newManager.remove("Blind Lemon Jefferson",true);
//    	newManager.print(false, false, true);
//    	newManager.print(false, true, true);
//    	newManager.print(true, false, true);
//    	newManager.remove("Long Lonesome Blues",false);
//    	newManager.print(false, false, true);
//    	newManager.print(false, true, true);
//    	newManager.print(true, false, true);
//    	newManager.remove("Ma Rainey",true);
//    	newManager.print(false, false, true);
//    	newManager.print(false, true, true);
//    	newManager.print(true, false, true);
    	
    	newManager.remove("When Summer's Through", false);
    	newManager.print(false, false, true);
    	newManager.print(false, true, true);
    	newManager.print(true, false, true);
    	newManager.insert("Blind Lemon Jefferson",true);
    	newManager.insert("Long Lonesome Blues",false);
    	newManager.insert("Ma Rainey",true);
    	newManager.insert("Ma Rainey's Black Bottom",false);
    	newManager.insert("Charley Patton",true);
    	newManager.insert("Mississippi Boweavil Blues",false);
    	newManager.insert("Sleepy John Estes",true);
    	newManager.insert("Street Car Blues",false);
    	newManager.insert("Bukka White",true);
    	newManager.insert("Fixin' To Die Blues",false);
    	newManager.print(true, true, true);
    	newManager.print(false, true, true);
    	newManager.insert("Guitar Slim",true);
    	newManager.insert("The Things That I Used To Do",false);
    	newManager.remove("Style Council",true);
    	newManager.remove("Ma Rainey",true);
    	newManager.remove("Mississippi Boweavil Blues",false);
    	newManager.remove("(The Best Part Of) Breakin' Up",false);
    	newManager.print(false, false, true);
    	newManager.insert("Blind Lemon Jefferson",true);
    	newManager.insert("Got The Blues",false);
    	newManager.insert("Little Eva",true);
    	newManager.insert("The Loco-Motion",false);
    	newManager.print(true, true, true);
    	newManager.print(false, true, true);
    	newManager.insert("Jim Reeves",true);
    	newManager.insert("Jingle Bells",false);
    	newManager.insert("Mongo Santamaria",true);
    	newManager.insert("Watermelon Man",false);
    	newManager.print(false, false, true);
    /*
    	newManager.print(true, false, false);
    	newManager.print(false, true, false);
    	newManager.print(false, false, true);
   
    	newManager.remove("test2",true);
    	
    	newManager.print(true, false, false);
    	newManager.print(false, true, false);
    	newManager.print(false, false, true);
 
    	newManager.insert("test3767",true);
    	
    	newManager.print(true, false, false);
    	newManager.print(false, true, false);
    	newManager.print(false, false, true);
    	*/
    	/*
    	Hashtable ht = new Hashtable(10,"p");
    	ht.add("teststr", 123);
    	ht.add("teststrr", 123);
    	ht.add("asdfsdg", 321);
    	ht.add("gdsagds", 123);
    	ht.add("sadaf", 321);
    	ht.add("trgrtrrh", 123);
    	System.out.println(ht.get("trgrtrrh2"));
    	System.out.println(ht.get("sadaf"));
    	System.out.println(ht.get("gdsagds"));
    	System.out.println(ht.get("asdfsdg"));
    	System.out.println(ht.get("teststr"));
    	ht.add("trhthr", 321);
    	ht.add("htrhrthrthrh", 123);
    	ht.add("hthrthrhrth", 321);
    	System.out.println(ht.get("teststr"));
    	System.out.println(ht.get("hthrthrhrth"));
    	System.out.println(ht.get("htrhrthrthrh"));
    	System.out.println(ht.get("trhthr"));
    	System.out.println(ht.get("trgrtrrh"));
    	System.out.println(ht.get("sadaf"));
    	System.out.println(ht.get("gdsagds"));
    	System.out.println(ht.get("asdfsdg"));
    	System.out.println(ht.get("teststr"));
    	*/
    	
    	/*String s = "hello";
    	System.out.println(s.getBytes().length);
        Byte[] bytes = new Byte[s.length()];
        Arrays.setAll(bytes, n -> s.getBytes()[n]);
    	MemoryBlock newMem = new MemoryBlock(bytes,s.length(),false);
    	System.out.println(newMem.getMemory());
    	System.out.println(newMem.getMemoryLength());*/

    	
//    	DoublyLinkedList<MemoryBlock> dll = new DoublyLinkedList<MemoryBlock>();
//    	dll.append(new MemoryBlock(new Byte[1],true));
//    	dll.stepBack();
//    	dll.getCurrent().getNodeData().setFree(false);
//    	System.out.println(dll.getCurrent().getNodeData().getFree();
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
