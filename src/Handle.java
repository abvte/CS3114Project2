/**
 * Implementation for the handle to positions in the memory pool
 * 
 * @author Adam Bishop and Jinwoo Yom
 */
public class Handle implements Comparable<Handle> {
    private int length; // Length of the data
    private int start; // Where in the pool the memory block starts at
    private final int twoByte = 2; // Constant holding the size of two bytes

    /**
     * Memory block constructor. Writes to the pool.
     * 
     * @param newMemory
     *            Memory to place in the block
     * @param newMemoryLength
     *            Length of the memory to place in the block
     * @param newStart
     *            Location in the memory pool
     */
    Handle(byte[] newMemory, byte[] newMemoryLength, int newStart, byte[] pool) {
        // If the length isn't null..use that value
        byte[] memory;
        if (newMemoryLength != null) {
            memory = new byte[newMemory.length + twoByte];
            System.arraycopy(newMemoryLength, 0, memory, 0, 2);
            System.arraycopy(newMemory, 0, memory, 2, newMemory.length);
        } else { // Else if it is null, it's probably a free block
            memory = new byte[newMemory.length];
            System.arraycopy(newMemory, 0, memory, 0, newMemory.length);
        }

        length = memory.length;
        start = newStart;
        System.arraycopy(memory, 0, pool, start, length);
    }

    /**
     * Changes the start and length of the memory block
     * 
     * @param newStart
     *            The new start value
     * @param newLength
     *            The new length value
     */
    public void resize(int newStart, int newLength) {
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

    /**
     * Compares start values of the blocks
     * @param other The block to compare to
     * @return 1 if the other block has a greater start value,
     *         0 if the other block has the same start value, or
     *        -1 if the other block has a lesser start value
     *         
     */
    @Override
    public int compareTo(Handle other) {
        int otherStart = other.getStart();
        int thisStart = this.getStart();
        
        if (otherStart == thisStart) {
            return 0;
        }
        else if (otherStart > thisStart) {
            return 1;
        }
        else {
            return -1;
        }
    }
    
    /**
     * Overload toString
     *
     * @return A print string
     */
    public String toString() {
        return String.valueOf(start);
    }

}
