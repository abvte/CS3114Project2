import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import student.TestCase;

/**
 * @author Kevin Zhang
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
        LeafNode leaf1 = new LeafNode(p1, p2, null);
        InternalNode node2 = new InternalNode(leaf1, leaf1, leaf1);
        node2.setCenter(null);
        assertNull(node2.getCenter());
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
     * Unit test to check if it lists and removes properly
     */
    public void testListAndRemove() {
        String[] args = new String[3];
        args[0] = "10";
        args[1] = "32";
        args[2] = "ListAndRemoveTest.txt";
        SearchTree.main(args);
        String content = null;
        File output = new File("ListAndRemoveTestOut.txt");
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
     * Unit test to check if it lists and removes properly
     */
    public void testSampleInput() {
        String[] args = new String[3];
        args[0] = "10";
        args[1] = "32";
        args[2] = "P2_Input1_Sample.txt";
        SearchTree.main(args);
        String content = null;
        File output = new File("P2_Output1_Sample.txt");
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
     * Unit test to check if it removes all songs properly
     */
    public void testSongRemoval() {
        String[] args = new String[3];
        args[0] = "10";
        args[1] = "32";
        args[2] = "TestSongRemoval.txt";
        SearchTree.main(args);
        String content = null;
        File output = new File("TestSongRemovalOut.txt");
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

}
