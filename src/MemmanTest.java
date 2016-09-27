import student.TestCase;

/**
 * @author Jinwoo Yom
 * @author Adam Bishop
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
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
        String[] args = { "abcde" };
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
        String[] args = { "1234", "4321", "aaaaa" };
        Memman.main(args);
        String output = systemOut().getHistory();
        assertEquals("File not found\n", output);
    }

    /**
     * Tests an unknown command
     */
    public void testWrongCommand() {
        String[] args = { "1234", "4321", "errorFile.txt" };
        Memman.main(args);
        String output = systemOut().getHistory();
        assertEquals(
                "Command not recognized\n" + "Unknown type in remove command\n"
                        + "Unknown type in print command\n",
                output);
    }

    /**
     * Tests that the parser can recognize and activate all commands
     */
    public void testParsing() {
        String[] args = { "1234", "4321", "testFile.txt" };
        Memman.main(args);
        String output = systemOut().getHistory();
        String assertedOutput = "|Eagles| is added to the artist"
                + " database.\n|Hotel California| is added to the"
                + " song database.\n|Michael Jackson| is added to"
                + " the artist database.\n|Thriller| is added to the"
                + " song database.\n|Justin Timberlake| does not exist"
                + " in the artist database.\n|SexyBack| does not exist"
                + " in the song database.\n|Michael Jackson| 639\n|Eagles|"
                + " 738\ntotal artists: 2\n|Hotel California| 432\n|Thriller|"
                + " 580\ntotal songs: 2\n" + "(53,4268)\n";
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
        newlist.jumpToTail(); // go to tail
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
        assertEquals(null, nullNode); // Checks for Head's data
    }

    // HASH TABLE
    /**
     * Tests initialization of the hash table
     */
    public void testHashtableinit() {
        MemoryManager memManager = new MemoryManager(1024, 32);
        Hashtable myHtb = new Hashtable(1024, "Artist", memManager);
        assertNotNull(myHtb);
        assertEquals(1024, myHtb.getSize());
        assertEquals(1024, myHtb.getTable().length);
    }

    /**
     * tests adding an entry to the hashtable
     */
    public void testHashtableAdd() {
        MemoryManager memManager = new MemoryManager(1024, 32);
        Hashtable myHtb = new Hashtable(1024, "Artist", memManager);
        myHtb.add("key", "Maroon5");
        assertEquals(1, myHtb.getItems());
        myHtb.add("keys", "Maroon6");
        myHtb.add("keys", "Maroon7");
        assertEquals(3, myHtb.getItems());
    }

    /**
     * tests adding an entry to the hashtable with tombstone
     */
    public void testHashtableAddWithTombstone() {
        MemoryManager memManager = new MemoryManager(1024, 32);
        Hashtable myHtb = new Hashtable(10, "Artist", memManager);
        myHtb.add("Maroon5", new Handle("Maroon5".getBytes(),
                new byte[] { 0, 7 }, 0, memManager.getPool()));
        assertEquals(1, myHtb.getItems());
        myHtb.remove("Maroon5", memManager.getPool());
        assertNotNull(myHtb.getTable()[8]);
        assertEquals(0, myHtb.getItems());
        myHtb.add("Maroon5", new Handle("Maroon5".getBytes(),
                new byte[] { 0, 7 }, 0, memManager.getPool()));
        assertEquals(1, myHtb.getItems());
        Handle newHandle = (Handle) myHtb.getTable()[8].getValue();
        assertEquals(memManager.handle2String(newHandle, memManager.getPool()),
                "Maroon5");
    }

    /**
     * Tests getting an object from the hashtable
     */
    public void testHashtableGet() {
        MemoryManager memManager = new MemoryManager(1024, 32);
        Hashtable myHtb = new Hashtable(1024, "Artist", memManager);
        Handle one = (Handle) myHtb.add("Maroon5",
                new Handle("Maroon5".getBytes(), new byte[] { 0, 7 }, 0,
                        memManager.getPool()));
        Handle two = (Handle) myHtb.add("Maroon6",
                new Handle("Maroon6".getBytes(), new byte[] { 0, 7 }, 9,
                        memManager.getPool()));

        Handle handle5 = (Handle) myHtb.get("Maroon5", memManager.getPool());
        Handle handle6 = (Handle) myHtb.get("Maroon6", memManager.getPool());
        assertEquals(memManager.handle2String(one, memManager.getPool()),
                memManager.handle2String(handle5, memManager.getPool()));
        assertEquals(memManager.handle2String(two, memManager.getPool()),
                memManager.handle2String(handle6, memManager.getPool()));
        assertEquals(null, myHtb.get("test", memManager.getPool()));
    }

    /**
     * Tests removing an entry from the hashtable
     */
    public void testHashtableRemove() {
        MemoryManager memManager = new MemoryManager(1024, 32);
        Hashtable myHtb = new Hashtable(1024, "Artist", memManager);
        Handle one = (Handle) myHtb.add("Maroon5",
                new Handle("Maroon5".getBytes(), new byte[] { 0, 7 }, 0,
                        memManager.getPool()));
        Handle two = (Handle) myHtb.add("Maroon6",
                new Handle("Maroon6".getBytes(), new byte[] { 0, 7 }, 9,
                        memManager.getPool()));

        // removes the existing key value pair
        assertEquals(myHtb.remove("Maroon5", memManager.getPool()), one);
        // tries to remove a non-existing key value pair
        assertEquals(myHtb.remove("Maroon6", memManager.getPool()), two);
    }

    /**
     * tests extending the hashtable
     */
    public void testHashtableExtend() {
        MemoryManager memManager = new MemoryManager(5, 32);
        Hashtable myHtb = new Hashtable(5, "Artist", memManager);
        Handle one = (Handle) myHtb.add("Maroon5",
                new Handle("Maroon5".getBytes(), new byte[] { 0, 7 }, 0,
                        memManager.getPool()));
        Handle two = (Handle) myHtb.add("Maroon6",
                new Handle("Maroon6".getBytes(), new byte[] { 0, 7 }, 9,
                        memManager.getPool()));
        Handle three = (Handle) myHtb.add("Maroon7",
                new Handle("Maroon7".getBytes(), new byte[] { 0, 7 }, 18,
                        memManager.getPool()));
        assertEquals(3, myHtb.getItems());
        myHtb.extend(memManager.getPool());
        assertEquals(10, myHtb.getSize());
        myHtb.remove("Maroon5", memManager.getPool());
        myHtb.remove("Maroon6", memManager.getPool());
        myHtb.remove("Maroon7", memManager.getPool());
        assertEquals(0, myHtb.getItems());
        myHtb.extend(memManager.getPool());
        assertEquals(20, myHtb.getSize());
    }

    /**
     * Tests a failure to search for an element in the Hash table
     */

    public void testHashtableQuadraticFail() {
        MemoryManager memManager = new MemoryManager(1024, 32);
        Hashtable myHtb = new Hashtable(11, "Artist", memManager);
        myHtb.add("a", "");
        myHtb.add("a", "");
        myHtb.add("a", "");
        myHtb.add("a", "");
        myHtb.add("a", "");
        myHtb.add("a", "");
        assertNull(myHtb.add("a", ""));
        for (int i = 0; i < 11; i++) {
            myHtb.getTable()[i] = new Hash(null);
        }
        assertNull(myHtb.get("e", memManager.getPool()));
        assertNull(myHtb.remove("e", memManager.getPool()));
    }

    // MemoryManagerTest
    /**
     * Tests the memory manager insertion
     */
    public void testMemoryManagerArtistInsert() {
        MemoryManager mm = new MemoryManager(2, 1);
        mm.insert("Micheal Jackson", true);
        mm.insert("Micheal Jackson", true);
        mm.insert("Suck my kiss", false);
        mm.insert("Suck my kiss", false);
        mm.insert("Eagles", true);
        assertEquals(2, mm.artists.getItems());
        assertEquals(1, mm.songs.getItems());
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
        assertEquals(mm.artists.getItems(), 1);
        mm.remove("Micheal Jackson", true);
        mm.remove("Eagles", true);
        assertEquals(mm.artists.getItems(), 0);
    }

    /**
     * Tests the MemoryManager's song remove function
     */
    public void testMemoryManagerSongRemove() {
        MemoryManager mm = new MemoryManager(2, 2);
        mm.insert("Billy Jean", false);
        mm.insert("Hotel California", false);

        mm.remove("Billy Jean", false);
        assertEquals(mm.songs.getItems(), 1);
        mm.remove("Billy Jean", false);
        mm.remove("Hotel California", false);
        assertEquals(mm.songs.getItems(), 0);
    }

    /**
     * Tests the MemoryManager's song printing
     */
    public void testMemoryManagerSongPrint() {
        MemoryManager mm = new MemoryManager(2, 2);
        mm.insert("Micheal Jackson", true);
        mm.insert("Maroon5", true);
        // mm.insert("Eagles", true);

        mm.insert("Billy Jean", false);
        mm.insert("Hotel California", false);

        assertTrue(mm.print(true, false, false));
        assertTrue(mm.print(false, true, false));
        assertTrue(mm.print(false, false, true));
    }

    /**
     * Tests output when all blocks are taken
     */
    public void testMemoryManagerPrintNoFreeBlocks() {
        MemoryManager mm = new MemoryManager(5, 5);
        mm.insert("xyz", true);

        assertTrue(mm.print(false, false, true));
        String output = systemOut().getHistory();
        assertEquals("|xyz| is added to the artist database." + "\n(5,0)\n",
                output);
    }

    /**
     * Tests the print function with blocks that have used blocks between them
     */
    public void testMemoryManagerFreeBlocksMergePrint() {
        MemoryManager mm = new MemoryManager(10, 15);
        mm.insert("xyz", true);
        mm.insert("abc", true);
        mm.insert("qwe", true);
        mm.remove("xyz", true);
        mm.remove("qwe", true);

        assertTrue(mm.print(false, false, true));
        String output = systemOut().getHistory();
        assertTrue(output.contains("(0,5) -> (10,5)"));
    }

    /**
     * Tests that the free block list will merge with both a block to the right
     * and a block to the left.
     */
    public void testMemoryManagerMerge() {
        MemoryManager mm = new MemoryManager(10, 15);
        mm.insert("xyz", true);
        mm.insert("abc", true);
        mm.insert("qwe", true);
        mm.remove("xyz", true);
        mm.remove("qwe", true);

        assertTrue(mm.print(false, false, true));
        String output = systemOut().getHistory();
        assertTrue(output.contains("(0,5) -> (10,5)"));
        mm.remove("abc", true); // Merge here
        assertTrue(mm.print(false, false, true));
        output = systemOut().getHistory();
        assertTrue(output.contains("(0,15)\n"));
    }

    /**
     * Tests the parser
     */
    public void testParser() {
        ParserClass pc = new ParserClass(2, 2, "testFile.txt");
        pc.run();
        assertEquals(pc, pc);
    }
}
