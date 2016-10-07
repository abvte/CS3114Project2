
/**
 * "World" class which allows class objects to communicate
 * 
 * @author Adam Bishop & Kevin Zhang
 * @version 1.0
 */
public class World {
    /**
     * 
     */
    protected TTTree searchTree;
    /**
     * 
     */
    protected MemoryManager memManager;
    /**
     * 
     */
    protected Hashtable<Handle> artists;
    /**
     * 
     */
    protected Hashtable<Handle> songs;
    /**
     * 
     */
    protected ParserClass parser;

    /**
     * Constructor
     * 
     * @param hashSize
     *            Initial hash size
     * @param blockSize
     *            Size of blocks
     * @param filename
     *            Path to input file
     */
    World(int hashSize, int blockSize, String filename) {
        this.memManager = new MemoryManager(blockSize);
        this.artists = new Hashtable<Handle>(hashSize, "Artist", memManager);
        this.songs = new Hashtable<Handle>(hashSize, "Song", memManager);
        this.searchTree = new TTTree();
        this.parser = new ParserClass(filename);
    }

    /**
     * Runs the parser to activate world's functions
     */
    public void run() {
        parser.run();
    }

    /**
     * Inserts a record into all entities
     * 
     * @param record
     *            The song/artist to add
     * @param artist
     *            Indicates if the record belongs in the artist table or not
     * @return handle to memory block
     */
    public Handle insert(String record, boolean artist) {
        if (artist) { // If this is an artist, check the artist table
            if (artists.get(record, memManager.getPool()) != null) {
                System.out.println("|" + record + "| duplicates a record "
                        + "already in the artist database.");
                return null;
            }
            // Make sure the amount of items doesn't
            // exceed half the size of the hashtable
            else if (artists.getItems() == (artists.getSize() / 2)) {
                artists.extend(memManager.getPool());
            }
        }
        else { // If this is a song, check the song table
            if (songs.get(record, memManager.getPool()) != null) {
                System.out.println("|" + record + "| duplicates a record "
                        + "already in the song database.");
                return null;
            }
            // Make sure the amount of items doesn't
            // exceed half the size of the hashtable
            else if (songs.getItems() == (songs.getSize() / 2)) {
                songs.extend(memManager.getPool());
            }
        }

        Handle result = memManager.insert(record, artist);
        if (artist) {
            artists.add(record, result);
            System.out.println(
                    "|" + record + "|" + " is added to the artist database.");
        }
        else {
            songs.add(record, result);
            System.out.println(
                    "|" + record + "|" + " is added to the song database.");
        }
        return result;
    }

    /**
     * Removes a record from all entities
     * 
     * @param record
     *            The song/artist to remove
     * @param artist
     *            Indicates if the record belongs in the artist table or not
     * @return handle to the removed memory block
     */
    public Handle remove(String record, boolean artist) {
        Handle result;
        if (artist) {
            if (artists.get(record, memManager.getPool()) == null) {
                System.out.println("|" + record
                        + "| does not exist in the artist database.");
                return null;
            }
            else { // Else if the record exists, remove the handle
                result = (Handle) artists.get(record, memManager.getPool());
                artists.remove(record, memManager.getPool());
                System.out.println("|" + record
                        + "| is removed from the artist database.");
            }
        }
        else {
            if (songs.get(record, memManager.getPool()) == null) {
                System.out.println("|" + record
                        + "| does not exist in the song database.");
                return null;
            }
            else { // Else if the record exists, remove the handle
                result = (Handle) songs.get(record, memManager.getPool());
                songs.remove(record, memManager.getPool());
                System.out.println(
                        "|" + record + "| is removed from the song database.");
            }
        }
        return memManager.remove(record, artist, result);
    }

    /**
     * Prints all artists, songs, or free blocks
     * 
     * @param artist
     *            indicates to print artists
     * @param song
     *            indicates to print songs
     * @param block
     *            indicates to print available blocks, their starting points,
     *            and their length
     * @return true or false based on the success of the printing
     */
    public boolean print(boolean artist, boolean song, boolean block) {
        if (artist) {
            Hash<Handle>[] table = artists.getTable();
            for (int i = 0; i < table.length; i++) {
                Hash<Handle> item = table[i];
                if (item != null && item.getValue() != null) {
                    Handle memBlock = item.getValue();
                    byte[] memory = new byte[memBlock.getLength()];
                    System.arraycopy(memManager.getPool(), memBlock.getStart(),
                            memory, 0, memory.length);
                    String artistStr = memManager.decodeMemory(memory);
                    System.out.println("|" + artistStr + "| " + i);
                }
            }
            System.out.println("total artists: " + artists.getItems());
        }
        else if (song) {
            Hash<Handle>[] table = songs.getTable();
            for (int i = 0; i < table.length; i++) {
                Hash<Handle> item = table[i];
                if (item != null && item.getValue() != null) {
                    Handle memBlock = item.getValue();
                    byte[] memory = new byte[memBlock.getLength()];
                    System.arraycopy(memManager.getPool(), memBlock.getStart(),
                            memory, 0, memory.length);
                    String songStr = memManager.decodeMemory(memory);
                    System.out.println("|" + songStr + "| " + i);
                }
            }
            System.out.println("total songs: " + songs.getItems());
        }
        else {
            memManager.print();
        }
        return true;
    }

    /**
     * Inserts into tree
     * 
     * @param artist
     *            name of artist
     * @param song
     *            name of song
     */
    public void insertToTree(String artist, String song) {
        Handle first = artists.get(artist, memManager.getPool()); // Artist
        Handle second = songs.get(song, memManager.getPool()); // Song

        if (first != null && second == null) {
            first = SearchTree.world.artists.get(artist,
                    SearchTree.world.memManager.getPool());
        }
        else if (first == null && second != null) {
            second = SearchTree.world.songs.get(song,
                    SearchTree.world.memManager.getPool());
        }
        else {
            first = SearchTree.world.artists.get(artist,
                    SearchTree.world.memManager.getPool());
            second = SearchTree.world.songs.get(song,
                    SearchTree.world.memManager.getPool());
        }
        searchTree.processHandles(first, second, song, artist);
    }
    
    public void deleteTree(String artist, String song) {
        Handle first = artists.get(artist, memManager.getPool()); // Artist
        Handle second = songs.get(song, memManager.getPool()); // Song
        
        if (first == null) {
            System.out.println("|" + artist
                    + "| does not exist in the artist database.");
            return;
        }
        else if (second == null) {
            System.out.println("|" + song
                    + "| does not exist in the song database.");
            return;
        }
        
        KVPair artistSong = new KVPair(first, second);
        KVPair songArtist = new KVPair(second, first);
        searchTree.delete(artistSong);
        searchTree.delete(songArtist);
    }

    /**
     * Lists leaf nodes of the tree
     * 
     * @param item
     *            Item to get from hashtable
     * @param pool
     *            Pool to extract data from
     * @param artist
     *            indicates if item is an artist or not
     */
    public void listTree(String item, MemoryManager pool, boolean artist) {
        Handle location;
        String itemType;

        if (artist) {
            location = artists.get(item, pool.getPool());
            itemType = "artist";
        }
        else {
            location = songs.get(item, pool.getPool());
            itemType = "song";
        }

        if (location == null) {
            System.out.println("|" + item + "| " + "does not exist in the "
                    + itemType + " database.");
            return;
        }
        else {
            searchTree.list(location, memManager);
        }
    }
}
