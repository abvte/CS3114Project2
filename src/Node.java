


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
	  
	  public Node(E data, Node<E> prev, Node<E> next) {			// Node Constructor
		  this.data = data;
		  this.before = prev;
		  this.after = next;
	  }
	  
	  public E getNodeData() {									// returns Node Data
		  return data;
	  }
	  
	  public Node<E> getAfter() {								// returns the next Node
		  return after;
	  }
	  
	  public Node<E> getBefore() {								// returns the previous Node
		  return before;
	  }
	  
	  /*														// We don't need this - Jinwoo
	  public void setData(E data) {								// sets the Node data
		  this.data = data;
	  }*/
	  
	  public void setAfter (Node<E> next) {						// sets the next Node
		  after = next;
	  }
	  
	  public void setBefore (Node<E> prev) {					// sets the previous Node
		  before = prev;
	  }
  }