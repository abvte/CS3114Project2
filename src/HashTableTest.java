import student.TestCase;

/**
 * @author Kevin Zhang
 * @author Adam Bishop
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class HashTableTest extends TestCase {
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
}