


  /* Node -> Element in the doubly linked list
   * Variables:
   * 	E data -> template data that the Node holds
   * 	Node before -> link to the previous Node
   * 	Node after -> link to the next Node
   */
  public class Node<E> {
	  private E data;
	  private Node<E> before;
	  private Node<E> after;
	  
	  public Node(E data, Node<E> prev, Node<E> next) {
		  this.data = data;
		  this.before = prev;
		  this.after = next;
	  }
	  
	  public E getNodeData() {
		  return data;
	  }
	  
	  public Node<E> getAfter() {
		  return after;
	  }
	  
	  public Node<E> getBefore() {
		  return before;
	  }
	  
	  public void setAfter (Node<E> next) {
		  after = next;
	  }
	  
	  public void setBefore (Node<E> prev) {
		  before = prev;
	  }
  }