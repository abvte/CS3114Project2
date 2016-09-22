/**
 * Elements in the tree class
 * 
 * @author Kevin Zhang
 * @author Adam Bishop
 * @version 1.0
 * 
 * @param <KVPair>
 *
 */
@SuppressWarnings("hiding")
class TreeNode<KVPair> {
    protected KVPair pair1;
    protected KVPair pair2;

    /**
     * Constructor 
     * @param firstPair
     *              First key-value pair 
     * @param secondPair
     *              Second key-value pair 
     */
    public TreeNode(KVPair firstPair, KVPair secondPair) {
        pair1 = firstPair;
        pair2 = secondPair;
    }
    
    /**
     * Getter for pair 1
     * @return One of the key-value pair 
     */
    public KVPair getPair1() {
        return pair1;
    }
    
    /**
     * Setter for pair 1 
     * @param pair
     *             Pair to set one of the values
     */
    public void setPair1(KVPair pair) {
        pair1 = pair;
    }
    
    /**
     * Getter for pair 2
     * @return One of the key-value pair 
     */
    public KVPair getPair2() {
        return pair2;
    }
    
    /**
     * Setter for pair 2
     * @param pair 
     *            Pair to set one of the values
     */
    public void setPair2(KVPair pair) {
        pair2 = pair;
    }
    

}

/**
 * @author Kevin Zhang
 * @author Adam Bishop
 *
 * @param <KVPair>
 */
@SuppressWarnings("hiding")
class LeafNode<KVPair> extends TreeNode<KVPair> {
    
    /**
     * @param firstPair
     *                 First key-value pair
     * @param secondPair
     *                 Second key-value pair
     */
    public LeafNode(KVPair firstPair, KVPair secondPair) {
        super(firstPair, secondPair);
    }
}

/**
 * @author Kevin Zhang
 * @author Adam Bishop
 *
 * @param <KVPair>
 */
@SuppressWarnings("hiding")
class InternalNode<KVPair> extends TreeNode<KVPair> {
    private TreeNode<KVPair> left;
    private TreeNode<KVPair> center;
    private TreeNode<KVPair> right;
    
    /**
     * @param firstPair
     *                 First key-value pair 
     * @param secondPair
     *                 Second key-value pair 
     * @param leftNode
     *                 Pointer to the left node 
     * @param centerNode
     *                 Pointer to the center node 
     * @param rightNode
     *                 Pointer to the right node 
     */
    public InternalNode(KVPair firstPair, KVPair secondPair, TreeNode<KVPair> leftNode, TreeNode<KVPair> centerNode, TreeNode<KVPair> rightNode) {
        super(firstPair, secondPair);
        left = leftNode;
        center = centerNode;
        right = rightNode;
    }
    
    /**
     * @param leftNode
     *                Pointer to the left node to set
     */
    public void setLeft(TreeNode<KVPair> leftNode) {
        left = leftNode;
    }
    
    /**
     * @param rightNode
     *                Pointer to the right node to set
     */
    public void setRight(TreeNode<KVPair> rightNode) {
        right = rightNode;
    }
    
    /**
     * @param centerNode
     *                Pointer to the center node to set 
     */
    public void setCenter(TreeNode<KVPair> centerNode) {
        center = centerNode;
    }
    
    /**
     * @return Pointer to the left node 
     */
    public TreeNode<KVPair> getLeft() {
        return left;
    }
    
    /**
     * @return Pointer to the right node 
     */
    public TreeNode<KVPair> getRight() {
        return right;
    }
    
    /**
     * @return Pointer to the center node 
     */
    public TreeNode<KVPair> getCenter() {
        return center;
    }
}