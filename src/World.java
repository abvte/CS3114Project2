
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
                        + "| is deleted from the artist database.");
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
                        "|" + record + "| is deleted from the song database.");
            }
        }
        return memManager.remove(record, artist, result);
    }

    /**
     * Removes a record from all entities
     * 
     * @param record
     *            The song/artist to remove
     * @param artist
     *            Indicates if the record belongs in the artist table or not
     */
    public void removeFromTree(String record, boolean artist) {
        if (artist) {
            this.artistRemoval(record, artist);
        }
        else {
            this.songRemoval(record, artist);
        }
    }

    /**
     * Removes an artist from all entities
     * 
     * @param record
     *            The song/artist to remove
     * @param artist
     *            Indicates if the record belongs in the artist table or not
     */
    public void artistRemoval(String record, boolean artist) {
        Handle result;
        String artistName;
        String songName;
        if (artists.get(record, memManager.getPool()) == null) {
            System.out.println(
                    "|" + record + "| does not exist in the artist database.");
            return;
        }
        else { // Else if the record exists, remove the handle
            result = (Handle) artists.get(record, memManager.getPool());
            KVPair artistRemove = searchTree.removeTree(result, true,
                    memManager);
            while (artistRemove.getKey() == null) {
                artistRemove = searchTree.removeTree(result, true, memManager);
            }
            while (artistRemove.getValue() == null) {
                artistName = memManager.handle2String(artistRemove.getKey(),
                        memManager.getPool());
                if (!(artistName.equals(record))) {
                    // This means that song needs to be deleted but artist
                    // still in tree
                    songName = artistName;
                    this.remove(songName, false);
                }
                else {
                    // This means that artist needs to be deleted but song
                    // is still in the table
                    this.remove(artistName, true);
                }
                artistRemove = searchTree.removeTree(result, true, memManager);
                if (artistRemove == null) {
                    return;
                }
            }
            if (artistRemove.getValue() != null) {
                // This means that both song and artist needs to be deleted
                artistName = memManager.handle2String(artistRemove.getKey(),
                        memManager.getPool());
                songName = memManager.handle2String(artistRemove.getValue(),
                        memManager.getPool());
                this.remove(artistName, true);
                this.remove(songName, false);
                return;
            }
        }

    }

    /**
     * Removes a song from all entities
     * 
     * @param record
     *            The song/artist to remove
     * @param artist
     *            Indicates if the record belongs in the artist table or not
     */
    public void songRemoval(String record, boolean artist) {
        Handle result;
        String artistName;
        String songName;
        if (songs.get(record, memManager.getPool()) == null) {
            System.out.println(
                    "|" + record + "| does not exist in the song database.");
            return;
        }
        else { // Else if the record exists, remove the handle
            result = (Handle) songs.get(record, memManager.getPool());
            KVPair songRemove = searchTree.removeTree(result, false,
                    memManager);
            while (songRemove.getKey() == null) {
                songRemove = searchTree.removeTree(result, false, memManager);
            }
            while (songRemove.getValue() == null) {
                songName = memManager.handle2String(songRemove.getKey(),
                        memManager.getPool());
                if (!(songName.equals(record))) {
                    // This means that artist needs to be deleted but song
                    // still in tree
                    artistName = songName;
                    this.remove(artistName, true);
                }
                else {
                    // This means that song needs to be deleted but artist
                    // is still in the table
                    this.remove(songName, false);
                }
                songRemove = searchTree.removeTree(result, false, memManager);
                if (songRemove == null) {
                    return;
                }
            }
            if (songRemove.getValue() != null) {
                // This means that both song and artist needs to be deleted
                artistName = memManager.handle2String(songRemove.getValue(),
                        memManager.getPool());
                songName = memManager.handle2String(songRemove.getKey(),
                        memManager.getPool());
                this.remove(songName, false);
                this.remove(artistName, true);
                return;
            }
        }
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
        searchTree.processHandles(first, second, song, artist, true);
    }

    /**
     * @param artist
     *            Artist to be deleted
     * @param song
     *            Song to be deleted
     */
    public void deleteTree(String artist, String song) {
        Handle first = artists.get(artist, memManager.getPool()); // Artist
        Handle second = songs.get(song, memManager.getPool()); // Song
        int hashDelete = 0;
        if (first == null) {
            System.out.println(
                    "|" + artist + "| does not exist in the artist database.");
            return;
        }
        else if (second == null) {
            System.out.println(
                    "|" + song + "| does not exist in the song database.");
            return;
        }
        hashDelete = searchTree.processHandles(first, second, song, artist,
                false);
        if (hashDelete == 3) { // Remove from both hash tables
            this.remove(artist, true);
            this.remove(song, false);
        }
        else if (hashDelete == 2) { // Remove from song hash
            this.remove(song, false);
        }
        else if (hashDelete == 1) { // Remove from artist hash
            this.remove(artist, true);
        }
        else {
            return; // Does not need to be removed
        }

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
