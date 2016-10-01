/**
 * Elements in the 2-3+ tree class
 * 
 * @author Kevin Zhang
 * @author Adam Bishop
 * @version 1.0
 * 
 *
 */
interface TreeNode {
    void setPair1(KVPair newPair);

    void setPair2(KVPair newPair);

    public KVPair getPair1();

    public KVPair getPair2();

    void swap();

    TreeNode insert(KVPair pair);
    
    KVPair getMinimum(int level, boolean center);
    // remove
}

/**
 * @author Kevin Zhang
 * @author Adam Bishop
 * @version 1
 *
 */
class LeafNode implements TreeNode {
    private KVPair pair1;
    private KVPair pair2;
    private TreeNode next;
    private TreeNode prev;

    /**
     * @param firstPair
     *            First key-value pair
     * @param secondPair
     *            Second key-value pair
     */
    public LeafNode(KVPair firstPair, KVPair secondPair, TreeNode nextNode,
            TreeNode prevNode) {
        pair1 = firstPair;
        pair2 = secondPair;
        next = nextNode;
        prev = prevNode;
    }

    /**
     * @return next node of the leaf
     */
    public TreeNode getNext() {
        return next;
    }

    /**
     * @param node
     *            Previous node of the leaf
     */
    public void setPrev(TreeNode node) {
        prev = node;
    }

    /**
     * @return previous node of the leaf
     */
    public TreeNode getPrev() {
        return prev;
    }

    /**
     * @param node
     *            Next node of the leaf
     */
    public void setNext(TreeNode node) {
        next = node;
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
    
    public KVPair getMinimum(int level, boolean center) {
        return pair1;
    }

    public void swap() {
        KVPair temp = pair1;
        pair1 = pair2;
        pair2 = temp;
    }

    public TreeNode insert(KVPair pair) {
        if (pair1 == null) {
            this.setPair1(pair);
            return this;
        }
        
        int pair1Comparison = pair.compareTo(pair1);
        if (pair1Comparison > 0 && pair2 == null) { // greater start value than
                                                    // pair1
            this.setPair2(pair);
            return this;
        }
        else if (pair1Comparison <= 0 && pair2 == null) { // lesser or equal
                                                          // start value
            this.setPair2(pair);
            this.swap();
            return this;
        }
        int pair2Comparison = pair.compareTo(pair2);

        if (pair1Comparison < 0 && pair2Comparison < 0) { // Split to the left
            TreeNode splitNode = new LeafNode(pair, null, this, null);

            return new InternalNode(splitNode, this, this.next);
        }
        else if (pair1Comparison >= 0 && pair2Comparison < 0) {
            TreeNode splitNode = new LeafNode(this.pair1, null, this, null);
            this.setPair1(pair);

            return new InternalNode(splitNode, this, null);
        }
        else {
            TreeNode splitNode = new LeafNode(pair1, null, this, null);
            this.setPair1(pair);
            this.swap();

            return new InternalNode(splitNode, this, null);
        }
    }
}

/**
 * @author Kevin Zhang
 * @author Adam Bishop
 * @version 1
 *
 */
class InternalNode implements TreeNode {
    private KVPair pair1;
    private KVPair pair2;
    private TreeNode left;
    private TreeNode center;
    private TreeNode right;
    private int count;

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
    public InternalNode(TreeNode leftNode, TreeNode centerNode, 
            TreeNode rightNode) {
        pair1 = null;
        pair2 = null;
        left = leftNode;
        center = centerNode;
        right = rightNode;
        if (leftNode != null) count++;
        if (centerNode != null) {
            count++;
            pair1 = this.getMinimum(0, true);
        }
        if (rightNode != null) {
            count++;
            pair2 = this.getMinimum(0, false);
        }
    }

    public TreeNode insert(KVPair pair) {
        int pair1Comparison = pair.compareTo(pair1);
        int pair2Comparison = 0;
        if (pair2 != null) pair2Comparison = pair.compareTo(pair2);
        
        if (pair1Comparison > 0 && pair2 == null) { // greater start value than
            TreeNode tempNode = this.getCenter().insert(pair);
            if (tempNode != this.getCenter()) { // We know it has split
                InternalNode internNode = (InternalNode) tempNode;
                this.setCenter(internNode.getLeft());
                this.setRight(internNode.getCenter());
            }
        }
        else if (pair1Comparison <= 0) {
            // go left
            TreeNode tempNode = this.getLeft().insert(pair);
            if (tempNode != this.getLeft()) {
                InternalNode internNode = (InternalNode) tempNode;
                
                if (count == 3) { //We need to split this internal node
                    InternalNode interimNode = new InternalNode(internNode.getCenter(), 
                            this.getRight(), null);
                    this.setCenter(internNode.getLeft());
                    this.setRight(null);
                    return new InternalNode(this, interimNode, null);
                }
                else {
                    this.setCenter(internNode.getCenter());
                    this.setLeft(internNode.getLeft());
                }
            }
        }
        else if (pair2Comparison == 0) {
            // go center
            TreeNode tempNode = this.getCenter().insert(pair);
            if (tempNode != this.getCenter()) {
                InternalNode internNode = (InternalNode) tempNode;
                if (count == 3) { //We need to split this internal node
                    InternalNode interimNode = new InternalNode(internNode.getCenter(), 
                            this.getRight(), null);
                    this.setCenter(internNode.getLeft());
                    this.setRight(null);
                    return new InternalNode(this, interimNode, null);
                }
                else {
                    this.setCenter(internNode.getLeft());
                    this.setRight(internNode.getCenter());
                }
            }
        }
        else if (pair1Comparison > 0 && pair2Comparison <= 0) {
            // go center
            TreeNode tempNode = this.getCenter().insert(pair);
            if (tempNode != this.getCenter()) {
                InternalNode internNode = (InternalNode) tempNode;
                if (count == 3) { //We need to split this internal node
                    InternalNode interimNode = new InternalNode(internNode.getCenter(), 
                            this.getRight(), null);
                    this.setCenter(internNode.getLeft());
                    this.setRight(null);
                    return new InternalNode(this, interimNode, null);
                }
                else {
                    this.setCenter(internNode.getLeft());
                    this.setRight(internNode.getCenter());
                }
            }
        }
        else {
            // go right
            TreeNode tempNode = this.getRight().insert(pair);
            if (tempNode != this.getRight()) {  
                InternalNode internNode = (InternalNode) tempNode;
                InternalNode interimNode = new InternalNode(internNode.getLeft(), 
                        internNode.getCenter(), null);
                this.setRight(null);
                return new InternalNode(this, interimNode, null);
            }

        }
        

        return this;
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
        if (right != null) pair2 = this.getMinimum(0, false);
        else pair2 = null;
    }

    /**
     * @param centerNode
     *            Pointer to the center node to set
     */
    public void setCenter(TreeNode centerNode) {
        center = centerNode;
        if (center != null) pair1 = this.getMinimum(0, true);
        else pair1 = null;
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
    
    public KVPair getMinimum(int level, boolean center) {
        if (level == 0) {
            if (center)
                return this.getCenter().getMinimum(1, false);
            else
                return this.getRight().getMinimum(1, false);
        }
        else return this.getLeft().getMinimum(++level, false);
    }

    public void swap() {
        KVPair temp = pair1;
        pair1 = pair2;
        pair2 = temp;
    }
}