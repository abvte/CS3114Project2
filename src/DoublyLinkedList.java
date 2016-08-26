
public class DoublyLinkedList<E> {
  private Node<E> head;      // Pointer to list header
  private Node<E> tail;      // Pointer to last element
  private Node<E> current;   // Pointer to current Node
  private int size;   // # of Nodes currently in list
  private int blockSize; // Amount of bytes to add when the list is full
  
  DoublyLinkedList() {
	  this.head = new Node<E>(null,null,tail);
	  this.tail = new Node<E>(null,head,null);
	  this.current = this.tail;
	  this.size = 2;
  }
  
  public boolean add(E data) {
	  current = new Node<E>(data,current.getBefore(),current);
	  
	  current.getAfter().setBefore(current);
	  current.getBefore().setAfter(current);
	  size++;
	  return true;
  }
  
  public boolean append(E data) {
	  Node<E> newNode = new Node<E>(data,tail.getBefore(),tail);
	  
	  tail.setBefore(newNode);
	  newNode.getBefore().setAfter(newNode);
	  size++;
	  
	  return true;
  }
  
  public Node<E> remove() {
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
  
  public boolean stepForward() {
	  if(current.equals(tail)) return false;
	  
	  current = current.getAfter();
	  
	  return true;
  }
  
  public boolean stepBack() {
	  if(current.equals(head)) return false;
	  
	  current = current.getBefore();
	  
	  return true;
  }
  
  public E printData() {
	  return current.getNodeData();
  }
  
  public Node<E> getCurrent() {
	  return current;
  }
  
}