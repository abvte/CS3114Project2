/**
 * Hash to be used in the hashtable class
 *
 * @author CS3114 staff
 * @version August 27, 2016
 * @param <E> is the key data type
 * @param <T> is the value data type
 */

public class Hash<T>
{
    private T value;   //Value which corresponds to the key


    /**
     * Constructor
     * @param newValue the new value
     */
    public Hash(T newValue)
    {
        value = newValue;
    }

    /**
     * @return value
     */
    public T getValue()
    {
        return value;
    }    
}
