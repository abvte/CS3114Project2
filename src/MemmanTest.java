import student.TestCase;

/**
 * @author Jinwoo Yom
 * @version 1.0
 */
public class MemmanTest extends TestCase {
    /**
     * This method sets up the tests that follow.
     */
    public void setUp() {
        // Nothing Here
    }
    
    /**
     * This method gets code coverage of the class declaration.
     */
    
    public void testMInit() {
        Memman mem = new Memman();
        assertNotNull(mem);
        Memman.main(null);
    }
    
    public void testFirstError() {
        Memman mem = new Memman();
        assertNotNull(mem);
        String[] args = {"abcde"};
        Memman.main(args);
    }
    
    public void testError() {
        Memman mem = new Memman();
        assertNotNull(mem);
        String[] args = {"1234", "4321", "aaaaa"};
        Memman.main(args);
    }
    
    public void testWrongCommand() {
        Memman mem = new Memman();
        assertNotNull(mem);
        String[] args = {"1234", "4321", "errorFile.txt"};
        Memman.main(args);
    }
    
    public void testParsing() {
        Memman mem = new Memman();
        assertNotNull(mem);
        String[] args = {"1234", "4321", "testFile.txt"};
        Memman.main(args);
    }
    
    // Doubly Linked List
    public void testDoublyLinkedList(){
    	DoublyLinkedList<String> newlist = new DoublyLinkedList<String>();
    	assertNotNull(newlist);
    	newlist.add("First");
    	newlist.add("Second");
    	newlist.append("Third");
    	assertEquals(5, newlist.getSize());								// make sure there are 5 items(3 items + 2)
    	assertEquals("Second", newlist.getCurrent().getNodeData());		// make sure that getNodedata method works
    	newlist.stepForward();
    	assertEquals("First", newlist.getData());						// Make sure that stepping forward was successful
    	newlist.stepBack();
    	assertEquals("Second", newlist.getData());						// Make sure that stepping back was successful
    	newlist.jumpToTail();											// go to tail
    	newlist.stepBack();
    	assertEquals("Third", newlist.getData());						// make sure that jumpToTail was successful
    	assertEquals("Third", newlist.remove().getNodeData());			// make sure that remove returns the correct data
    	assertEquals(4, newlist.getSize());								// make sure that remove was successfully 
    	newlist.jumpToHead();											// go to head
    	assertEquals(null, newlist.getData()); 							// Checks for Head's data
    }
    
    // HASH TABLE
    public void testHashtableinit() {
    	Hashtable myHtb = new Hashtable(1024, "Artist");
        assertNotNull(myHtb);
        assertEquals(1024, myHtb.getSize());
        assertEquals(1024, myHtb.getTable().length);
        
        //assertEquals(myHash.h("aaaabbbb", 101), 75);
        //assertEquals(myHash.h("aaaabbb", 101), 1640219587 % 101);
    }
    
    public void testHashtableAdd(){
    	Hashtable myHtb = new Hashtable(1024, "Artist");
    	myHtb.add("key", "Maroon5");
    	assertEquals(1,myHtb.getItems());
    	myHtb.add("keys", "Maroon6");
    	myHtb.add("keys", "Maroon7");
    	assertEquals(3,myHtb.getItems());
    }
    
    public void testHashtableGet(){
    	Hashtable myHtb = new Hashtable(1024, "Artist");
    	myHtb.add("key", "Maroon5");
    	myHtb.add("keys", "Maroon6");
    	myHtb.add("keys", "Maroon7");
    	
    	assertEquals("Maroon5", myHtb.get("key"));
    	assertEquals("Maroon6", myHtb.get("keys"));
    	assertEquals("Maroon6", myHtb.get("keys"));
    	assertEquals(null, myHtb.get("test"));
    }
    
    public void testHashtableRemove(){
    	Hashtable myHtb = new Hashtable(1024, "Artist");
    	myHtb.add("key", "Maroon5");
    	myHtb.add("keys", "Maroon6");
    	myHtb.add("keys", "Maroon7");
    	
    	assertTrue(myHtb.remove("key"));			// removes the existing key value pair
    	assertFalse(myHtb.remove("key"));			// tries to remove a non-existing key value pair
    }
    
    public void testHashtableExtend(){
    	Hashtable myHtb = new Hashtable(1024, "Artist");
    	myHtb.add("key", "Maroon5");
    	myHtb.add("keys", "Maroon6");
    	myHtb.add("keys", "Maroon7");
    	
    	myHtb.extend();
    	assertEquals(2048, myHtb.getSize());
    }
    
    // MemoryManagerTest
    public void testMemoryManager(){
    	MemoryManager mm = new MemoryManager(2048, 128);
    	
    }
}
