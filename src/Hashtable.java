
public class Hashtable {
	Hash[] table;
	int size;
	int items;
	int[] locations;
	String name;
	
	Hashtable(int newSize, String newName) {
		table = new Hash[newSize];
		locations = new int[newSize];
		size = newSize;
		items = 0;
		name = newName;
	}
	
	public boolean add(String key, Object value)
	{
		Hash newHash = new Hash(key,value);
		
		//Quadratic probing variable
		int i = 1;
		
		int hash = h(key,this.size);
		int pos = hash;
		while(table[pos] != null)
		{
			pos = (hash + (i*i)) % size;
			i++;
		}
		
		table[pos] = newHash;
		locations[items] = hash;
		
		items++;
		
		//if(items > (size/2)) extend();
		
		return true;
	}
	
	public Hash[] getTable()
	{
		return table;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public int getItems()
	{
		return items;
	}
	
	public Object get(String key)
	{
		int i = 1;
		int hash = h(key,this.size);
		int pos = hash;
		while(table[pos] != null && !table[pos].getKey().equals(key))
		{
			pos = (hash + (i*i)) % size;
			i++;
		}
		
		if(table[pos] == null)
			return null;
		else
			return table[pos].getValue();
	}
	
	public boolean remove(String key)
	{
		//Quadratic probing variable
		int i = 1;
		
		int hash = h(key,this.size);
		int pos = hash;
		while(table[pos] != null && !table[pos].getKey().equals(key))
		{
			pos = (hash + (i*i)) % size;
			i++;
		}
		
		// Didn't find key in table
		if(table[pos] == null) return false;
				
		table[pos] = null;
		
		items--;
		
		return true;
	}
	
	public void extend()
	{
		Hash[] oldTable = table;
		int oldSize = size;
		
		size = size * 2;
		table = new Hash[size];
		locations = new int[size];
		items = 0;

		for(int i = 0; i < oldSize; i++)
		{
			Hash oldHash = oldTable[i];
			
			if(oldHash != null) 
			{
				add(oldHash.key,oldHash.value);
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
    // Make this private in your project.
    // This is private for distributing hash function in a way that will
    // pass milestone 1 without change.
    private int h(String s, int m)
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
}
