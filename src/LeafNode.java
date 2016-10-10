/**
 * Leaf Node subclass
 * 
 * @author Kevin Zhang
 * @author Adam Bishop
 * @version 1
 *
 */
class LeafNode implements TreeNode {
    private KVPair pair1;
    private KVPair pair2;
    private TreeNode next;

    /**
     * Constructor
     * 
     * @param firstPair
     *            First key-value pair
     * @param secondPair
     *            Second key-value pair
     * @param nextNode
     *            Pointer to the next leaf
     */
    public LeafNode(KVPair firstPair, KVPair secondPair, TreeNode nextNode) {
        pair1 = firstPair;
        pair2 = secondPair;
        next = nextNode;
    }

    /**
     * Getter for next pointer
     * 
     * @return next node of the leaf
     */
    public TreeNode getNext() {
        return next;
    }

    /**
     * Setter for next pointer
     * 
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

    /**
     * Returns the minimum value in the leaf node
     * 
     * @param level
     *            Height of the tree
     * @param center
     *            Boolean to check if it's the center child
     * @return Minimum value in the tree
     */
    public KVPair getMinimum(int level, boolean center) {
        return pair1;
    }

    /**
     * Simple swap function
     */
    public void swap() {
        KVPair temp = pair1;
        pair1 = pair2;
        pair2 = temp;
    }

    /**
     * Insert method for leaf node
     * 
     * @param pair
     *            Pair to be inserted
     * @return root
     */
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
            LeafNode splitNode = new LeafNode(this.pair1, this.pair2,
                    this.getNext());
            this.setPair1(pair);
            this.setPair2(null);
            this.setNext(splitNode);
            return new InternalNode(this, splitNode, null);
        }
        else if (pair1Comparison >= 0 && pair2Comparison < 0) {
            // Moves the current pair 1 to the new node
            LeafNode splitNode = new LeafNode(pair, pair2, this.getNext());
            this.setNext(this);
            this.setPair1(pair1);
            this.setPair2(null);
            return new InternalNode(this, splitNode, null);
        }
        else {
            // Moves the current pair 1 to the new node
            LeafNode splitNode = new LeafNode(pair2, pair, this.getNext());
            this.setPair1(pair1);
            this.setPair2(null);
            this.setNext(splitNode);
            return new InternalNode(this, splitNode, null);
        }
    }

    /**
     * Delete method for leaf node
     * 
     * @param pair
     *            Pair to be deleted
     * @return root
     */
    public TreeNode delete(KVPair pair) {
        int pair1Comparison = pair.compareTo(pair1);
        if (pair1Comparison == 0 && pair2 == null) {
            return null;
            // restructure needed
        }
        else if (pair1Comparison == 0) {
            this.swap();
            this.setPair2(null);
        }
        else {
            int pair2Comparison = pair.compareTo(pair2);
            if (pair2Comparison == 0) {
                this.setPair2(null);
            }
        }
        return this;
    }

    /**
     * Searches recursively through tree
     * 
     * @param pair
     *            Search key
     * @return Null if not found, an object otherwise
     */
    public KVPair search(KVPair pair) {
        if (this.getPair1() == null || pair == null) {
            return null;
        }

        if (pair.compareTo(this.pair1) == 0) {
            return pair1;
        }
        else if (this.pair2 != null && pair.compareTo(this.pair2) == 0) {
            return pair2;
        }
        else {
            return null;
        }
    }

    /**
     * @param location
     *            Handle location
     * @return TreeNode
     */
    public TreeNode handleSearch(Handle location) {
        if (this.getPair1() == null) {
            return null;
        }
        TreeNode temp = this;
        LeafNode leafTemp = (LeafNode) temp;
        while (leafTemp != null) {
            if (leafTemp.getPair1().compareTo(location) == 0) {
                return leafTemp;
            }
            else if (leafTemp.getPair2() != null
                    && leafTemp.getPair2().compareTo(location) == 0) {
                return leafTemp;
            }
            else {
                leafTemp = (LeafNode) leafTemp.getNext();
            }
        }
        return null;
    }

    /**
     * Sets next for a leaf node by casting first
     * 
     * @param node
     *            to cast
     * @param nextNode
     *            to set to next
     * @return new node
     */
    public TreeNode lazySetNext(TreeNode node, TreeNode nextNode) {
        LeafNode interimNode = (LeafNode) node;
        interimNode.setNext(nextNode);
        return interimNode;
    }
}