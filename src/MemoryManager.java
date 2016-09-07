/**
 * Implementation of the MemoryManager. Contains the MemoryBlock class
 * @author Adam Bishop
 * @author Jinwoo Yom
 * @version 1.0
 */
public class MemoryManager {
    private byte[] pool;    //Data pool holding the free and used bytes
    
    /**
     * Holds hashes of Artists with handles being their values
     */
    protected Hashtable artists;
    
    /**
     * Holds hashes of Song with handles being their values
     */
    protected Hashtable songs;
    private DoublyLinkedList<MemoryBlock> freeBlocks;   //LinkedList 
                              //holding handles to free MemoryBlocks
    private int blockSize;  //Initial pool size and the size of added 
                            //free blocks
    private final int twoByte = 2; //Constant holding the size of two bytes

    /**
     * Constructor
     * @param hashSize Initial hashtable size
     * @param newBlockSize Initial pool size and the size of added freeblocks
     */
    MemoryManager(int hashSize, int newBlockSize)
    {
        freeBlocks = new DoublyLinkedList<MemoryBlock>();
        artists = new Hashtable(hashSize, "Artist");
        songs = new Hashtable(hashSize, "Song");
        pool = new byte[newBlockSize];
        blockSize = newBlockSize;

        //This adds the first free memory block
        freeBlocks.append(new MemoryBlock(new byte[pool.length], null, 0));
    }

    /**
     * Inserts a record into memory
     * @param record The song/artist to add
     * @param artist Indicates if the record belongs in the artist table or not
     * @return true if the insertion was successful, false otherwise
     */
    public boolean insert(String record, boolean artist)
    {
        MemoryBlock handle; //Empty Handle
        if (artist) {   //If this is an artist, check the artist table
            if (artists.get(record) != null) {
                System.out.println("|" + record + "| duplicates a record "
                        + "already in the artist database.");
                return false;
            }
            //Make sure the amount of items doesn't
            //exceed half the size of the hashtable
            else if (artists.getItems() == (artists.getSize() / 2)) {
                artists.extend();
            }
        }
        else {    //If this is a song, check the song table
            if (songs.get(record) != null) {
                System.out.println("|" + record + "| duplicates a record "
                        + "already in the song database.");
                return false;
            }
            //Make sure the amount of items doesn't 
            //exceed half the size of the hashtable
            else if (songs.getItems() == (songs.getSize() / 2)) {
                songs.extend();
            }
        }

        //Begin checking for the best fit block
        do {
            handle = findBestFit(record);
            if (handle == null) {    // null means there was no fit at all
                expandPool();    //Add a block of size blockSize
            }
        } while (handle == null);

        //New handle with the best fit block's position and length
        MemoryBlock newBlock = new MemoryBlock(record.getBytes(), 
                toByte(record.length()), 
                handle.getStart());
        newBlock.applyBlock();    //Write to the pool

        if (artist) {
            artists.add(record, newBlock);
        }
        else {
            songs.add(record, newBlock);
        }

        // This checks to see if the new block is exactly 
        // the length of the free block it's displacing
        if ( newBlock.getStart() == handle.getStart() &&
                newBlock.getLength() == handle.getLength()) 
        {
            freeBlocks.remove();
        }
        else {
                //Else just resize the free block for use later
            handle.resize(handle.getStart() + newBlock.getLength(),
                handle.getLength() - newBlock.getLength());
        }
        
        if (artist) {
            System.out.println("|" + record + "|" + 
                               " is added to the artist database.");
        }
        else {
            System.out.println("|" + record + "|" +
                               " is added to the song database.");
        }

        return true;
    }

    /**
     * Converts an integer to byte[]
     * @param number number to be converted
     * @return number converted to byte[]
     */
    private byte[] toByte(int number)
    {
        byte[] out = new byte[] {(byte)(number >>> 8), (byte) number };
        return out;
    }

    /**
     * Removes a record into memory
     * @param record The song/artist to remove
     * @param artist Indicates if the record belongs in the artist table or not
     * @return true if the removal was successful, false otherwise
     */
    public boolean remove(String record, boolean artist)
    {
        MemoryBlock handle;    //Empty Handle
        if (artist) {
            if (artists.get(record) == null) {
                System.out.println("|" + record + 
                                   "| does not exist in the artist database.");
                return false;
            } 
            else {    //Else if the record exists, remove the handle
                handle = (MemoryBlock) artists.get(record);
                artists.remove(record);
                System.out.println("|" + record + 
                                   "| is removed from the artist database.");
            }
        }
        else {
            if (songs.get(record) == null) {
                System.out.println("|" + record +
                                   "| does not exist in the song database.");
                return false;
            }
            else {    //Else if the record exists, remove the handle
                handle = (MemoryBlock) songs.get(record);
                songs.remove(record);
                System.out.println("|" + record + 
                                   "| is removed from the song database.");
            }
        }

        //    Tell the LinkedList to jump to the 
        //  head to prepare for sequential searching
        freeBlocks.jumpToHead();

        //If the freeBlocks list is empty, just insert the handle
        if (freeBlocks.getSize() == 2) {
            freeBlocks.append(handle);
            return true;
        }
        else {
            //While there are nodes...
            while (freeBlocks.stepForward()) {
                //Get current node
                MemoryBlock currentBlock = freeBlocks.getCurrent()
                                                     .getNodeData();
                if (currentBlock.getStart() + 
                    currentBlock.getLength() 
                    == handle.getStart()) 
                {    //If these two nodes are adjacent
                    //Move one to the right of the current node
                    //and insert because it is further in the memory pool
                    freeBlocks.stepForward();
                    freeBlocks.add(handle);
                    //Check to see if there are merging opportunities
                    checkForMerge(freeBlocks.getCurrent());
                    return true;
                }
                //Else If the current node starts later
                //in the pool then the freed node
                else if (currentBlock.getStart() > handle.getStart()) {
                    freeBlocks.add(handle);
                    checkForMerge(freeBlocks.getCurrent());
                    return true;
                }
            }
            // Else just append the block
            freeBlocks.append(handle);
            return true;
        }
    }

    /**
     * Prints all artists, songs, or free blocks
     * @param artist indicates to print artists
     * @param song indicates to print songs
     * @param block indicates to print available blocks, 
     *        their starting points, and their length
     * @return true or false based on the success of the printing
     */
    public boolean print(boolean artist, boolean song, boolean block)
    {
        if (artist) {
            Hash[] table = artists.getTable();
            for (int i = 0; i < table.length; i++) {
                Hash item = table[i];
                if (item != null && item.getKey() != null) {
                    System.out.println("|" + item.getKey() + "| " + i);
                }
            }
            System.out.println("total artists: " + artists.getItems());
        }
        else if (song) {
            Hash[] table = songs.getTable();
            for (int i = 0; i < table.length; i++) {
                Hash item = table[i];
                if (item != null && item.getKey() != null) {
                    System.out.println("|" + item.getKey() + "| " + i);
                }
            }
            System.out.println("total songs: " + songs.getItems());
        }
        else {
            StringBuffer buf = new StringBuffer();
            freeBlocks.jumpToHead();
            while (freeBlocks.stepForward()) {
                Node<MemoryBlock> current = freeBlocks.getCurrent();
                buf.append("(" + 
                           current.getNodeData().getStart() + 
                           "," + 
                           current.getNodeData().getLength() + 
                           ")" );
                if (current.getAfter().getNodeData() != null) {
                    buf.append(" -> ");
                }
            }
            if (buf.length() == 0) {   //If no free blocks available..
                buf.append("(" + 
                        pool.length + 
                        "," + 
                        "0)" );
            }
            System.out.println(buf.toString());
        }
        return true;
    }

    /**
     * Expands the size of the memory pool by {blockSize}
     */
    private void expandPool()
    {
        byte[] tempPool = pool;   //Hold temporary value of old pool
        pool = new byte[pool.length + blockSize]; //Create new pool
        //Copy old pool into new
        System.arraycopy(tempPool, 0, pool, 0, tempPool.length);

        freeBlocks.jumpToTail();  //Prepare for appending
        freeBlocks.stepBack();

        System.out.println("Memory pool expanded to be " + 
                            pool.length + 
                            " bytes.");

        //If there are at least one free memory blocks
        if (freeBlocks.getSize() != 2) {   
            freeBlocks.append(
                    new MemoryBlock(
                    new byte[blockSize], 
                    null, 
                    freeBlocks.getCurrent().getNodeData().getStart() + 
                    freeBlocks.getCurrent().getNodeData().getLength())
            );
            //Check for merging opportunities
            checkForMerge(freeBlocks.getCurrent());
        } 
        else {  //Else there are no free memory blocks, just append it
            freeBlocks.append(new MemoryBlock(
                    new byte[blockSize], 
                    null, 
                    tempPool.length));
        }
    }

    /**
     * Find the MemoryBlock which is the best fit for the given record
     * @param record
     * @return MemoryBlock which is the best fit
     */
    private MemoryBlock findBestFit(String record)
    {
        // Set min to a large value in preparation to find best fit
        int min = Integer.MAX_VALUE;
        int difference;   //Difference is used in finding the smallest 
                          //amount of free space left after consumption
        int position = 0; //Position to save where the best fit was found
        int minPos = 0; //Position to save where the best fit was found

        //Prepare for sequential search
        freeBlocks.jumpToHead();
        while (freeBlocks.stepForward()) {
            position++;  //Log how many nodes traversed
            MemoryBlock currentBestFit = freeBlocks.getCurrent().getNodeData();
            difference = currentBestFit.getLength() - record.length();
            
            //If difference is less than minimum found and 
            //there's enough space for the length bytes
            if (difference < min && difference >= 2) {    
                min = difference;
                minPos = position;
            }
        }

        //This means that no available block was found
        if (min == Integer.MAX_VALUE) { 
            return null;
        }

        //Retrack to where the best fit was found and grab it
        freeBlocks.jumpToHead();
        for (int i = 0; i < minPos; i++) {
            freeBlocks.stepForward();
        }
        return freeBlocks.getCurrent().getNodeData();
    }

    /**
     * Checks adjacent memory blocks and merges if possible
     * @param blockPointer
     */
    private void checkForMerge(Node<MemoryBlock> blockPointer) {
        MemoryBlock blockBefore = blockPointer.getBefore().getNodeData();
        MemoryBlock blockAfter = blockPointer.getAfter().getNodeData();
        MemoryBlock blockCurrent = blockPointer.getNodeData();

        //Make sure the block before isn't the head
        //And the blocks are adjacent
        if (blockBefore != null &&
            blockBefore.getStart() + blockBefore.getLength()
                                  == blockCurrent.getStart())
        {
            blockBefore.resize(blockBefore.getStart(),
                                   blockBefore.getLength()
                                 + blockCurrent.getLength());
            freeBlocks.remove();
            freeBlocks.setCurrent(blockPointer.getBefore());
                //Prepare for checking the blockAfter
            blockCurrent = blockBefore; 
        }

        //Make sure the block before isn't the tail
        if (blockAfter != null &&
                blockCurrent.getStart() + blockCurrent.getLength() 
                                        == blockAfter.getStart()) 
        {
            blockAfter.resize(blockCurrent.getStart(),
                                  blockAfter.getLength()
                                + blockCurrent.getLength());
            freeBlocks.remove();
        }
    }

    /**
     * @return Memory pool
     */
    public byte[] getPool()
    {
        return pool;
    }

    /**
     * Implementation for the handle to positions in the memory pool
     * @author Adam Bishop and Jinwoo Yom
     */
    private class MemoryBlock {
        private byte[] memory;    //Memory of the block
        private int length;   //Length of the data
        private int start;    //Where in the pool the memory block starts at

        /**
         * Memory block constructor
         * @param newMemory Memory to place in the block
         * @param newMemoryLength Length of the memory to place in the block
         * @param newStart Location in the memory pool
         */
        MemoryBlock(byte[] newMemory, byte[] newMemoryLength, int newStart) {
            //If the length isn't null..use that value
            if (newMemoryLength != null) {
                memory = new byte[newMemory.length + twoByte];
                System.arraycopy(newMemoryLength, 0, memory, 0, 2);
                System.arraycopy(newMemory, 0, memory, 2, newMemory.length);
            }
            else {   //Else if it is null, it's probably a free block
                memory = new byte[newMemory.length];
                System.arraycopy(newMemory, 0, memory, 0, newMemory.length);
            }

            length = memory.length;
            start = newStart;
        }

        /**
         * Writes to the memory pool the contents of the block's memory
         * @return true or false depending on if the application was successful
         */
        public boolean applyBlock()
        {
            System.arraycopy(memory, 
                    0, 
                    MemoryManager.this.getPool(), 
                    start, 
                    length);
            return true;
        }

        /**
         * Changes the start and length of the memory block
         * @param newStart The new start value
         * @param newLength The new length value
         */
        public void resize(int newStart, int newLength)
        {
            length = newLength;
            start = newStart;
        }

        /**
         * @return start variable
         */
        public int getStart() {
            return start;
        }

        /**
         * @return length variable
         */
        public int getLength() {
            return length;
        }

    }
}
