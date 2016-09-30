/**
 * Elements in the tree class
 * 
 * @author Kevin Zhang
 * @author Adam Bishop
 * @version 1.0
 * 
 *
 */
class TreeNode {
    protected KVPair pair1;
    protected KVPair pair2;

    /**
     * Constructor
     * 
     * @param firstPair
     *            First key-value pair
     * @param secondPair
     *            Second key-value pair
     */
    public TreeNode(KVPair firstPair, KVPair secondPair) {
        pair1 = firstPair;
        pair2 = secondPair;
    }

    /**
     * Getter for pair 1
     * 
     * @return One of the key-value pair
     */
    public KVPair getPair1() {
        return pair1;
    }

    /**
     * Setter for pair 1
     * 
     * @param pair
     *            Pair to set one of the values
     */
    public void setPair1(KVPair pair) {
        pair1 = pair;
    }

    /**
     * Getter for pair 2
     * 
     * @return One of the key-value pair
     */
    public KVPair getPair2() {
        return pair2;
    }

    /**
     * Setter for pair 2
     * 
     * @param pair
     *            Pair to set one of the values
     */
    public void setPair2(KVPair pair) {
        pair2 = pair;
    }
    
    public void swap() {
        KVPair temp = pair1;
        pair1 = pair2;
        pair2 = pair1;
    }

}

/**
 * @author Kevin Zhang
 * @author Adam Bishop
 * @version 1
 *
 */
class LeafNode extends TreeNode {
    private TreeNode next;

    /**
     * @param firstPair
     *            First key-value pair
     * @param secondPair
     *            Second key-value pair
     */
    public LeafNode(KVPair firstPair, KVPair secondPair, TreeNode nextNode) {
        super(firstPair, secondPair);
        next = nextNode;
    }

    /**
     * @return next node of the leaf
     */
    public TreeNode getNext() {
        return next;
    }

    /**
     * @param node
     *            Next node of the leaf
     */
    public void setNext(TreeNode node) {
        next = node;
    }
}

/**
 * @author Kevin Zhang
 * @author Adam Bishop
 * @version 1
 *
 */
class InternalNode extends TreeNode {
    private TreeNode left;
    private TreeNode center;
    private TreeNode right;

    /**
     * @param firstPair
     *            First key-value pair
     * @param secondPair
     *            Second key-value pair
     * @param leftNode
     *            Pointer to the left node
     * @param centerNode
     *            Pointer to the center node
     * @param rightNode
     *            Pointer to the right node
     */
    public InternalNode(KVPair firstPair, KVPair secondPair, TreeNode leftNode,
            TreeNode centerNode, TreeNode rightNode) {
        super(firstPair, secondPair);
        left = leftNode;
        center = centerNode;
        right = rightNode;
    }

    /**
     * @param leftNode
     *            Pointer to the left node to set
     */
    public void setLeft(TreeNode leftNode) {
        left = leftNode;
    }

    /**
     * @param rightNode
     *            Pointer to the right node to set
     */
    public void setRight(TreeNode rightNode) {
        right = rightNode;
    }

    /**
     * @param centerNode
     *            Pointer to the center node to set
     */
    public void setCenter(TreeNode centerNode) {
        center = centerNode;
    }

    /**
     * @return Pointer to the left node
     */
    public TreeNode getLeft() {
        return left;
    }

    /**
     * @return Pointer to the right node
     */
    public TreeNode getRight() {
        return right;
    }

    /**
     * @return Pointer to the center node
     */
    public TreeNode getCenter() {
        return center;
    }
}