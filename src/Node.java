/**
 * Element in the doubly linked list
 * @author Adam Bishop
 * @author Jinwoo Yom
 * @version 1.0
 * @param <E>
 */
public class Node<E> {
    /*
     * Variables:
     *  E data -> template data that the Node holds
     *  Node before -> link to the previous Node
     *  Node after -> link to the next Node
     */
    private E data;
    private Node<E> before;
    private Node<E> after;

    /**
     * Constructor
     * @param data held in the node
     * @param prev pointer to the previous node
     * @param next pointer to the next node
     */
    public Node(E data, Node<E> prev, Node<E> next) {
        this.data = data;
        this.before = prev;
        this.after = next;
    }

    /**
     * @return data variable
     */
    public E getNodeData() {
        return data;
    }

    /**
     * @return next node
     */
    public Node<E> getAfter() {
        return after;
    }

    /**
     * @return previous node
     */
    public Node<E> getBefore() {
        return before;
    }

    /**
     * @param next pointer to the next node
     */
    public void setAfter(Node<E> next) {
        after = next;
    }

    /**
     * @param prev pointer to the previous node
     */
    public void setBefore(Node<E> prev) {
        before = prev;
    }
}