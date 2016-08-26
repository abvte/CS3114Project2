
public class MemoryBlock {
	private Byte[] memory;
	private Byte[] memoryLength;
	private boolean free;
	private int totalSize;
	
	MemoryBlock(Byte[] newMemory, boolean newFree) {
		free = newFree;
		memory = newMemory;
		memoryLength = new Byte[]{(byte)0x0,(byte)0x0};
		totalSize = memory.length + 2;
	}
	
	MemoryBlock(Byte[] newMemory, Byte[] newMemoryLength, boolean newFree) {
		free = newFree;
		memory = newMemory;
		memoryLength = newMemoryLength;
		totalSize = memory.length + 2;
	}
	
	MemoryBlock(Byte[] newMemory,int newMemoryLength, boolean newFree) {
		free = newFree;
		memory = newMemory;
		memoryLength =  new Byte[] {
				            (byte)(newMemoryLength >>> 8),
				            (byte) newMemoryLength};
		totalSize = memory.length + 2;
	}
	
	public void setMemory(Byte[] newMemory) {
		memory = newMemory;
	}
	
	public void setMemoryLength(Byte[] newMemoryLength) {
		memoryLength = newMemoryLength;
	}
	
	public void setFree(boolean newFree) {
		free = newFree;
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
