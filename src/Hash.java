/**
 * Stub for hash table class
 *
 * @author CS3114 staff
 * @version August 27, 2016
 */

public class Hash
{
	String key;
	Object value;

    /**
     * Create a new Hash object.
     */
	
    public Hash(String newKey, Object newValue)
    {
    	key = newKey;
    	value = newValue;
    }

	public String getKey() 
    {
    	return key;
    }
    
    public Object getValue()
    {
    	return value;
    }
    
}
