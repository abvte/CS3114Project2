/**
 * Hash to be used in the hashtable class
 *
 * @author CS3114 staff
 * @version August 27, 2016
 */

public class Hash
{
    private String key; //Key which maps to the value
    private Object value;   //Value which corresponds to the key


    /**
     * Constructor
     * @param newKey the new key
     * @param newValue the new value
     */
    public Hash(String newKey, Object newValue)
    {
        key = newKey;
        value = newValue;
    }

    /**
     * @return key
     */
    public String getKey() 
    {
        return key;
    }

    /**
     * @return value
     */
    public Object getValue()
    {
        return value;
    }    
}
