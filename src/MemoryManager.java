public class MemoryManager {
	byte[] pool;
	Hashtable artists;
	Hashtable songs;
	DoublyLinkedList<MemoryBlock> freeBlocks;
	int blockSize;
	String commandFile;
	
	MemoryManager(int hashSize, int newBlockSize, String newCommandFile)
	{
		freeBlocks = new DoublyLinkedList<MemoryBlock>();
		artists = new Hashtable(hashSize,"Artist");
		songs = new Hashtable(hashSize,"Song");
		pool = new byte[newBlockSize];
		blockSize = newBlockSize;
		commandFile = newCommandFile;
		
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
	
	private void expandPool()
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
		
		if(freeBlocks.jumpToHead()) {
			while(freeBlocks.stepForward()) {
				position++;
				MemoryBlock currentBestFit = freeBlocks.getCurrent().getNodeData();
				difference = currentBestFit.getSize() - record.length();
				if(difference < min && difference >= 2) {
					min = difference;
				}
			}
			
			//This means that no available block was found
			if(min == Integer.MAX_VALUE) return null;
			
			freeBlocks.jumpToHead();
			for(int i = 0; i < position; i++) {
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
		private byte[] memory;
		private byte[] memoryLength;
		private int memoryLengthAsInt;
		private boolean free;
		private int totalSize;
		private int start;
		
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
		
		public boolean applyBlock()
		{
			System.arraycopy(memoryLength,0,MemoryManager.this.getPool(),start,2);
			System.arraycopy(memory,0,MemoryManager.this.getPool(),start+2,memoryLengthAsInt);
			return true;
		}
		
		public void resize(int start, int end)
		{
			if(free) {
				memoryLengthAsInt = end - start;
				this.start = start;
				totalSize = memoryLengthAsInt;
			}
		}
		
		public void setMemory(byte[] newMemory) {
			memory = newMemory;
		}
		
		public void setMemoryLength(byte[] newMemoryLength) {
			memoryLength = newMemoryLength;
		}
		
		public void setFree(boolean newFree) {
			free = newFree;
			
			if(!free) totalSize = memory.length + 2;
			else 	  totalSize = memory.length;
		}
		
		public byte[] getMemory() {
			return memory;
		}
		
		public byte[] getMemoryLength() {
			return memoryLength;
		}
		
		public boolean getFree() {
			return free;
		}
		
		public int getSize() {
			return totalSize;
		}
		
		public int getStart() {
			return start;
		}
		
	}
}
