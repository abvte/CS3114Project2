/**
 * Hashtable data structure with quadratic probing for collision avoidance
 * 
 * @author Adam Bishop
 * @author Jinwoo Yom
 * @version 1.0
 * @param <T>
 *            is the value data type
 */
@SuppressWarnings("unchecked")
public class Hashtable<T> {
    private Hash<T>[] table; // Table holding hash objects
    private int size; // Max items
    private int items; // Current amount of items
    private String name; // Name for use when outputting
                         // which hashtable expanded
    private MemoryManager converter;

    /**
     * Constructor
     * 
     * @param newSize
     *            new size
     * @param newName
     *            new name
     * @param convert
     *            Object to convert bytes to strings          
     */
    Hashtable(int newSize, String newName, MemoryManager convert) {
        table = new Hash[newSize];
        size = newSize;
        items = 0;
        name = newName;
        converter = convert;
    }

    /**
     * Adds a new (key, value) hash to the table
     * 
     * @param key
     *            new key
     * @param value
     *            new value
     * @return true or false
     */
    public T add(String key, T value) {
        Hash<T> newHash = new Hash<T>(value);

        // Quadratic probing variable
        int i = 1;

        int hash = h(key, this.size);
        int pos = hash;
        while (table[pos] != null && table[pos].getValue() != null) {
            pos = (hash + (i * i)) % size;
            i++;
            if (pos == hash) { // We are back at the original position
                return null;
            }
        }

        table[pos] = newHash;
        items++;
        return newHash.getValue();
    }

    /**
     * Queries the hashtable and returns a value if it exists
     * 
     * @param key
     *            in key/value pair
     * @param pool
     *            Byte pool          
     * @return value in key/value pair
     */
    public T get(String key, byte[] pool) {
        int i = 1;
        int hash = h((String) key, this.size);
        int pos = hash;

        while (table[pos] != null
                && (table[pos].getValue() == null || !converter
                        .handle2String((Handle) table[pos].getValue(), pool)
                        .equals(key))) {
            pos = (hash + (i * i)) % size;
            i++;
            if (pos == hash) { // We are back at the original position
                return null;
            }
        }

        if (table[pos] == null) {
            return null;
        }
        else {
            return table[pos].getValue();
        }
    }

    /**
     * Removes key/value pair
     * 
     * @param key
     *            in key/value pair
     * @param pool
     *            Byte pool           
     * @return true or false based on success
     */
    public T remove(String key, byte[] pool) {
        // Quadratic probing variable
        int i = 1;

        int hash = h(key, this.size);
        int pos = hash;
        while (table[pos] != null
                && (table[pos].getValue() == null || !converter
                        .handle2String((Handle) table[pos].getValue(), pool)
                        .equals(key))) {
            pos = (hash + (i * i)) % size;
            i++;
            if (pos == hash) { // We are back at the original position
                return null;
            }
        }

        // Didn't find key in table
        if (table[pos] == null) {
            return null;
        }
        Hash<T> temp = table[pos];
        table[pos] = new Hash<T>(null);
        items--;

        return temp.getValue();
    }

    /**
     * Doubles the size of the hashtable
     * @param pool
     *              Byte pool
     */
    public void extend(byte[] pool) {
        // Store old values
        Hash<T>[] oldTable = table;
        int oldSize = size;

        size = size * 2;
        table = new Hash[size]; // Create larger table
        items = 0;

        // Rehash all stored hashes
        for (int i = 0; i < oldSize; i++) {
            Hash<T> oldHash = oldTable[i];

            if (oldHash != null && oldHash.getValue() != null) {
                add(converter.handle2String((Handle) oldHash.getValue(), pool),
                        oldHash.getValue());
            }
        }

        System.out.println(name + " hash table size doubled.");
    }

    /**
     * Compute the hash function. Uses the "sfold" method from the OpenDSA
     * module on hash functions
     *
     * @param s
     *            The string that we are hashing
     * @param m
     *            The size of the hash table
     * @return The home slot for that string
     */
    private int h(String s, int m) {
        int intLength = s.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++) {
            char[] c = s.substring(j * 4, (j * 4) + 4).toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++) {
                sum += c[k] * mult;
                mult *= 256;
            }
        }

        char[] c = s.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++) {
            sum += c[k] * mult;
            mult *= 256;
        }

        return (int) (Math.abs(sum) % m);
    }

    /**
     * @return table
     */
    public Hash<T>[] getTable() {
        return table;
    }

    /**
     * @return size
     */
    public int getSize() {
        return size;
    }

    /**
     * @return items
     */
    public int getItems() {
        return items;
    }
}
