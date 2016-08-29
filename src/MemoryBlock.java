
public class MemoryBlock {
	private Byte[] memory;
	private Byte[] memoryLength;
	private boolean free;
	private int totalSize;
	private int start;
	
	
	MemoryBlock(Byte[] newMemory, boolean newFree) {
		free = newFree;
		memory = newMemory;
		memoryLength = new Byte[]{(byte)0x0,(byte)0x0};
		
		if(!free) totalSize = memory.length + 2;
		else 	  totalSize = memory.length;
	}
	
	MemoryBlock(Byte[] newMemory, Byte[] newMemoryLength, boolean newFree) {
		free = newFree;
		memory = newMemory;
		memoryLength = newMemoryLength;
		
		if(!free) totalSize = memory.length + 2;
		else 	  totalSize = memory.length;
	}
	
	MemoryBlock(Byte[] newMemory,int newMemoryLength, boolean newFree) {
		free = newFree;
		memory = newMemory;
		memoryLength =  new Byte[] {
				            (byte)(newMemoryLength >>> 8),
				            (byte) newMemoryLength};

		if(!free) totalSize = memory.length + 2;
		else 	  totalSize = memory.length;
	}
	
	MemoryBlock(Byte[] newMemory,int newMemoryLength, int newStart, boolean newFree) {
		free = newFree;
		memory = newMemory;
		memoryLength =  new Byte[] {
				            (byte)(newMemoryLength >>> 8),
				            (byte) newMemoryLength};
		start = newStart;
		if(!free) totalSize = memory.length + 2;
		else 	  totalSize = memory.length;
	}
	
	public boolean applyBlock()
	{
		return true;
	}
	
	public void setMemory(Byte[] newMemory) {
		memory = newMemory;
	}
	
	public void setMemoryLength(Byte[] newMemoryLength) {
		memoryLength = newMemoryLength;
	}
	
	public void setFree(boolean newFree) {
		free = newFree;
		
		if(!free) totalSize = memory.length + 2;
		else 	  totalSize = memory.length;
	}
	
	public Byte[] getMemory() {
		return memory;
	}
	
	public Byte[] getMemoryLength() {
		return memoryLength;
	}
	
	public boolean getFree() {
		return free;
	}
	
	public int getSize() {
		return totalSize;
	}
	
}
