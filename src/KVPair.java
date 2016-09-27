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
public class KVPair implements Comparable<KVPair> {
    private Handle key;
    private Handle value;

    /**
     * Constructor
     * 
     * @param keyHandle
     *            Initial key handle
     * @param valueHandle
     *            Initial value handle
     */
    public KVPair(Handle keyHandle, Handle valueHandle) {
        key = keyHandle;
        value = valueHandle;
    }

    /**
     * The magic that lets us compare two KVPairs. KVPairs are all that this
     * knows how to compare against. First compare the key field. If they are
     * identical, then break the tie with the value field.
     * 
     * @param it
     *            The KVPair to compare "this" against
     * @return the usual for a comparable (+, 0, -)
     */
    public int compareTo(KVPair it) {
        int compKey = key.compareTo(it.getKey());
        return compKey == 0 ? value.compareTo(it.getValue()) : compKey;
    }

    /**
     * Compare a KVPair to a Handle, by comparing theKey to the Handle's
     * position. Note that this relies on Handle having a compareTo method.
     * 
     * @param it
     *            The Handle to compare "this" against
     * @return the usual for a comparable (+, 0, -)
     */
    public int compareTo(Handle it) {
        return key.compareTo(it);
    }

    /**
     * Getter for key handle
     * 
     * @return Key handle
     */
    public Handle getKey() {
        return key;
    }

    /**
     * Getter for value handle
     * 
     * @return Value handle
     */
    public Handle getValue() {
        return value;
    }

    /**
     * Setter for key handle
     * 
     * @param keyValue
     *            Key handle to set
     */
    public void setKey(Handle keyValue) {
        key = keyValue;
    }

    /**
     * Setting for value handle
     * 
     * @param valueHandle
     *            Value handle to set
     */
    public void setValue(Handle valueHandle) {
        value = valueHandle;
    }

    /**
     * Overload the standard toString method
     * 
     * @return the printable string
     */
    public String toString() {
        return key.toString() + " " + value.toString();
    }
}