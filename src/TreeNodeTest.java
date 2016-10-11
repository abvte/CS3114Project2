import student.TestCase;

/**
 * Unit tests to test functionality of the node classes.
 * 
 * @author Kevin Zhang
 * @author Adam Bishop
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TreeNodeTest extends TestCase {
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
     * This unit test checks the operation of the internal node when it tries to
     * call handle search when it's null
     */
    public void testEmptyInternalHandleSearch() {
        InternalNode intern1 = new InternalNode(null, null, null);
        assertNull(intern1.handleSearch(null));
        assertNull(intern1.search(null));
    }

    /**
     * This unit test checks the split helper operations within the internal
     * nodes for center nodes
     */
    public void testCenterSplitHelper() {
        MemoryManager memManager = new MemoryManager(300);
        Hashtable myHtb = new Hashtable(30, "Artist", memManager);
        Handle zero = (Handle) myHtb.add("Maroon0",
                new Handle("Maroon0".getBytes(), new byte[] { 0, 7 }, 0,
                        memManager.getPool()));
        Handle one = (Handle) myHtb.add("Maroon1",
                new Handle("Maroon1".getBytes(), new byte[] { 0, 7 }, 9,
                        memManager.getPool()));
        KVPair pair1 = new KVPair(zero, one);
        LeafNode leaf1 = new LeafNode(pair1, pair1, null);
        InternalNode intern1 = new InternalNode(leaf1, leaf1, leaf1);
        intern1.splitHelper(intern1, true);
        assertEquals(leaf1.getNext(), intern1.getLeft());
    }

    /**
     * This unit test checks to see if it properly deleted the right branch of
     * an internal node
     */
    public void testInternalRightDelete() {
        MemoryManager memManager = new MemoryManager(300);
        Hashtable myHtb = new Hashtable(30, "Artist", memManager);
        Handle zero = (Handle) myHtb.add("Maroon0",
                new Handle("Maroon0".getBytes(), new byte[] { 0, 7 }, 0,
                        memManager.getPool()));
        Handle one = (Handle) myHtb.add("Maroon1",
                new Handle("Maroon1".getBytes(), new byte[] { 0, 7 }, 9,
                        memManager.getPool()));
        KVPair pair1 = new KVPair(zero, one);
        KVPair pair2 = new KVPair(one, zero);
        LeafNode leaf1 = new LeafNode(pair1, pair1, null);
        LeafNode leaf2 = new LeafNode(pair2, null, null);
        InternalNode intern1 = new InternalNode(leaf1, leaf1, leaf2);
        intern1.setPair1(pair1);
        intern1.setPair1(pair1);
        intern1.delete(pair2);
        assertNull(intern1.getCenter().getPair2());
    }

    /**
     * tests tree delete without restructuring
     */
    public void testTreeSimpleDelete() {
        TTTree tree = new TTTree();
        byte[] pool = new byte[1024];
        byte[] mem = { 0, 2 };

        tree.insert(new KVPair(new Handle(mem, mem, 1, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 3, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 5, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 2, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 4, pool),
                new Handle(mem, mem, 0, pool)));

        tree.delete(new KVPair(new Handle(mem, mem, 1, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 3, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 5, pool),
                new Handle(mem, mem, 0, pool)));
        tree.print();
        assertEquals(systemOut().getHistory(),
                "Printing 2-3 tree:\n4 0\n  2 0\n  4 0\n");
        systemOut().clearHistory();
        tree.insert(new KVPair(new Handle(mem, mem, 6, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 9, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 2, pool),
                new Handle(mem, mem, 0, pool)));
        tree.print();
        assertEquals(systemOut().getHistory(),
                "Printing 2-3 tree:\n6 0\n  4 0\n  6 0 9 0\n");
        tree.insert(new KVPair(new Handle(mem, mem, 2, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 1, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 4, pool),
                new Handle(mem, mem, 0, pool)));
        tree.print();
        tree.delete(new KVPair(new Handle(mem, mem, 2, pool),
                new Handle(mem, mem, 0, pool)));
        systemOut().clearHistory();
        tree.print();
        assertEquals(systemOut().getHistory(),
                "Printing 2-3 tree:\n6 0 9 0\n  1 0\n  6 0\n  9 0\n");
    }

    /**
     * Unit test to test delete with restructuring
     */
    public void testTreeDeleteRestructure() {
        TTTree tree = new TTTree();
        byte[] pool = new byte[1024];
        byte[] mem = { 0, 2 };

        tree.insert(new KVPair(new Handle(mem, mem, 1, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 3, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 5, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 7, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 9, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 11, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 13, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 15, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 16, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 17, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 19, pool),
                new Handle(mem, mem, 0, pool)));

        tree.delete(new KVPair(new Handle(mem, mem, 1, pool),
                new Handle(mem, mem, 0, pool)));
        tree.print();
        String output = "Printing 2-3 tree:\n13 0\n  9 0\n    5 0 7 0\n"
                + "      3 0\n      5 0\n      7 0\n    11 0\n      9 0"
                + "\n      11 0\n  16 0\n    15 0\n      13 0\n      15 0"
                + "\n    17 0\n      16 0\n      17 0 19 0\n";
        assertEquals(systemOut().getHistory(), output);
    }

    /**
     * Unit test that checks deletion of center node
     */
    public void testTreeCenterDeleteRestructure() {
        TTTree tree = new TTTree();
        byte[] pool = new byte[1024];
        byte[] mem = { 0, 2 };

        tree.insert(new KVPair(new Handle(mem, mem, 1, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 3, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 5, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 7, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 9, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 11, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 13, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 15, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 16, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 17, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 19, pool),
                new Handle(mem, mem, 0, pool)));

        tree.delete(new KVPair(new Handle(mem, mem, 5, pool),
                new Handle(mem, mem, 0, pool)));
        tree.print();
        String output = "Printing 2-3 tree:\n13 0\n  9 0\n    3 0 7 0\n"
                + "      1 0\n      3 0\n      7 0\n    11 0\n      9 0"
                + "\n      11 0\n  16 0\n    15 0\n      13 0\n      15 0"
                + "\n    17 0\n      16 0\n      17 0 19 0\n";
        assertEquals(systemOut().getHistory(), output);
    }

    /**
     * Unit tests that checks deletion of right node
     */
    public void testTreeRightDeleteRestructure() {
        TTTree tree = new TTTree();
        byte[] pool = new byte[1024];
        byte[] mem = { 0, 2 };

        tree.insert(new KVPair(new Handle(mem, mem, 1, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 3, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 5, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 7, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 9, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 11, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 13, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 15, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 16, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 17, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 19, pool),
                new Handle(mem, mem, 0, pool)));

        tree.delete(new KVPair(new Handle(mem, mem, 16, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 19, pool),
                new Handle(mem, mem, 0, pool)));
        tree.print();
        String output = "Printing 2-3 tree:\n9 0\n  5 0\n    3 0\n"
                + "      1 0\n      3 0\n    7 0\n      5 0"
                + "\n      7 0\n  13 0\n    11 0\n      9 0\n      11 0"
                + "\n    15 0 17 0\n      13 0\n      15 0\n      17 0\n";
        assertEquals(systemOut().getHistory(), output);
    }

    /**
     * Unit test that checks to see if it deletes the entire tree properly
     */
    public void testTreeFullRemoval() {
        TTTree tree = new TTTree();
        byte[] pool = new byte[1024];
        byte[] mem = { 0, 2 };

        tree.insert(new KVPair(new Handle(mem, mem, 1, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 3, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 5, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 7, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 9, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 11, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 13, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 15, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 16, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 17, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 19, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 21, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 23, pool),
                new Handle(mem, mem, 0, pool)));

        tree.delete(new KVPair(new Handle(mem, mem, 16, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 7, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 15, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 3, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 11, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 1, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 19, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 21, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 23, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 5, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 9, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 17, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 13, pool),
                new Handle(mem, mem, 0, pool)));
        tree.print();
        String output = "Printing 2-3 tree:\n";
        assertEquals(systemOut().getHistory(), output);
    }

    /**
     * 
     */
    public void testTreeCrazyRemoval() {
        TTTree tree = new TTTree();
        byte[] pool = new byte[1024];
        byte[] mem = { 0, 2 };

        tree.insert(new KVPair(new Handle(mem, mem, 1, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 3, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 5, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 7, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 9, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 11, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 13, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 15, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 17, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 19, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 21, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 23, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 25, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 27, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 29, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 31, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 33, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 35, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 37, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 39, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 41, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 43, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 45, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 47, pool),
                new Handle(mem, mem, 0, pool)));
        tree.insert(new KVPair(new Handle(mem, mem, 49, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 1, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 9, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 13, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 15, pool),
                new Handle(mem, mem, 0, pool)));
        tree.delete(new KVPair(new Handle(mem, mem, 17, pool),
                new Handle(mem, mem, 0, pool)));
        tree.print();
        String output = "Printing 2-3 tree:\n33 0\n  25 0\n    7 0 19 0\n      "
                + "5 0\n        3 0\n        5 0\n      11 0\n        7 0\n    "
                + "    11 0\n      21 0 23 0\n        19 0\n        21 0\n     "
                + "   23 0\n    29 0\n      27 0\n        25 0\n        27 0\n "
                + "     31 0\n        29 0\n        31 0\n  41 0\n    37 0\n   "
                + "   35 0\n        33 0\n        35 0\n      39 0\n        "
                + "37 0\n        39 0\n    45 0\n      43 0\n        41 0\n    "
                + "    43 0\n      47 0\n        45 0\n        47 0 49 0\n";
        assertEquals(systemOut().getHistory(), output);
    }
}