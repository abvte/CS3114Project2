
/**
 * Hashtable data structure with quadratic probing for collision avoidance
 * @author Adam Bishop
 * @author Jinwoo Yom
 * @version 1.0
 */
public class Hashtable {
    private Hash[] table;  //Table holding hash objects
    private int size;  //Max items
    private int items; //Current amount of items
    private String name;   //Name for use when outputting 
                           //which hashtable expanded

    /**
     * Constructor
     * @param newSize new size
     * @param newName new name
     */
    Hashtable(int newSize,  String newName) {
        table = new Hash[newSize];
        size = newSize;
        items = 0;
        name = newName;
    }

    /**
     * Adds a new (key, value) hash to the table
     * @param key new key
     * @param value new value
     * @return true or false
     */
    public boolean add(String key,  Object value)
    {
        Hash newHash = new Hash(key, value);
        
        //Quadratic probing variable
        int i = 1;
        
        int hash = h(key, this.size);
        int pos = hash;
        while (table[pos] != null && table[pos].getKey() != null)
        {
            pos = (hash + (i * i)) % size;
            i++;
        }
        
        table[pos] = newHash;
        items++;
        return true;
    }

    /**
     * Queries the hashtable and returns a value if it exists
     * @param key in key/value pair
     * @return value in key/value pair
     */
    public Object get(String key)
    {
        int i = 1;
        int hash = h(key, this.size);
        int pos = hash;

        while (table[pos] != null &&
              (table[pos].getKey() == null ||
              !table[pos].getKey().equals(key)))
        {
            pos = (hash + (i * i)) % size;
            i++;
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
     * @param key in key/value pair
     * @return true or false based on success
     */
    public boolean remove(String key)
    {
        //Quadratic probing variable
        int i = 1;
        
        int hash = h(key, this.size);
        int pos = hash;
        while (table[pos] != null &&
              (table[pos].getKey() == null ||
              !table[pos].getKey().equals(key)))
        {
            pos = (hash + (i * i)) % size;
            i++;
        }
        
        // Didn't find key in table
        if (table[pos] == null) {
            return false;
        }
        table[pos] = new Hash(null, null);
        items--;
        
        return true;
    }

    /**
     * Doubles the size of the hashtable
     */
    public void extend()
    {
        //Store old values
        Hash[] oldTable = table;
        int oldSize = size;

        size = size * 2;
        table = new Hash[size];   //Create larger table
        items = 0;

        //Rehash all stored hashes
        for (int i = 0; i < oldSize; i++)
        {
            Hash oldHash = oldTable[i];

            if (oldHash != null && oldHash.getKey() != null) 
            {
                add(oldHash.getKey(), oldHash.getValue());
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
    private int h(String s,  int m)
    {
        int intLength = s.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++)
        {
            char[] c = s.substring(j * 4, (j * 4) + 4).toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++)
            {
                sum += c[k] * mult;
                mult *= 256;
            }
        }

        char[] c = s.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++)
        {
            sum += c[k] * mult;
            mult *= 256;
        }

        return (int)(Math.abs(sum) % m);
    }

    /**
     * @return table
     */
    public Hash[] getTable()
    {
        return table;
    }

    /**
     * @return size
     */
    public int getSize()
    {
        return size;
    }

    /**
     * @return items
     */
    public int getItems()
    {
        return items;
    }
}
