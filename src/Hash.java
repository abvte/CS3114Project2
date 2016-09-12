/**
 * Hash to be used in the hashtable class
 *
 * @author CS3114 staff
 * @version August 27, 2016
 * @param E is the key data type
 * @param T is the value data type
 */

public class Hash<E, T>
{
    private E key; //Key which maps to the value
    private T value;   //Value which corresponds to the key


    /**
     * Constructor
     * @param newKey the new key
     * @param newValue the new value
     */
    public Hash(E newKey, T newValue)
    {
        key = newKey;
        value = newValue;
    }

    /**
     * @return key
     */
    public E getKey() 
    {
        return key;
    }

    /**
     * @return value
     */
    public T getValue()
    {
        return value;
    }    
}
