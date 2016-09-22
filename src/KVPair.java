/**
 * Class to store key and value pairs for the 2-3+ tree
 * 
 * @author Kevin Zhang
 * @author Adam Bishop
 * @version 1.0
 * 
 * @param <E>
 * @param <T>
 *
 */
public class KVPair<E, T> {
    private E key;
    private T value;

    /**
     * @param keyHandle
     *            Initial key handle
     * @param valueHandle
     *            Initial value handle
     */
    public KVPair(E keyHandle, T valueHandle) {
        key = keyHandle;
        value = valueHandle;
    }

    /**
     * @return Key handle
     */
    public E getKey() {
        return key;
    }

    /**
     * @return Value handle
     */
    public T getValue() {
        return value;
    }

    /**
     * @param keyValue
     *            Key handle to set
     */
    public void setKey(E keyValue) {
        key = keyValue;
    }

    /**
     * @param valueHandle
     *            Value handle to set
     */
    public void setValue(T valueHandle) {
        value = valueHandle;
    }
}