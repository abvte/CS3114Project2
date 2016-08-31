import java.nio.ByteBuffer;

public class MemoryManager {
	byte[] pool;
	Hashtable artists;
	Hashtable songs;
	DoublyLinkedList<MemoryBlock> freeBlocks;
	int blockSize;
	String commandFile;
	ByteBuffer buff;
	
	final int TWO_BYTE = 2;
	
	MemoryManager(int hashSize, int newBlockSize, String newCommandFile)
	{
		freeBlocks = new DoublyLinkedList<MemoryBlock>();
		artists = new Hashtable(hashSize,"Artist");
		songs = new Hashtable(hashSize,"Song");
		pool = new byte[newBlockSize];
		blockSize = newBlockSize;
		commandFile = newCommandFile;
		buff = ByteBuffer.allocate(2);
		
		freeBlocks.append(new MemoryBlock(new byte[pool.length],null,0));
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
			if(artists.getItems() == (artists.getSize()/2))
				artists.extend();
		}
		else {
			if(songs.get(record) != null) {
				System.out.println("|" + record + "| duplicates a record "
									   + "already in the song database.");
				return false;
			}
			if(songs.getItems() == (songs.getSize()/2))
				songs.extend();
		}
		
		
		do {
			handle = findBestFit(record);
			if(handle == null) {
				expandPool();
			}
		} while(handle == null);
		
		MemoryBlock newBlock = new MemoryBlock(record.getBytes(),
												   toByte(record.length()),
												   handle.getStart());
		newBlock.applyBlock();
		
		if(artist)
			artists.add(record, newBlock);
		else
			songs.add(record, newBlock);
		
		if( newBlock.getStart() == handle.getStart() &&
		    newBlock.getLength() == handle.getLength()) 
		{
			freeBlocks.remove();
		}
		else {
			handle.resize(handle.getStart() + newBlock.getLength()
				 	 	 ,handle.getLength() - newBlock.getLength());
		}
		if(artist)
			System.out.println("|" + record + "|" + " is added to the artist datbase.");
		else
			System.out.println("|" + record + "|" + " is added to the song database.");

		return true;
	}
	
	private byte[] toByte(int number)
	{
		byte[] out = new byte[] {
	            (byte)(number >>> 8),
	            (byte) number};
		return out;
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
				System.out.println("|" + record + "| is removed from the artist database.");
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
				System.out.println("|" + record + "| is removed from the song database.");
			}
		}
		
		freeBlocks.jumpToHead();
		
		if(freeBlocks.getSize() == 2) {
			freeBlocks.append(handle);
			return true;
		}
		else {
			while(freeBlocks.stepForward()) {
				MemoryBlock currentBlock = freeBlocks.getCurrent().getNodeData();
				if(currentBlock.getStart() + currentBlock.getLength() == handle.getStart()) {
					freeBlocks.stepForward();
					freeBlocks.add(handle);
					checkForMerge(freeBlocks.getCurrent());
					return true;
				}
				else if(currentBlock.getStart() > handle.getStart()) {
					freeBlocks.add(handle);
					checkForMerge(freeBlocks.getCurrent());
					return true;
				}
			}
			freeBlocks.stepBack();
			MemoryBlock currentBlock = freeBlocks.getCurrent().getNodeData();
			if(handle.getStart() > currentBlock.getStart()) {
				freeBlocks.append(handle);
				return true;
			}
				
//			freeBlocks.jumpToHead();
//			if(freeBlocks.stepForward()) {
//				freeBlocks.add(handle);
//				checkForMerge(freeBlocks.getCurrent());
//				return true;
//			}
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
