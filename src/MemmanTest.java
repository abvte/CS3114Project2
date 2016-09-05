import student.TestCase;

/**
 * @author Jinwoo Yom
 * @version 1.0
 */
public class MemmanTest extends TestCase {
    /**
     * This method sets up the tests that follow.
     */

    /**
     * Tests the initialization of Memman.
     */
    public void testMInit() { 
        Memman mem = new Memman();
        assertNotNull(mem);
        Memman.main(null);
    }  

    /**
     * Tests invalid arguments
     */
    public void testFirstError() { // tests the First Error message
        Memman mem = new Memman();
        assertNotNull(mem);
        String[] args = {"abcde"};
        Memman.main(args);
        String output = systemOut().getHistory();
        assertEquals("Invalid Arguments\n", output);
    }

    /**
     * Tests invalid file
     */
    public void testError() {
        Memman mem = new Memman();
        assertNotNull(mem);
        String[] args = {"1234", "4321", "aaaaa"};
        Memman.main(args);
        String output = systemOut().getHistory();
        assertEquals("File not found\n", output);
    }

    /**
     * Tests an unknown command
     */
    public void testWrongCommand() {
        Memman mem = new Memman();
        assertNotNull(mem);
        String[] args = {"1234", "4321", "errorFile.txt"};
        Memman.main(args);
        String output = systemOut().getHistory();
        assertEquals("Command not recognized\n", output);
    }

    /**
     * Tests that the parser can recognize and activate all commands
     */
    public void testParsing() {
        Memman mem = new Memman();
        assertNotNull(mem);
        String[] args = {"1234", "4321", "testFile.txt"};
        Memman.main(args);
        String output = systemOut().getHistory();
        String assertedOutput = "|Eagles| is added to the artist";
        assertedOutput += " database.\n|Hotel California| is added to ";
        assertedOutput += "the song database.\n|Michael Jackson| ";
        assertedOutput += "is added to the artist database.\n|Thriller|";
        assertedOutput += " is added to the song database.\n|Justin ";
        assertedOutput += "Timberlake| does not exist in the artist ";
        assertedOutput += "database.\n|SexyBack| does not exist in ";
        assertedOutput += "the song database.\n|Michael Jackson| 639\n";
        assertedOutput += "|Eagles| 738\ntotal artists: 2\n|Hotel ";
        assertedOutput += "California| 432\n|Thriller| 580\ntotal ";
        assertedOutput += "songs: 2\n(53,4268)\n(53,4268)\n";
        assertEquals(assertedOutput, output);
    }

    // Doubly Linked List
    /**
     * Tests doubly linked list commands
     */
    public void testDoublyLinkedList() {
        DoublyLinkedList<String> newlist = new DoublyLinkedList<String>();
        assertNotNull(newlist);
        newlist.add("First");
        newlist.add("Second");
        newlist.append("Third");
        // make sure there are 5 items(3 items + 2)
        assertEquals(5, newlist.getSize()); 
        // make sure that getNodedata method works
        assertEquals("Second", newlist.getCurrent().getNodeData()); 
        newlist.stepForward();
        // Make sure that stepping forward was successful
        assertEquals("First", newlist.getData());   
        newlist.stepBack();
        // Make sure that stepping back was successful
        assertEquals("Second", newlist.getData());  
        newlist.jumpToTail();   // go to tail
        newlist.stepBack();
        // make sure that jumpToTail was successful
        assertEquals("Third", newlist.getData());
        // make sure that remove returns the correct data
        assertEquals("Third", newlist.remove().getNodeData());
        // make sure that remove was successfully
        assertEquals(4, newlist.getSize()); 
        // go to head
        newlist.jumpToHead();
        // Checks for Head's data
        assertEquals(null, newlist.getData());
    }
    
    // Doubly Linked List
    /**
     * Tests removing the head
     */
    public void testLinkedListRemoveNull() {
        DoublyLinkedList<String> newlist = new DoublyLinkedList<String>();
        assertNotNull(newlist);
        Node<String> nullNode = newlist.remove();
        assertEquals(null, nullNode);   // Checks for Head's data
    }


    // HASH TABLE
    /**
     * Tests initialization of the hash table
     */
    public void testHashtableinit() {
        Hashtable myHtb = new Hashtable(1024, "Artist");
        assertNotNull(myHtb);
        assertEquals(1024, myHtb.getSize());
        assertEquals(1024, myHtb.getTable().length);

        //assertEquals(myHash.h("aaaabbbb", 101), 75);
        //assertEquals(myHash.h("aaaabbb", 101), 1640219587 % 101);
    }

    /**
     * tests adding an entry to the hashtable
     */
    public void testHashtableAdd() {
        Hashtable myHtb = new Hashtable(1024, "Artist");
        myHtb.add("key", "Maroon5");
        assertEquals(1, myHtb.getItems());
        myHtb.add("keys", "Maroon6");
        myHtb.add("keys", "Maroon7");
        assertEquals(3, myHtb.getItems());
    }

    /**
     * Tests getting an object from the hashtable
     */
    public void testHashtableGet() {
        Hashtable myHtb = new Hashtable(1024, "Artist");
        myHtb.add("key", "Maroon5");
        myHtb.add("keys", "Maroon6");
        myHtb.add("keys", "Maroon7");

        assertEquals("Maroon5", myHtb.get("key"));
        assertEquals("Maroon6", myHtb.get("keys"));
        assertEquals("Maroon6", myHtb.get("keys"));
        assertEquals(null, myHtb.get("test"));
    }

    /**
     * Tests removing an entry from the hashtable
     */
    public void testHashtableRemove() {
        Hashtable myHtb = new Hashtable(1024, "Artist");
        myHtb.add("key", "Maroon5");
        myHtb.add("keys", "Maroon6");
        myHtb.add("keys", "Maroon7");
        
        // removes the existing key value pair
        assertTrue(myHtb.remove("key"));
        // tries to remove a non-existing key value pair
        assertFalse(myHtb.remove("key"));
    }

    /**
     * tests extending the hashtable
     */
    public void testHashtableExtend() {
        Hashtable myHtb = new Hashtable(1024, "Artist");
        myHtb.add("key", "Maroon5");
        myHtb.add("keys", "Maroon6");
        myHtb.add("keys", "Maroon7");

        myHtb.extend();
        assertEquals(2048, myHtb.getSize());
    }

    // MemoryManagerTest
    /**
     * Tests the memory manager insertion
     */
    public void testMemoryManagerArtistInsert() {
        MemoryManager mm = new MemoryManager(2, 1);
        mm.insert("Micheal Jackson", true);
        mm.insert("Micheal Jackson", true);
        mm.insert("Eagles", true);
        assertEquals(2, mm.artists.getItems());
    }

    /**
     * Tests inserting a song
     */
    public void testMemoryManagerSongsInsert() {
        MemoryManager mm = new MemoryManager(2, 1);
        mm.insert("Billy Jean", false);
        mm.insert("Billy Jean", false);
        mm.insert("Hotel California", false);
        assertEquals(2, mm.songs.getItems());
    }

    /**
     * tests the MemoryManager's artist remove function
     */
    public void testMemoryManagerArtistRemove() {
        MemoryManager mm = new MemoryManager(2, 2);
        mm.insert("Micheal Jackson", true);
        mm.insert("Eagles", true);

        mm.remove("Micheal Jackson", true);
        mm.remove("Micheal Jackson", true);
        mm.remove("Eagles", true);
        assertEquals(0, mm.artists.getItems());
    }

    /**
     * Tests the MemoryManager's song remove function
     */
    public void testMemoryManagerSongRemove() {
        MemoryManager mm = new MemoryManager(2, 2);
        mm.insert("Billy Jean", false);
        mm.insert("Hotel California", false);

        mm.remove("Billy Jean", false);
        mm.remove("Billy Jean", false);
        mm.remove("Hotel California", false);
        assertEquals(0, mm.songs.getItems());
    }

    /**
     * Tests the MemoryManager's song printing
     */
    public void testMemoryManagerSongPrint() {
        MemoryManager mm = new MemoryManager(2, 2);
        mm.insert("Micheal Jackson", true);
        mm.insert("Maroon5", true);
        //mm.insert("Eagles", true);

        mm.insert("Billy Jean", false);
        mm.insert("Hotel California", false);

        assertTrue(mm.print(true, false, false));
        assertTrue(mm.print(false, true, false));
        assertTrue(mm.print(false, false, true));
    }

    
}
