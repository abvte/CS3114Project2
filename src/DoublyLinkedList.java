
public class DoublyLinkedList<E> {
  private Node<E> head;      // Pointer to list header
  private Node<E> tail;      // Pointer to last element
  private Node<E> current;   // Pointer to current Node
  private int size;   // # of Nodes currently in list
  
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
	  current = current.getAfter();
	  
	  if(current.equals(tail)) return false;
	  
	  return true;
  }
  
  public boolean stepBack() {
	  current = current.getBefore();
	  
	  if(current.equals(head)) return false;
	  
	  return true;
  }
  
  public E getData() {
	  return current.getNodeData();
  }
  
  public Node<E> getCurrent() {
	  return current;
  }
  
  public boolean jumpToHead()
  {
	  current = head;
	  return current.equals(head);
  }
  
  public boolean jumpToTail()
  {
	  current = tail;
	  return current.equals(tail);
  }
  
  public int getSize()
  {
	  return size;
  }

  public void setCurrent(Node<E> newNode)
  {
	  current = newNode;
  }
  
}