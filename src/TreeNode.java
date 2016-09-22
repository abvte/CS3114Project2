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
    private KVPair pair1;
    private KVPair pair2;

    /**
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
     * @return One of the key-value pair 
     */
    public KVPair getPair1() {
        return pair1;
    }
    
    /**
     * @param pair
     *             Pair to set one of the values
     */
    public void setPair1(KVPair pair) {
        pair1 = pair;
    }
    
    /**
     * @return One of the key-value pair 
     */
    public KVPair getPair2() {
        return pair2;
    }
    
    /**
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