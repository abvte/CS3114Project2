import java.nio.ByteBuffer;

/**
 * Implementation of the MemoryManager. Contains the MemoryBlock class
 * @author Adam Bishop and Jinwoo Yom
 */
public class MemoryManager {
	byte[] pool;	//Data pool holding the free and used bytes
	Hashtable artists,songs;	//Hashtables holding the MemoryBlock handles to song/artist info
	DoublyLinkedList<MemoryBlock> freeBlocks;	//LinkedList holding handles to free MemoryBlocks
	int blockSize;	//Initial pool size and the size of added free blocks
	ByteBuffer buff;
	final int TWO_BYTE = 2; //Constant holding the size of two bytes
	
	/**
	 * Constructor
	 * @param hashSize Initial hashtable size
	 * @param newBlockSize Initial pool size and the size of added freeblocks
	 */
	MemoryManager(int hashSize, int newBlockSize)
	{
		freeBlocks = new DoublyLinkedList<MemoryBlock>();
		artists = new Hashtable(hashSize,"Artist");
		songs = new Hashtable(hashSize,"Song");
		pool = new byte[newBlockSize];
		blockSize = newBlockSize;
		buff = ByteBuffer.allocate(2);
		
		//This adds the first free memory block
		freeBlocks.append(new MemoryBlock(new byte[pool.length],null,0));
	}
	
	/**
	 * Inserts a record into memory
	 * @param record The song/artist to add
	 * @param artist Indicates if the record belongs in the artist table or not
	 * @return true if the insertion was successful, false otherwise
	 */
	public boolean insert(String record, boolean artist)
	{
		MemoryBlock handle;	//Empty Handle
		if(artist) {	//If this is an artist, check the artist table
			if(artists.get(record) != null) {
				System.out.println("|" + record + "| duplicates a record "
									   + "already in the artist database.");
				return false;
			}
			//Make sure the amount of items doesn't exceed half the size of the hashtable
			else if(artists.getItems() == (artists.getSize()/2))
				artists.extend();
		}
		else {	//If this is a song, check the song table
			if(songs.get(record) != null) {
				System.out.println("|" + record + "| duplicates a record "
									   + "already in the song database.");
				return false;
			}
			//Make sure the amount of items doesn't exceed half the size of the hashtable
			else if(songs.getItems() == (songs.getSize()/2))
				songs.extend();
		}
		
		//Begin checking for the best fit block
		do {
			handle = findBestFit(record);
			if(handle == null) {	//a handle pointing to null means there was no fit at all
				expandPool();	//Add a block of size blockSize
			}
		} while(handle == null);
		
		//New handle with the best fit block's position and length
		MemoryBlock newBlock = new MemoryBlock(record.getBytes(),
											   toByte(record.length()),
											   handle.getStart());
		newBlock.applyBlock();	//Write to the pool
		
		if(artist)
			artists.add(record, newBlock);
		else
			songs.add(record, newBlock);
		
		// This checks to see if the new block is exactly the length of the free block it's displacing
		if( newBlock.getStart() == handle.getStart() &&
		    newBlock.getLength() == handle.getLength()) 
		{
			freeBlocks.remove();
		}
		else {
			//Else just resize the free block for use later
			handle.resize(handle.getStart() + newBlock.getLength()
				 	 	 ,handle.getLength() - newBlock.getLength());
		}
		if(artist)
			System.out.println("|" + record + "|" + " is added to the artist datbase.");
		else
			System.out.println("|" + record + "|" + " is added to the song database.");

		return true;
	}
	
	/**
	 * Converts an integer to byte[]
	 * @param number
	 * @return number converted to byte[]
	 */
	private byte[] toByte(int number)
	{
		byte[] out = new byte[] {
	            (byte)(number >>> 8),
	            (byte) number};
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
		MemoryBlock handle;	//Empty Handle
		if(artist) {
			if(artists.get(record) == null) {
				System.out.println("|" + record + "| does not exist in the artist database.");
				return false;
			} 
			else {	//Else if the record exists, remove the handle
				handle = (MemoryBlock) artists.get(record);
				artists.remove(record);
				System.out.println("|" + record + "| is removed from the artist database.");
			}
		}
		else {
			if(songs.get(record) == null) {
				System.out.println("|" + record + "| does not exist in the song database.");
				return false;
			}
			else {	//Else if the record exists, remove the handle
				handle = (MemoryBlock) songs.get(record);
				songs.remove(record);
				System.out.println("|" + record + "| is removed from the song database.");
			}
		}
		
		//	Tell the LinkedList to jump to the head to prepare for sequential searching
		freeBlocks.jumpToHead();
		
		//If the freeBlocks list is empty, just insert the handle
		if(freeBlocks.getSize() == 2) {
			freeBlocks.append(handle);
			return true;
		}
		else {
			//While there are nodes...
			while(freeBlocks.stepForward()) {
				MemoryBlock currentBlock = freeBlocks.getCurrent().getNodeData();	//Get current node
				if(currentBlock.getStart() + currentBlock.getLength() == handle.getStart()) {	//If these two nodes are adjacent
					freeBlocks.stepForward();	//Move one to the right of the current node and insert because it is further in the memory pool
					freeBlocks.add(handle);
					checkForMerge(freeBlocks.getCurrent()); //Check to see if there are merging opportunities
					return true;
				}
				else if(currentBlock.getStart() > handle.getStart()) { //If the current node starts later in the pool then the freed node
					freeBlocks.add(handle);
					checkForMerge(freeBlocks.getCurrent());
					return true;
				}
			}
			// Else step back and 
			freeBlocks.stepBack();
			MemoryBlock currentBlock = freeBlocks.getCurrent().getNodeData();
			if(handle.getStart() > currentBlock.getStart()) {
				freeBlocks.append(handle);
				return true;
			}
		}
		return false;
	}
	
	public void print (boolean artist, boolean song, boolean block)
	{
		if(artist) {
			Hash[] table = artists.getTable();
			for(int i = 0; i < table.length; i++) {
				Hash item = table[i];
				if(item != null) {
					System.out.println("|" + item.getKey() + "| " + i);
				}
			}
			System.out.println("total artists: " + artists.getItems());
		}
		else if(song) {
			Hash[] table = songs.getTable();
			for(int i = 0; i < table.length; i++) {
				Hash item = table[i];
				if(item != null) {
					System.out.println("|" + item.getKey() + "| " + i);
				}
			}
			System.out.println("total songs: " + songs.getItems());
		}
		else {
			StringBuffer buf = new StringBuffer();
			freeBlocks.jumpToHead();
			while(freeBlocks.stepForward()) {
				Node<MemoryBlock> current = freeBlocks.getCurrent();
				buf.append("(" + current.getNodeData().getStart() + "," + current.getNodeData().getLength() + ")" );
				if(current.getAfter().getNodeData() != null) {
					buf.append(" -> ");
				}
			}
			if(buf.length() > 0) {
				System.out.println(buf.toString());
			}
		}
	}
	
	private void expandPool()
	{
		byte[] tempPool = pool;
		pool = new byte[pool.length + blockSize];
		System.arraycopy(tempPool, 0, pool, 0, tempPool.length);
		
		freeBlocks.jumpToTail();
		freeBlocks.stepBack();
		
		freeBlocks.append(new MemoryBlock(new byte[blockSize],null,freeBlocks.getCurrent().getNodeData().getStart() + 
															       freeBlocks.getCurrent().getNodeData().getLength()));
		System.out.println("Memory pool expanded to be " + pool.length + " bytes.");

		checkForMerge(freeBlocks.getCurrent());
	}
	
	private MemoryBlock findBestFit(String record)
	{
		// Set min to a large value in preparation to find best fit
		int min = Integer.MAX_VALUE;
		int difference;
		int position = 0;
		int minPos = 0;
		
		if(freeBlocks.jumpToHead()) {
			while(freeBlocks.stepForward()) {
				position++;
				MemoryBlock currentBestFit = freeBlocks.getCurrent().getNodeData();
				difference = currentBestFit.getLength() - record.length();
				if(difference < min && difference >= 2) {
					min = difference;
					minPos = position;
				}
			}
			
			//This means that no available block was found
			if(min == Integer.MAX_VALUE) return null;
			
			freeBlocks.jumpToHead();
			for(int i = 0; i < minPos; i++) {
				freeBlocks.stepForward();
			}
			return freeBlocks.getCurrent().getNodeData();
		}
		else {
			return null;
		}
	}
	
	private void checkForMerge(Node<MemoryBlock> blockPointer) {
		MemoryBlock blockBefore = blockPointer.getBefore().getNodeData();
		MemoryBlock blockAfter = blockPointer.getAfter().getNodeData();
		MemoryBlock blockCurrent = blockPointer.getNodeData();

		if (blockBefore != null) {
			if(blockBefore.getStart() + blockBefore.getLength() == blockCurrent.getStart()) {
				blockBefore.resize(blockBefore.getStart(), blockBefore.getLength() + blockCurrent.getLength());
				freeBlocks.remove();
				freeBlocks.setCurrent(blockPointer.getBefore());
				blockCurrent = blockBefore;
			}
		}

		if (blockAfter != null) {
			if(blockCurrent.getStart() + blockCurrent.getLength() == blockAfter.getStart()) {
				blockAfter.resize(blockCurrent.getStart(), blockAfter.getLength() + blockCurrent.getLength());
				freeBlocks.remove();
			}
		}
	}
	
	public byte[] getPool()
	{
		return pool;
	}
	
	public class MemoryBlock {
		private byte[] memory;
		private int length;
		private int start;
		

		MemoryBlock(byte[] newMemory,byte[] newMemoryLength, int newStart) {
			if(newMemoryLength != null) {
				memory = new byte[newMemory.length + TWO_BYTE];
				System.arraycopy(newMemoryLength, 0, memory, 0, 2);
				System.arraycopy(newMemory, 0, memory, 2, newMemory.length);
			}
			else {
				memory = new byte[newMemory.length];
				System.arraycopy(newMemory, 0, memory, 0, newMemory.length);
			}
			
			length = memory.length;
			start = newStart;
		}
		
		public boolean applyBlock()
		{
			System.arraycopy(memory,0,MemoryManager.this.getPool(),start,length);
			return true;
		}
		
		public void resize(int start, int length)
		{
			this.length = length;
			this.start = start;
		}
		
		public void setMemory(byte[] newMemory) {
			memory = newMemory;
		}
		
		public byte[] getMemory() {
			return memory;
		}
		
		public byte[] getMemoryLength() {
			return new byte[]{ memory[0],memory[1] };
		}
		
		public int getStart() {
			return start;
		}
		
		public int getLength() {
			return length;
		}
		
	}
}
