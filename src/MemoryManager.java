public class MemoryManager {
	byte[] pool;
	Hashtable artists;
	Hashtable songs;
	DoublyLinkedList<MemoryBlock> freeBlocks;
	int blockSize;
	
	MemoryManager(int hashSize, int newBlockSize)
	{
		freeBlocks = new DoublyLinkedList<MemoryBlock>();
		artists = new Hashtable(hashSize,"Artist");
		songs = new Hashtable(hashSize,"Song");
		pool = new byte[newBlockSize];
		blockSize = newBlockSize;
		
		freeBlocks.append(new MemoryBlock(new byte[pool.length],0,0,true));
	}
	
	public boolean insert(String record, boolean artist)
	{
		MemoryBlock handle;
		if(artist) {
			if(artists.get(record) != null) {
				System.out.println("|" + record + "| duplicates a record "
									   + "already in the artist database.");
				return false;
			}
		}
		else {
			if(songs.get(record) != null) {
				System.out.println("|" + record + "| duplicates a record "
									   + "already in the song database.");
				return false;
			}
		}
		
		do {
			handle = findBestFit(record);
			if(handle == null) {
				expandPool();
			}
		} while(handle == null);
		
		MemoryBlock artistHandle = new MemoryBlock(record.getBytes(),record.length(),handle.getStart(),false);
		artistHandle.applyBlock();
		artists.add(record, artistHandle);
		
		if( artistHandle.getStart() == handle.getStart() &&
		    artistHandle.getSize() == handle.getSize()) 
		{
			freeBlocks.remove();
		}
		else {
			handle.resize(handle.getStart() + artistHandle.getSize()
				 	 	 ,handle.getSize());
		}
		
		return true;
	}
	
	public boolean remove(String record, boolean artist)
	{
		MemoryBlock handle;
		if(artist) {
			if(artists.get(record) == null) {
				System.out.println("|" + record + "| does not exist in the artist database.");
				return false;
			} 
			else {
				handle = (MemoryBlock) artists.get(record);
				artists.remove(record);
			}
		}
		else {
			if(songs.get(record) == null) {
				System.out.println("|" + record + "| does not exist in the song database.");
				return false;
			}
			else {
				handle = (MemoryBlock) songs.get(record);
				songs.remove(record);
			}
		}
		
		freeBlocks.jumpToHead();
		while(freeBlocks.stepForward()) {
			MemoryBlock currentBlock = freeBlocks.getCurrent().getNodeData();
			if(currentBlock.getStart() + currentBlock.getSize() == handle.getStart()) {
				handle.setFree(true);
				freeBlocks.add(handle);
				
				checkForMerge(freeBlocks.getCurrent());
				return true;
			}
		}
		return false;
	}
	
	private void expandPool() 						// expand the memory pool by a block size
	{
		byte[] tempPool = pool;
		pool = new byte[pool.length + blockSize];
		System.arraycopy(tempPool, 0, pool, 0, tempPool.length);
		freeBlocks.append(new MemoryBlock(new byte[blockSize],0, pool.length - blockSize, true));
		System.out.println("Memory pool expanded to be " + pool.length + " bytes.");
		
		freeBlocks.jumpToTail();
		freeBlocks.stepBack();
		//freeBlocks.getCurrent().getNodeData().resize(0, pool.length);
		checkForMerge(freeBlocks.getCurrent());
	}
	
	private MemoryBlock findBestFit(String record)
	{
		// Set min to a large value in preparation to find best fit
		int min = Integer.MAX_VALUE;
		int difference;
		int position = 0;
		int foundPosition = 0;
		
		if(freeBlocks.jumpToHead()) {
			while(freeBlocks.stepForward()) {
				position++;
				MemoryBlock currentBestFit = freeBlocks.getCurrent().getNodeData();
				difference = currentBestFit.getSize() - record.length();
				if(difference < min && difference >= 2) {
					min = difference;
					foundPosition = position;
				}
			}
			
			//This means that no available block was found
			if(min == Integer.MAX_VALUE) return null;
			
			freeBlocks.jumpToHead();
			for(int i = 0; i < foundPosition; i++) {
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
			if(blockBefore.getStart() + blockBefore.getSize() == blockCurrent.getStart()) {
				blockBefore.resize(blockBefore.getStart(), blockCurrent.getStart() + blockCurrent.getSize());
				blockPointer = blockPointer.getBefore();
				freeBlocks.remove();
			}
		}

		if (blockAfter != null) {
			if(blockAfter.getStart() + blockAfter.getSize() == blockCurrent.getStart()) {
				blockAfter.resize(blockCurrent.getStart(), blockAfter.getStart() + blockCurrent.getSize());
				freeBlocks.remove();
			}
		}
	}
	
	public byte[] getPool()
	{
		return pool;
	}
	
	public class MemoryBlock {
		private byte[] memory;				// String Data in byte format
		private byte[] memoryLength;		// Length of the string in a byte format
		private int memoryLengthAsInt;		// length of the string
		private boolean free;				// determines if the the memory Block is free or not
		private int totalSize;				// Length of the string (+2 if it's free)
		private int start;					// starting index of the memory
		
/*		
		MemoryBlock(byte[] newMemory, boolean newFree) {
			free = newFree;
			memory = newMemory;
			memoryLength = new byte[]{(byte)0x0,(byte)0x0};
			
			if(!free) totalSize = memory.length + 2;
			else 	  totalSize = memory.length;
		}
		
		MemoryBlock(byte[] newMemory, byte[] newMemoryLength, boolean newFree) {
			free = newFree;
			memory = newMemory;
			memoryLength = newMemoryLength;
			
			if(!free) totalSize = memory.length + 2;
			else 	  totalSize = memory.length;
		}
		
		MemoryBlock(byte[] newMemory,int newMemoryLength, boolean newFree) {
			free = newFree;
			memory = newMemory;
			memoryLength =  new byte[] {
					            (byte)(newMemoryLength >>> 8),
					            (byte) newMemoryLength};

			if(!free) totalSize = memory.length + 2;
			else 	  totalSize = memory.length;
		}
*/		
		MemoryBlock(byte[] newMemory,int newMemoryLength, int newStart, boolean newFree) {
			free = newFree;
			memory = newMemory;
			memoryLengthAsInt = newMemoryLength;
			memoryLength =  new byte[] {
					            (byte)(newMemoryLength >>> 8),
					            (byte) newMemoryLength};
			start = newStart;
			if(!free) totalSize = memory.length + 2;
			else 	  totalSize = memory.length;
		}
		
		public boolean applyBlock()					// writes to the pool
		{
			System.arraycopy(memoryLength,0,MemoryManager.this.getPool(),start,2);
			System.arraycopy(memory,0,MemoryManager.this.getPool(),start+2,memoryLengthAsInt);
			return true;
		}
		
		public void resize(int start, int end)		// resizes the memory block
		{
			if(free) {
				memoryLengthAsInt = end - start;
				this.start = start;
				totalSize = memoryLengthAsInt;
			}
		}
		
		public void setMemory(byte[] newMemory) {			  // sets the memory
			memory = newMemory;
		}
		
		public void setMemoryLength(byte[] newMemoryLength) { // sets the length ofthe memory
			memoryLength = newMemoryLength;
		}
		
		public void setFree(boolean newFree) {				  // sets if memory is free or not
			free = newFree;
			
			if(!free) totalSize = memory.length + 2;
			else 	  totalSize = memory.length;
		}
		
		public byte[] getMemory() {							  // returns the memory
			return memory;
		}
		
		public byte[] getMemoryLength() {					  // returns the length of the memory
			return memoryLength;
		}
		
		public boolean getFree() {							  // returns if the memory is free or not
			return free;
		}
		
		public int getSize() {								  // returns the total size of the memory
			return totalSize;
		}
		
		public int getStart() {								  // returns the starting point of the memory
			return start;
		}
		
	}
}
