/**
 * Internal node subclass
 * 
 * @author Kevin Zhang
 * @author Adam Bishop
 * @version 1
 */
class InternalNode implements TreeNode {
    private KVPair pair1;
    private KVPair pair2;
    private TreeNode left;
    private TreeNode center;
    private TreeNode right;
    private int count;

    /**
     * Constructor for the internal node
     * 
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
        if (leftNode != null) {
            count++;
        }
        if (centerNode != null) {
            count++;
            pair1 = this.getMinimum(0, true);
        }
        if (rightNode != null) {
            count++;
            pair2 = this.getMinimum(0, false);
        }
    }

    /**
     * Insert method for internal nodes. Recursively iterates through children
     * 
     * @param pair
     *            Pair to be inserted
     * @return root node
     */
    public TreeNode insert(KVPair pair) {
        int pair1Comparison = pair.compareTo(pair1);
        int pair2Comparison = 0;
        if (pair2 != null) {
            pair2Comparison = pair.compareTo(pair2);
        }
        if (pair1Comparison > 0 && pair2 == null) { // Go center
            TreeNode tempNode = this.getCenter().insert(pair);
            if (tempNode != this.getCenter()) { // We know it has split
                InternalNode internNode = (InternalNode) tempNode;
                this.setCenter(internNode.getLeft());
                this.setRight(internNode.getCenter());
                if (this.getLeft() instanceof LeafNode) { // Update pointers
                    LeafNode temp = (LeafNode) this.getLeft();
                    temp.setNext(this.getCenter());
                    this.left = temp;
                }
            }
        }
        else if (pair1Comparison <= 0) { // go left
            TreeNode tempNode = this.getLeft().insert(pair);
            if (tempNode != this.getLeft()) {
                InternalNode internNode = (InternalNode) tempNode;
                if (count == 3) { // We need to split this internal node
                    InternalNode interimNode = new InternalNode(
                            this.getCenter(), this.getRight(), null);
                    this.setLeft(internNode.getLeft()); // Restructure
                    this.setCenter(internNode.getCenter());
                    this.setRight(null);
                    return new InternalNode(this, interimNode, null);
                }
                else { // Else consolidate into one node
                    this.setRight(this.getCenter());
                    this.setCenter(internNode.getCenter());
                    this.setLeft(internNode.getLeft());
                }
            }
        }
        else if (pair2Comparison == 0
                || (pair1Comparison > 0 && pair2Comparison <= 0)) { // go center
            TreeNode tempNode = this.getCenter().insert(pair);
            if (tempNode != this.getCenter()) {
                InternalNode internNode = (InternalNode) tempNode;
                InternalNode interimNode = new InternalNode(
                        internNode.getCenter(), this.getRight(), null);
                this.setCenter(internNode.getLeft());
                splitHelper(interimNode, false);
                this.setRight(null);
                // Return a new consolidated InternalNode to tell parent node
                // it has restructured.
                return new InternalNode(this, interimNode, null);
            }
        }
        else { // go right
            TreeNode tempNode = this.getRight().insert(pair);
            if (tempNode != this.getRight()) { //We will need to restructure
                InternalNode internNode = (InternalNode) tempNode;
                InternalNode interimNode = new InternalNode(
                        internNode.getLeft(), internNode.getCenter(), null);
                splitHelper(interimNode, false);

                this.setRight(null);
                //Restructure and consolidate into a new InternalNode
                return new InternalNode(this, interimNode, null);
            }
        }
        return this;
    }
    
    /**
     * Handles deletion and splits along left child of an internal node
     * @param node
     *            Node to be deleted
     * @return New internal node if the caller restructured, or the caller 
     *         node if no restructure needed
     */
    private TreeNode deleteHelperLeft(TreeNode node) {
        if (node == left) { //Simple deletion if this is true
            return this;    //Just return this
        }
        else if (node != null) { // internal node restructure needed
            if (left != node) {
                InternalNode internal = (InternalNode) node;
                InternalNode centerNode = (InternalNode) center;
                if (center.getPair2() != null) { //Check if we can borrow
                    internal.setCenter(centerNode.getLeft());
                    centerNode.setLeft(centerNode.getCenter());
                    centerNode.setCenter(centerNode.getRight());
                    centerNode.setRight(null);
                    center = centerNode;
                    this.setLeft(internal);
                }
                else if (count == 3) { //Else check if we have a right sibling
                    internal.setCenter(centerNode.getLeft()); //Merge the
                    internal.setRight(centerNode.getCenter()); //center into
                    setLeft(internal);                        // the left
                    setCenter(right);
                    setRight(null);
                }
                else { //Else we will have to consolidate and restructure
                    internal.setCenter(centerNode.getLeft());
                    internal.setRight(centerNode.getCenter());
                    return new InternalNode(internal, null, null);
                }
            }
            this.setPair1(this.getMinimum(0, true)); // Make sure to keep
                                                     // the
            this.setPair2(this.getMinimum(0, false)); // KVPairs up to date
        }
        else if (center.getPair2() != null) {   //Check the sibling to borrow
            left.setPair1(center.getPair1());
            center.swap();
            center.setPair2(null);
            this.setPair1(center.getPair1());
        }
        else if (count == 3) { // Else see if we can merge the center and left
            left.setPair1(center.getPair1());
            this.setCenter(right);
            this.setRight(null);
            LeafNode leaf = (LeafNode) left;
            left = leaf.lazySetNext(left, center); //Set pointer to center's
        }
        else { //Else we will need to restructure
            LeafNode leaf = (LeafNode) left; // Conserve next pointers
            LeafNode centerLeaf = (LeafNode) center;
            left = leaf.lazySetNext(left, centerLeaf.getNext());
            left.setPair1(center.getPair1());
            return new InternalNode(left, null, null);
        }
        return this; // Should reach here only if restructure unnecessary
    }
    
    /**
     * Handles deletion and splits along center child of an internal node
     * @param node
     *            Node to be deleted
     * @return New internal node if the caller restructured, or the caller 
     *         node if no restructure needed
     */
    private TreeNode deleteHelperCenter(TreeNode node) {
        if (node == center) { // simple deletion
            this.setCenter(node); // Update node
        }
        else if (node != null) {    //Node beneath us restructured
            InternalNode internal = (InternalNode) node;
            InternalNode leftNode = (InternalNode) left;
            InternalNode rightNode = (InternalNode) right;
            if (left.getPair2() != null) {  // Check to see if we can borrow
                internal.setCenter(internal.getLeft());
                internal.setLeft(leftNode.getRight());
                leftNode.setRight(null);
                left = leftNode;
                this.setCenter(internal);
            }
            else if (count == 3) { // Check to see if we can borrow from right
                if (right.getPair2() != null) {
                    internal.setCenter(rightNode.getLeft());
                    rightNode.setLeft(rightNode.getCenter());
                    rightNode.setCenter(rightNode.getRight());
                    rightNode.setRight(null);
                    this.setPair2(rightNode.getPair1());
                    right = rightNode;
                    this.setCenter(internal);
                }
                else { //Else merge the right into the center
                    internal.setRight(internal.getLeft());
                    internal.setCenter(leftNode.getCenter());
                    internal.setLeft(leftNode.getLeft());
                    this.setLeft(internal);
                    this.setCenter(this.getRight());
                    this.setRight(null);
                }
            }
            else { //We will have to restructure, so consolidate the nodes
                internal.setRight(internal.getLeft());
                internal.setCenter(leftNode.getCenter());
                internal.setLeft(leftNode.getLeft());
                return new InternalNode(internal, null, null);
            }
            this.setPair1(this.getMinimum(0, true)); // Make sure to keep
            // the
            this.setPair2(this.getMinimum(0, false)); // KVPairs up to date
        } //We reach this point if the child didn't restructure
        else if (left.getPair2() != null) { //Try borrowing from the left
            center.setPair1(left.getPair2());
            left.setPair2(null);
            this.setPair1(center.getPair1());
        }
        else if (count == 3) { //Else we'll check the right, if it exists
            if (right.getPair2() != null) { //Try to borrow from the right
                center.setPair1(right.getPair1());
                right.setPair1(null);
                right.swap();
                this.setPair2(right.getPair1());
                this.setPair1(center.getPair1());
            }
            else { //Else merge the right into the center
                LeafNode leaf = (LeafNode) left;
                left = leaf.lazySetNext(left, right);
                this.setCenter(right);
                this.setRight(null);
            }
        }
        else { //We will have to restructure since we have one child
            LeafNode leaf = (LeafNode) left;
            LeafNode centerLeaf = (LeafNode) center;
            left = leaf.lazySetNext(left, centerLeaf.getNext());
            return new InternalNode(left, null, null);
        }
        return this;  // Should reach here only if restructure unnecessary
    }
    
    /**
     * Handles deletion and splits along right child of an internal node
     * @param node
     *            Node to be deleted
     * @return New internal node if the caller restructured, or the caller 
     *         node if no restructure needed
     */
    private TreeNode deleteHelperRight(TreeNode node) {
        if (node == right) { // simple deletion
            this.setRight(right); // Update node
        }
        else if (node != null) {    //If the node beneath this restructured
            InternalNode internal = (InternalNode) node;
            InternalNode centerNode = (InternalNode) center;
            if (center.getPair2() != null) {    //Check sibling to borrow from
                internal.setCenter(internal.getLeft()); //Rearrange nodes
                internal.setLeft(centerNode.getRight());
                centerNode.setRight(null); //Exchange nodes
                center = centerNode;
                this.setRight(internal);
            }
            else {  //Else just merge this node with the center node
                centerNode.setRight(internal.getLeft());
                this.setRight(null);
            }
            this.setPair1(this.getMinimum(0, true)); // Make sure to keep
            // the
            this.setPair2(this.getMinimum(0, false)); // KVPairs up to date
        }
        else if (center.getPair2() != null) { // Just rearrange
            right.setPair1(center.getPair2());
            center.setPair2(null);
            this.setPair2(right.getPair1());
        }
        else { //Else the right leaf was deleted, set pointers accordingly
            LeafNode leaf = (LeafNode) center;
            LeafNode rightLeaf = (LeafNode) right;
            center = leaf.lazySetNext(center, rightLeaf.getNext());

            this.setRight(null);
        }
        return this; // Should reach here only if restructure unnecessary
    }
    
    /**
     * Delete method for internal nodes
     * 
     * @param pair
     *            Pair to be deleted
     * @return root node
     */
    public TreeNode delete(KVPair pair) {
        int pair1Comparison = pair.compareTo(pair1);
        int pair2Comparison = 0;
        if (pair2 != null) {
            pair2Comparison = pair.compareTo(pair2);
        }

        if (pair1Comparison > 0 && pair2 == null) { // go center
            TreeNode tempNode = this.getCenter().delete(pair);
            return deleteHelperCenter(tempNode);
        }
        else if (pair1Comparison < 0) { // go left
            TreeNode tempNode = this.getLeft().delete(pair);
            return deleteHelperLeft(tempNode);
        }
        else if (pair1Comparison == 0 || 0 > pair2Comparison) { // go center
            TreeNode tempNode = this.getCenter().delete(pair);
            return deleteHelperCenter(tempNode);
        }
        else { // go right
            TreeNode tempNode = this.getRight().delete(pair);
            return deleteHelperRight(tempNode);
        }
    }

    /**
     * Helps link the nodes
     * 
     * @param node
     *            Node to connect to
     * @param centerCheck
     *            Indicates whether to change center path or right path
     */
    public void splitHelper(InternalNode node, boolean centerCheck) {
        LeafNode leftNode;
        LeafNode centerNode;
        if (!(this.getLeft() instanceof LeafNode)) {
            return;
        }
        else {
            leftNode = (LeafNode) this.getLeft();
            centerNode = (LeafNode) this.getCenter();
            if (centerCheck) { // center
                leftNode.setNext(node.getLeft());
            }
            else { // right
                centerNode.setNext(node.getLeft());
            }
        }
    }

    /**
     * Setter for left node
     * 
     * @param leftNode
     *            Pointer to the left node to set
     */
    public void setLeft(TreeNode leftNode) {
        if (left != null && leftNode == null) {
            count--;
        }
        else if (left == null && leftNode != null) {
            count++;
        }
        left = leftNode;
    }

    /**
     * Setter for right node
     * 
     * @param rightNode
     *            Pointer to the right node to set
     */
    public void setRight(TreeNode rightNode) {
        if (right != null && rightNode == null) {
            count--;
        }
        else if (right == null && rightNode != null) {
            count++;
        }
        right = rightNode;
        if (right != null) {
            pair2 = this.getMinimum(0, false);
        }
        else {
            pair2 = null;
        }
    }

    /**
     * Setter for center node
     * 
     * @param centerNode
     *            Pointer to the center node to set
     */
    public void setCenter(TreeNode centerNode) {
        if (center != null && centerNode == null) {
            count--;
        }
        else if (center == null && centerNode != null) {
            count++;
        }
        center = centerNode;
        if (center != null) {
            pair1 = this.getMinimum(0, true);
        }
        else {
            pair1 = null;
        }
    }

    /**
     * Getter for left node
     * 
     * @return Pointer to the left node
     */
    public TreeNode getLeft() {
        return left;
    }

    /**
     * Getter for right node
     * 
     * @return Pointer to the right node
     */
    public TreeNode getRight() {
        return right;
    }

    /**
     * Getter for center node
     * 
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

    /**
     * @param level
     *            Height of the tree
     * @param centerCheck
     *            Boolean to check if it's the center child
     * @return Minimum value in the tree's path indicated
     */
    public KVPair getMinimum(int level, boolean centerCheck) {
        if (level == 0) {   //If level == 0, we're at the root
            if (centerCheck && center != null) { //
                return this.getCenter().getMinimum(1, false);
            }
            else if (right != null) {
                return this.getRight().getMinimum(1, false);
            }
            return null;
        }
        else {
            return this.getLeft().getMinimum(++level, false);
        }
    }

    /** Simple swap function */
    public void swap() {
        KVPair temp = pair1;
        pair1 = pair2;
        pair2 = temp;
    }

    /**
     * Searches recursively through tree
     * 
     * @param pair
     *            Search key
     * @return Null if not found, an object otherwise
     */
    public KVPair search(KVPair pair) {
        if (pair1 == null || pair == null) {
            return null;
        }

        int pair1Comparison = pair.compareTo(pair1);
        int pair2Comparison = 0;
        if (pair2 != null) {
            pair2Comparison = pair.compareTo(pair2);
        }

        if (pair1Comparison >= 0 && pair2 == null) { // center
            return this.getCenter().search(pair);
        }
        else if (pair1Comparison < 0) { // go left
            return this.getLeft().search(pair);
        }
        else if (pair2Comparison >= 0) { // go right
            return this.getRight().search(pair);
        }
        else { // go center
            return this.getCenter().search(pair);
        }
    }

    /**
     * Returns leftmost node containing the wanted handle
     * 
     * @param location
     *            Handle location
     * @return TreeNode
     */
    public TreeNode handleSearch(Handle location) {
        if (this.getPair1() == null) {
            return null;
        }
        int pair1Comparison = location.compareTo(pair1.getKey());
        int pair2Comparison = 0;
        if (pair2 != null) {
            pair2Comparison = location.compareTo(pair2.getKey());
        }

        if (pair1Comparison <= 0) {
            // Equal to pair 1
            return this.getLeft().handleSearch(location);
        }
        else if (pair2Comparison == 0 && pair2 != null) {
            // Go center since it's equal
            return this.getCenter().handleSearch(location);
        }
        else if (pair2Comparison > 0 && pair2 != null) {
            return this.getRight().handleSearch(location);
        }
        else {
            return this.getCenter().handleSearch(location);
        }
    }

}