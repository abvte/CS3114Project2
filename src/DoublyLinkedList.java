
public class DoublyLinkedList<E> {
  private Node<E> head;      // Pointer to list header
  private Node<E> tail;      // Pointer to last element
  private Node<E> current;   // Pointer to current Node
  private int size;   		 // # of Nodes currently in list
  
  DoublyLinkedList() {										// DoublyLinkedList Constructor
	  this.head = new Node<E>(null,null,tail);
	  this.tail = new Node<E>(null,head,null);
	  this.current = this.tail;
	  this.size = 2;
  }
  
  public boolean add(E data) {									// Adds an node after the current node in a list
	  if(current.equals(head)) return false;					// you need to check for Head.
	  current = new Node<E>(data,current.getBefore(),current);	// Otherwise you will add before the head - Jinwoo
	  
	  current.getAfter().setBefore(current);
	  current.getBefore().setAfter(current);
	  size++;
	  return true;
  }
  
  public boolean append(E data) {							// appending a node to the end of the list
	  Node<E> newNode = new Node<E>(data,tail.getBefore(),tail);
	  
	  tail.setBefore(newNode);
	  newNode.getBefore().setAfter(newNode);
	  size++;
	  
	  return true;
  }
  
  public Node<E> remove() {									// removing a current node and returning it
	  if(current.equals(tail) || current.equals(head)) {
		  throw new NullPointerException();
	  }
	  
	  Node<E> oldNode = current;
	  current.getAfter().setBefore(current.getBefore());
	  current.getBefore().setAfter(current.getAfter());
	  current = current.getAfter();
	  size--;
	  
	  return oldNode;
  }
  
  public boolean stepForward() {				// traversing forward in DoublyLinkedList
	  
	  if(current.equals(tail)) return false;	// you need to to this before the next line - Jinwoo
	  
	  current = current.getAfter();
	  
	  return true;
  }
  
  public boolean stepBack() {					// traversing backwards in DoublyLinkedList
	  
	  if(current.equals(head)) return false;	// you need to to this before the next line - Jinwoo
	  
	  current = current.getBefore();
	  
	  return true;
  }
  
  public E getData() {						// returns the current Node's data
	  return current.getNodeData();
  }
  
  public Node<E> getCurrent() {					// gets the current Node
	  return current;
  }
  
  public boolean jumpToHead() {					// modifies the current pointer to the Head
	  current = head;
	  return current.equals(head);
  }
  
  public boolean jumpToTail() {					// modifies the current pointer to the tail
	  current = tail;
	  return current.equals(tail);
  }
  
  public int getSize() {						// returns the size of the list
	  return size;
  }
  
}