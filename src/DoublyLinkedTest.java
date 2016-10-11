import student.TestCase;

/**
 * @author Kevin Zhang
 * @author Adam Bishop
 * @version 1.0
 */
public class DoublyLinkedTest extends TestCase {
    /**
     * Tests doubly linked list commands
     */
    public void testDoublyLinkedList() {
        DoublyLinkedList<String> newlist = new DoublyLinkedList<String>();
        assertNotNull(newlist);
        newlist.add("First");
        newlist.add("Second");
        newlist.append("Third");
        // make sure there are 5 items(3 items + 2)
        assertEquals(5, newlist.getSize());
        // make sure that getNodedata method works
        assertEquals("Second", newlist.getCurrent().getNodeData());
        newlist.stepForward();
        // Make sure that stepping forward was successful
        assertEquals("First", newlist.getData());
        newlist.stepBack();
        // Make sure that stepping back was successful
        assertEquals("Second", newlist.getData());
        newlist.jumpToTail(); // go to tail
        newlist.stepBack();
        // make sure that jumpToTail was successful
        assertEquals("Third", newlist.getData());
        // make sure that remove returns the correct data
        assertEquals("Third", newlist.remove().getNodeData());
        // make sure that remove was successfully
        assertEquals(4, newlist.getSize());
        // go to head
        newlist.jumpToHead();
        // Checks for Head's data
        assertEquals(null, newlist.getData());
    }

    /**
     * Tests removing the head
     */
    public void testLinkedListRemoveNull() {
        DoublyLinkedList<String> newlist = new DoublyLinkedList<String>();
        assertNotNull(newlist);
        Node<String> nullNode = newlist.remove();
        assertEquals(null, nullNode); // Checks for Head's data
    }

}