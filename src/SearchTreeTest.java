import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import student.TestCase;

/**
 * @author Jinwoo Yom
 * @author Adam Bishop
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SearchTreeTest extends TestCase {
    /**
     * This method sets up the tests that follow.
     */

    /**
     * Tests the initialization of SearchTree.
     */
    public void testMInit() {
        SearchTree mem = new SearchTree();
        assertNotNull(mem);
        SearchTree.main(null);
    }

    /**
     * Tests invalid arguments
     */
    public void testFirstError() { // tests the First Error message
        SearchTree mem = new SearchTree();
        assertNotNull(mem);
        String[] args = { "abcde" };
        SearchTree.main(args);
        String output = systemOut().getHistory();
        assertEquals("Invalid Arguments\n", output);
    }

    /**
     * Tests invalid file
     */
    public void testError() {
        SearchTree mem = new SearchTree();
        assertNotNull(mem);
        String[] args = { "1234", "4321", "aaaaa" };
        SearchTree.main(args);
        String output = systemOut().getHistory();
        assertEquals("File not found\n", output);
    }

    /**
     * Tests an unknown command
     */
    public void testWrongCommand() {
        String[] args = { "1234", "4321", "errorFile.txt" };
        SearchTree.main(args);
        String output = systemOut().getHistory();
        assertEquals(
                "Command not recognized\n" + "Unknown type in remove command\n"
                        + "Unknown type in print command\n",
                output);
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
        MemoryManager memManager = new MemoryManager(1024);
        Hashtable myHtb = new Hashtable(1024, "Artist", memManager);
        assertNotNull(myHtb);
        assertEquals(1024, myHtb.getSize());
        assertEquals(1024, myHtb.getTable().length);
    }

    /**
     * Tests adding an entry to the hashtable
     */
    public void testHashtableAdd() {
        MemoryManager memManager = new MemoryManager(1024);
        Hashtable myHtb = new Hashtable(1024, "Artist", memManager);
        myHtb.add("key", "Maroon5");
        assertEquals(1, myHtb.getItems());
        myHtb.add("keys", "Maroon6");
        myHtb.add("keys", "Maroon7");
        assertEquals(3, myHtb.getItems());
    }

    /**
     * /** Tests adding an entry to the hashtable with tombstone
     */
    public void testHashtableAddWithTombstone() {
        MemoryManager memManager = new MemoryManager(1024);
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
        MemoryManager memManager = new MemoryManager(1024);
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
        MemoryManager memManager = new MemoryManager(1024);
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
     * Tests extending the hashtable
     */
    public void testHashtableExtend() {
        MemoryManager memManager = new MemoryManager(32);
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
        one.getStart();
        two.getStart();
        three.getStart();
    }

    /**
     * Tests quadratic probing
     */
    public void testHashtableQuadraticFail() {
        MemoryManager memManager = new MemoryManager(1024);
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
        assertNull(myHtb.get("e", memManager.getPool()));
        assertNull(myHtb.remove("e", memManager.getPool()));
    }

    // MemoryManagerTest
    /**
     * Tests the memory manager insertion
     */
    public void testMemoryManagerArtistInsert() {
        World master = new World(2, 1, "file.txt");
        master.insert("Micheal Jackson", true);
        master.insert("Micheal Jackson", true);
        master.insert("Suck my kiss", false);
        master.insert("Suck my kiss", false);
        master.insert("Eagles", true);
        assertEquals(2, master.artists.getItems());
        assertEquals(1, master.songs.getItems());
    }

    /**
     * Tests inserting a song
     */
    public void testMemoryManagerSongsInsert() {
        World master = new World(2, 1, "file.txt");
        master.insert("Billy Jean", false);
        master.insert("Billy Jean", false);
        master.insert("Hotel California", false);
        assertEquals(2, master.songs.getItems());
    }

    /**
     * Tests the MemoryManager's artist remove function
     */
    public void testMemoryManagerArtistRemove() {
        World master = new World(2, 2, "file.txt");
        master.insert("Micheal Jackson", true);
        master.insert("Eagles", true);
        master.remove("Micheal Jackson", true);
        assertEquals(master.artists.getItems(), 1);
        master.remove("Micheal Jackson", true);
        master.remove("Eagles", true);
        assertEquals(master.artists.getItems(), 0);
    }

    /**
     * Tests the MemoryManager's song remove function
     */
    public void testMemoryManagerSongRemove() {
        World master = new World(2, 2, "file.txt");
        master.insert("Billy Jean", false);
        master.insert("Hotel California", false);

        master.remove("Billy Jean", false);
        assertEquals(master.songs.getItems(), 1);
        master.remove("Billy Jean", false);
        master.remove("Hotel California", false);
        assertEquals(master.songs.getItems(), 0);
    }

    /**
     * Tests the MemoryManager's song printing
     */
    public void testMemoryManagerSongPrint() {
        World master = new World(2, 100, "file.txt");
        master.insert("Michael Jackson", true);
        master.insert("Maroon5", true);
        master.insert("Billy Jean", false);
        master.insert("Hotel California", false);
        String output = "|Michael Jackson| is added to the artist database.\n"
                + "Artist hash table size doubled.\n|Maroon5|"
                + " is added to the artist database.\n|"
                + "Billy Jean| is added to the song database.\n"
                + "Song hash table size doubled.\n"
                + "|Hotel California| is added to the song database.\n";
        assertEquals(output, systemOut().getHistory());
    }

    /**
     * Tests output when all blocks are taken
     */
    public void testMemoryManagerPrintNoFreeBlocks() {
        MemoryManager mm = new MemoryManager(5);
        mm.insert("xyz", true);
        mm.print();
        String output = systemOut().getHistory();
        assertEquals("(5,0)\n", output);
    }

    /**
     * Tests the print function with blocks that have used blocks between them
     */
    public void testMemoryManagerFreeBlocksMergePrint() {
        MemoryManager mm = new MemoryManager(15);
        Handle one = mm.insert("xyz", true);
        mm.insert("abc", true);
        Handle three = mm.insert("qwe", true);
        mm.remove("xyz", true, one);
        mm.remove("qwe", true, three);
        mm.print();
        String output = systemOut().getHistory();
        assertTrue(output.contains("(0,5) -> (10,5)"));
    }

    /**
     * Tests that the free block list will merge with both a block to the right
     * and a block to the left.
     */
    public void testMemoryManagerMerge() {
        MemoryManager mm = new MemoryManager(15);
        Handle one = mm.insert("xyz", true);
        Handle two = mm.insert("abc", true);
        Handle three = mm.insert("qwe", true);
        mm.remove("xyz", true, one);
        mm.remove("qwe", true, three);
        mm.print();
        String output = systemOut().getHistory();
        assertTrue(output.contains("(0,5) -> (10,5)"));
        mm.remove("abc", true, two); // Merge here
        mm.print();
        output = systemOut().getHistory();
        assertTrue(output.contains("(0,15)\n"));
    }

    /**
     * Tests the parser
     */
    public void testParser() {
        ParserClass pc = new ParserClass("testFile.txt");
        pc.run();
        assertEquals(pc, pc);
    }

    // 2-3 Tree Tests

    /**
     * This unit test checks to see if swap works properly
     */
    public void testSwap() {
        MemoryManager memManager = new MemoryManager(500);
        Hashtable myHtb = new Hashtable(30, "Artist", memManager);
        Handle zero = (Handle) myHtb.add("Maroon0",
                new Handle("Maroon0".getBytes(), new byte[] { 0, 7 }, 0,
                        memManager.getPool()));
        Handle one = (Handle) myHtb.add("Maroon1",
                new Handle("Maroon1".getBytes(), new byte[] { 0, 7 }, 9,
                        memManager.getPool()));
        KVPair p1 = new KVPair(zero, one);
        assertEquals(0, p1.compareTo(zero)); // Checks if pair was created
                                             // properly
        KVPair p2 = new KVPair(one, zero);
        assertEquals(0, p2.compareTo(one)); // Checks if pair was created
                                            // properly
        LeafNode leaf = new LeafNode(p1, p2, null);
        InternalNode internal = new InternalNode(null, null, null);
        internal.setPair1(p1);
        internal.setPair2(p2);
        leaf.swap();
        internal.swap();
        assertEquals(p2.getKey(), leaf.getPair1().getKey());
        assertEquals(p2.getKey(), internal.getPair1().getKey());
    }

    /**
     * Unit method to test to see if it sets the internal nodes properly
     */
    public void testInternalSetters() {
        MemoryManager memManager = new MemoryManager(500);
        Hashtable myHtb = new Hashtable(30, "Artist", memManager);
        Handle zero = (Handle) myHtb.add("Maroon0",
                new Handle("Maroon0".getBytes(), new byte[] { 0, 7 }, 0,
                        memManager.getPool()));
        Handle one = (Handle) myHtb.add("Maroon1",
                new Handle("Maroon1".getBytes(), new byte[] { 0, 7 }, 9,
                        memManager.getPool()));
        KVPair p1 = new KVPair(zero, one);
        KVPair p2 = new KVPair(one, zero);
        InternalNode node1 = new InternalNode(null, null, null);
        InternalNode test = new InternalNode(null, null, null);
        test.setPair1(p1);
        test.setPair2(p2);
        node1.setLeft(test);
        assertEquals(test, node1.getLeft());
        node1.setLeft(null);
        assertNull(node1.getLeft());
        node1.setLeft(test);
        assertEquals(test, node1.getLeft());
    }

    /**
     * Unit test to check inserts with leaves
     */
    public void testLeafInsert() {
        MemoryManager memManager = new MemoryManager(500);
        Hashtable myHtb = new Hashtable(30, "Artist", memManager);
        Handle zero = (Handle) myHtb.add("Maroon0",
                new Handle("Maroon0".getBytes(), new byte[] { 0, 7 }, 0,
                        memManager.getPool()));
        Handle one = (Handle) myHtb.add("Maroon1",
                new Handle("Maroon1".getBytes(), new byte[] { 0, 7 }, 9,
                        memManager.getPool()));
        Handle two = (Handle) myHtb.add("Maroon2",
                new Handle("Maroon2".getBytes(), new byte[] { 0, 7 }, 18,
                        memManager.getPool()));
        Handle three = (Handle) myHtb.add("Maroon3",
                new Handle("Maroon3".getBytes(), new byte[] { 0, 7 }, 27,
                        memManager.getPool()));
        KVPair p1 = new KVPair(zero, one);
        KVPair p2 = new KVPair(one, zero);
        KVPair p3 = new KVPair(two, three);
        KVPair p4 = new KVPair(three, two);
        LeafNode leaf1 = new LeafNode(null, null, null);
        LeafNode leaf2 = new LeafNode(null, null, null);
        LeafNode leaf3 = new LeafNode(p3, p4, null);
        LeafNode leaf4 = new LeafNode(p3, p4, null);
        leaf1.insert(p2);
        leaf1.insert(p1);
        leaf2.insert(p1);
        leaf2.insert(p2);
        assertEquals(leaf1.getPair1(), p1); // Makes sure it swaps
        assertEquals(leaf2.getPair2(), p2); // Makes sure it inserts in second
                                            // pair position if the second pair
                                            // is greater and empty
        boolean correctReturn = false;
        if (leaf1.insert(p3) instanceof InternalNode) {
            correctReturn = true;
        }
        assertEquals(true, correctReturn);
        correctReturn = false;
        if (leaf2.insert(p1) instanceof InternalNode) {
            correctReturn = true;
        }
        assertEquals(true, correctReturn);
        correctReturn = false;
        if (leaf3.insert(p1) instanceof InternalNode) {
            correctReturn = true;
        }
        assertEquals(true, correctReturn);
        correctReturn = false;
        if (leaf4.insert(p3) instanceof InternalNode) {
            correctReturn = true;
        }
        assertEquals(true, correctReturn);
    }

    /**
     * Unit insertion test for one artists with multiple songs
     */
    public void testInsertOneArtistMultipleSongs() {
        String[] args = new String[3];
        args[0] = "10";
        args[1] = "32";
        args[2] = "SingleArtistMultipleSongs.txt";
        SearchTree.main(args);
        String content = null;
        File output = new File("SingleArtistMultipleSongsOut.txt");
        try {
            Scanner scan = new Scanner(output);
            content = scan.useDelimiter("\\Z").next();
            scan.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(content + "\n", systemOut().getHistory());
    }

    /**
     * This unit tests checks for inserts with ten songs
     */
    public void testInsertTenSongs() {
        String[] args = new String[3];
        args[0] = "10";
        args[1] = "32";
        args[2] = "InsertTenSongs.txt";
        SearchTree.main(args);
        String content = null;
        File output = new File("InsertTenSongsOut.txt");
        try {
            Scanner scan = new Scanner(output);
            content = scan.useDelimiter("\\Z").next();
            scan.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(content + "\n", systemOut().getHistory());
    }

    /**
     * This unit tests checks for proper prints with duplicates
     */
    public void testDuplicates() {
        String[] args = new String[3];
        args[0] = "10";
        args[1] = "32";
        args[2] = "DuplicateTest.txt";
        SearchTree.main(args);
        String content = null;
        File output = new File("DuplicateTestOut.txt");
        try {
            Scanner scan = new Scanner(output);
            content = scan.useDelimiter("\\Z").next();
            scan.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(content + "\n", systemOut().getHistory());
    }

    /**
     * Unit test to check if forty pairs of different songs and artists are
     * inserted properly into the 2-3 tree.
     */
    public void testFortyPairs() {
        String[] args = new String[3];
        args[0] = "10";
        args[1] = "32";
        args[2] = "FortyPairsTest.txt";
        SearchTree.main(args);
        String content = null;
        File output = new File("FortyPairsTestOut.txt");
        try {
            Scanner scan = new Scanner(output);
            content = scan.useDelimiter("\\Z").next();
            scan.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(content + "\n", systemOut().getHistory());
    }

    /**
     * This unit test checks to see if a certain node exists in the leaves of
     * the tree
     */
    public void testFindTreeNode() {
        MemoryManager memManager = new MemoryManager(300);
        Hashtable myHtb = new Hashtable(30, "Artist", memManager);
        Handle zero = (Handle) myHtb.add("Maroon0",
                new Handle("Maroon0".getBytes(), new byte[] { 0, 7 }, 0,
                        memManager.getPool()));
        Handle one = (Handle) myHtb.add("Maroon1",
                new Handle("Maroon1".getBytes(), new byte[] { 0, 7 }, 9,
                        memManager.getPool()));
        Handle two = (Handle) myHtb.add("Maroon2",
                new Handle("Maroon2".getBytes(), new byte[] { 0, 7 }, 18,
                        memManager.getPool()));
        Handle three = (Handle) myHtb.add("Maroon3",
                new Handle("Maroon3".getBytes(), new byte[] { 0, 7 }, 27,
                        memManager.getPool()));
        Handle four = (Handle) myHtb.add("Maroon4",
                new Handle("Maroon4".getBytes(), new byte[] { 0, 7 }, 36,
                        memManager.getPool()));
        Handle five = (Handle) myHtb.add("Maroon5",
                new Handle("Maroon5".getBytes(), new byte[] { 0, 7 }, 45,
                        memManager.getPool()));
        Handle six = (Handle) myHtb.add("Maroon5",
                new Handle("Maroon6".getBytes(), new byte[] { 0, 7 }, 54,
                        memManager.getPool()));
        Handle seven = (Handle) myHtb.add("Maroon5",
                new Handle("Maroon7".getBytes(), new byte[] { 0, 7 }, 63,
                        memManager.getPool()));
        Handle eight = (Handle) myHtb.add("Maroon5",
                new Handle("Maroon8".getBytes(), new byte[] { 0, 7 }, 72,
                        memManager.getPool()));
        Handle nine = (Handle) myHtb.add("Maroon5",
                new Handle("Maroon9".getBytes(), new byte[] { 0, 7 }, 81,
                        memManager.getPool()));
        Handle ten = (Handle) myHtb.add("Maroon5",
                new Handle("Maroon10".getBytes(), new byte[] { 0, 8 }, 91,
                        memManager.getPool()));
        Handle eleven = (Handle) myHtb.add("Maroon5",
                new Handle("Maroon11".getBytes(), new byte[] { 0, 8 }, 101,
                        memManager.getPool()));
        KVPair p1 = new KVPair(zero, one);
        KVPair p2 = new KVPair(two, three);
        KVPair p3 = new KVPair(four, five);
        KVPair p4 = new KVPair(six, seven);
        KVPair p5 = new KVPair(eight, nine);
        KVPair p6 = new KVPair(ten, eleven);
        LeafNode leaf1 = new LeafNode(p1, p2, null);
        LeafNode leaf2 = new LeafNode(p3, p4, null);
        LeafNode leaf3 = new LeafNode(p5, null, null);
        LeafNode leaf4 = new LeafNode(null, null, null);
        assertEquals(null, leaf3.search(p6));
        assertEquals(p2, leaf1.search(p2));
        assertEquals(p4, leaf2.search(p4));
        assertNull(leaf4.search(p5));
    }

    /**
     * Unit test to check if it lists songs and artists with single entries
     */
    public void testList() {
        String[] args = new String[3];
        args[0] = "10";
        args[1] = "32";
        args[2] = "ListTest.txt";
        SearchTree.main(args);
        String content = null;
        File output = new File("ListTestOut.txt");
        try {
            Scanner scan = new Scanner(output);
            content = scan.useDelimiter("\\Z").next();
            scan.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertFuzzyEquals(content + "\n", systemOut().getHistory());
    }

    /**
     * Unit test to check if it inserts and prints 100+ entries properly
     */
    public void testHugeInsert() {
        String[] args = new String[3];
        args[0] = "10";
        args[1] = "32";
        args[2] = "MultipleInserts.txt";
        SearchTree.main(args);
        String content = null;
        File output = new File("MultipleInsertsOut.txt");
        try {
            Scanner scan = new Scanner(output);
            content = scan.useDelimiter("\\Z").next();
            scan.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(content + "\n", systemOut().getHistory());
    }
    
    /**
     * tests tree delete without restructuring
     */
    public void testTreeSimpleDelete() {
        TTTree tree = new TTTree();
        byte[] pool = new byte[1024];
        byte[] mem = {0,2};
        
        tree.insert(new KVPair(new Handle(mem,mem,1,pool),new
        Handle(mem,mem,0,pool)));
        tree.insert(new KVPair(new Handle(mem,mem,3,pool),new
        Handle(mem,mem,0,pool)));
        tree.insert(new KVPair(new Handle(mem,mem,5,pool),new
        Handle(mem,mem,0,pool)));
        tree.insert(new KVPair(new Handle(mem,mem,2,pool),new
        Handle(mem,mem,0,pool)));
        tree.insert(new KVPair(new Handle(mem,mem,4,pool),new
        Handle(mem,mem,0,pool)));
        
        tree.delete(new KVPair(new Handle(mem,mem,1,pool),new
                Handle(mem,mem,0,pool)));
        tree.delete(new KVPair(new Handle(mem,mem,3,pool),new
                Handle(mem,mem,0,pool)));
        tree.delete(new KVPair(new Handle(mem,mem,5,pool),new
                Handle(mem,mem,0,pool)));
        tree.print();
        assertEquals(systemOut().getHistory(), "Printing 2-3 tree:\n4 0\n  2 0\n  4 0\n");
        systemOut().clearHistory();
        tree.insert(new KVPair(new Handle(mem,mem,6,pool),new
        Handle(mem,mem,0,pool)));
        tree.insert(new KVPair(new Handle(mem,mem,9,pool),new
        Handle(mem,mem,0,pool)));
        tree.delete(new KVPair(new Handle(mem,mem,2,pool),new
                Handle(mem,mem,0,pool)));
        tree.print();
        assertEquals(systemOut().getHistory(), "Printing 2-3 tree:\n6 0\n  4 0\n  6 0 9 0\n");
        tree.insert(new KVPair(new Handle(mem,mem,2,pool),new
        Handle(mem,mem,0,pool)));
        tree.insert(new KVPair(new Handle(mem,mem,1,pool),new
        Handle(mem,mem,0,pool)));
        tree.delete(new KVPair(new Handle(mem,mem,4,pool),new
                Handle(mem,mem,0,pool)));
        tree.print();
        tree.delete(new KVPair(new Handle(mem,mem,2,pool),new
                Handle(mem,mem,0,pool)));
        systemOut().clearHistory();
        tree.print();
        assertEquals(systemOut().getHistory(), "Printing 2-3 tree:\n6 0 9 0\n  1 0\n  6 0\n  9 0\n");
    }
}

// Tests to be implemented: