import student.TestCase;

/**
 * @author Kevin Zhang 
 * @author Adam Bishop
 * @version 1.0
 */
public class MemoryManagerTest extends TestCase {
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
}