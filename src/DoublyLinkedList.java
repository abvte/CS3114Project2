
/**
 * Implementation of the DoublyLinkedList
 * @author Adam Bishop
 * @author Jinwoo Yom
 * @version 1.0
 * @param <E>
 */
public class DoublyLinkedList<E> {
    private Node<E> head;      // Pointer to list header
    private Node<E> tail;      // Pointer to last element
    private Node<E> current;   // Pointer to current Node
    private int size;   // # of Nodes currently in list

    /**
     * Linked list constructor
     */
    DoublyLinkedList() {
        this.head = new Node<E>(null, null, tail);
        this.tail = new Node<E>(null, head, null);
        this.current = this.tail;
        this.size = 2;
    }

    /**
     * Adds a new node and then sets the current node to the next node
     * @param data to put in the node
     * @return true or false based on success
     */
    public boolean add(E data) {
        current = new Node<E>(data, current.getBefore(), current);

        current.getAfter().setBefore(current);
        current.getBefore().setAfter(current);
        size++;
        return true;
    }

    /**
     * Adds a new node at the tail of the list
     * @param data to put in the node
     * @return true or false based on success
     */
    public boolean append(E data) {
        Node<E> newNode = new Node<E>(data, tail.getBefore(), tail);

        tail.setBefore(newNode);
        newNode.getBefore().setAfter(newNode);
        size++;

        return true;
    }

    /**
     * Removes the node that the current node points to
     * @return removed node
     */
    public Node<E> remove() {

        if (current.equals(tail) || current.equals(head)) {
            throw new IllegalArgumentException();
        } 
        Node<E> oldNode = current;
        current.getAfter().setBefore(current.getBefore());
        current.getBefore().setAfter(current.getAfter());
        current = current.getAfter();
        size--;

        return oldNode;
    }

    /**
     * Move the current node to the next node
     * @return true or false depending on if the node is the tail
     */
    public boolean stepForward() {
        current = current.getAfter();

        return !current.equals(tail);
    }

    /**
     * Move the current node to the previous node
     * @return true or false depending on if the node is the head
     */
    public boolean stepBack() {
        current = current.getBefore();

        return !current.equals(head);
    }

    /**
     * @return current node's data
     */
    public E getData() {
        return current.getNodeData();
    }

    /**
     * @return current node
     */
    public Node<E> getCurrent() {
        return current;
    }

    /**
     * Sets the current node to the head
     * @return true or false if the operation succeeded
     */
    public boolean jumpToHead()
    {
        current = head;
        return current.equals(head);
    }

    /**
     * Sets the current node to the tail
     * @return true or false if the operation succeeded
     */
    public boolean jumpToTail()
    {
        current = tail;
        return current.equals(tail);
    }

    /**
     * @return hash table's max size
     */
    public int getSize()
    {
        return size;
    }

    /**
     * @param newNode node to set the current node to
     */
    public void setCurrent(Node<E> newNode)
    {
        current = newNode;
    }

}