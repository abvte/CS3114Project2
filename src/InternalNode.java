/** Internal node subclass
 * @author Kevin Zhang
 * @author Adam Bishop
 * @version 1 */
class InternalNode implements TreeNode {
    private KVPair pair1;
    private KVPair pair2;
    private TreeNode left;
    private TreeNode center;
    private TreeNode right;
    private int count;

    /** Constructor for the internal node
     * @param leftNode Pointer to the left node
     * @param centerNode Pointer to the center node
     * @param rightNode Pointer to the right node */
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

    /** Insert method for internal nodes
     * @param pair Pair to be inserted
     * @return root node */
    public TreeNode insert(KVPair pair) {
        int pair1Comparison = pair.compareTo(pair1);
        int pair2Comparison = 0;
        if (pair2 != null) {
            pair2Comparison = pair.compareTo(pair2);
        }
        if (pair1Comparison > 0 && pair2 == null) { // greater start value than
            TreeNode tempNode = this.getCenter().insert(pair);
            if (tempNode != this.getCenter()) { // We know it has split
                InternalNode internNode = (InternalNode) tempNode;

                this.setCenter(internNode.getLeft());
                this.setRight(internNode.getCenter());
                if (this.getLeft() instanceof LeafNode) {
                    LeafNode temp = (LeafNode) this.getLeft();
                    temp.setNext(this.getCenter());
                    this.left = temp;
                }

            }
        }
        else if (pair1Comparison <= 0) {
            // go left
            TreeNode tempNode = this.getLeft().insert(pair);
            if (tempNode != this.getLeft()) {
                InternalNode internNode = (InternalNode) tempNode;
                if (count == 3) { // We need to split this internal node
                    InternalNode interimNode = new InternalNode(
                            this.getCenter(), this.getRight(), null);
                    this.setLeft(internNode.getLeft());
                    this.setCenter(internNode.getCenter());

                    this.setRight(null);
                    return new InternalNode(this, interimNode, null);
                }
                else {
                    this.setRight(this.getCenter());
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
                InternalNode interimNode = new InternalNode(
                        internNode.getCenter(), this.getRight(), null);
                this.setCenter(internNode.getLeft());
                splitHelper(interimNode, true);

                this.setRight(null);
                return new InternalNode(this, interimNode, null);
            }

        }
        else if (pair1Comparison > 0 && pair2Comparison <= 0) {
            // go center
            TreeNode tempNode = this.getCenter().insert(pair);
            if (tempNode != this.getCenter()) {
                InternalNode internNode = (InternalNode) tempNode;
                if (count == 3) { // We need to split this internal node
                    InternalNode interimNode = new InternalNode(
                            internNode.getCenter(), this.getRight(), null);
                    this.setCenter(internNode.getLeft());
                    splitHelper(interimNode, true);

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
                InternalNode interimNode = new InternalNode(
                        internNode.getLeft(), internNode.getCenter(), null);
                splitHelper(interimNode, false);

                this.setRight(null);
                return new InternalNode(this, interimNode, null);
            }
        }
        return this;
    }

    private TreeNode deleteHelper(TreeNode node, int path) {
        if (node == left && path == 1) {
            // do nothing
        }
        else if (node == center && path == 2) { //simple deletion
            this.setCenter(node);
        }
        else if (node == right && path == 3) { //simple deletion
            this.setRight(right);
        }
        else if (node != null) { //this was from an internal node, restructure
            InternalNode internal = (InternalNode) node;
            InternalNode centerNode = (InternalNode) center;
            //if (path == 1 && this.getLeft() != node || path == 2 && this.getCenter() != node || path == 3 && this.getRight() != node) 
            if (path == 1) {
                if (center.getPair2() != null) { //borrow from sibling
                    internal.setCenter(centerNode.getLeft());
                    centerNode.setLeft(centerNode.getCenter());
                    centerNode.setCenter(centerNode.getRight());
                    centerNode.setRight(null);
                    center = centerNode;
                    this.setLeft(internal);
                }
            }
            this.setPair1(this.getMinimum(0, true));    //Make sure to keep the
            this.setPair2(this.getMinimum(0, false));   //KVPairs up to date
        }
        else if (path == 1) { // left stuff
            if (center.getPair2() != null) {
                left.setPair1(center.getPair1());
                center.swap();
                center.setPair2(null);
                this.setPair1(center.getPair1());
            }
            else if (count == 3) {
                left.setPair1(center.getPair1());
                this.setCenter(right);
                this.setRight(null);
                LeafNode leaf = (LeafNode) left;
                left = leaf.lazySetNext(left, center);
            }
            else {
                LeafNode leaf = (LeafNode) left;    // Conserve next pointers
                LeafNode centerLeaf = (LeafNode) center;
                left = leaf.lazySetNext(left, centerLeaf.getNext());
                left.setPair1(center.getPair1());
                return new InternalNode(left, null, null);
            }
        }
        else if (path == 2) { // center stuff
                if (left.getPair2() != null) {
                    center.setPair1(left.getPair2());
                    left.setPair2(null);
                    this.setPair1(center.getPair1());
                }
                else if (count == 3) {
                    if (right.getPair2() != null) {
                        center.setPair1(right.getPair1());
                        right.setPair1(null);
                        right.swap();
                        this.setPair2(right.getPair1());
                        this.setPair1(center.getPair1());
                    }
                    else {
                        LeafNode leaf = (LeafNode) left;
                        left = leaf.lazySetNext(left, right);
                        this.setCenter(right);
                        this.setRight(null);
                    }
                }
                else {
                    LeafNode leaf = (LeafNode) left;
                    LeafNode centerLeaf = (LeafNode) center;
                    left = leaf.lazySetNext(left, centerLeaf.getNext());
                    return new InternalNode(left, null, null);
                }
                
        }
        else {  // right stuff
            if (center.getPair2() != null) {
                right.setPair1(center.getPair2());
                center.setPair2(null);
                this.setPair2(right.getPair1());
            }
            else {
                LeafNode leaf = (LeafNode) center;
                LeafNode rightLeaf = (LeafNode) right;
                center = leaf.lazySetNext(center, rightLeaf.getNext());
                this.setRight(null);
            }
        }
        return this;
    }

    /** Delete method for internal nodes
     * @param pair Pair to be deleted
     * @return root node */
    public TreeNode delete(KVPair pair) {
        int pair1Comparison = pair.compareTo(pair1);
        int pair2Comparison = 0;
        if (pair2 != null) {
            pair2Comparison = pair.compareTo(pair2);
        }

        if (pair1Comparison > 0 && pair2 == null) {
            // go center
            TreeNode tempNode = this.getCenter().delete(pair);
            return deleteHelper(tempNode, 2);
        }
        else if (pair1Comparison < 0) {
            // go left
            TreeNode tempNode = this.getLeft().delete(pair);
            return deleteHelper(tempNode, 1);
        }
        else if (pair1Comparison == 0 || 0 > pair2Comparison) {
            // go center
            TreeNode tempNode = this.getCenter().delete(pair);
            return deleteHelper(tempNode, 2);
        }
        else {
            // go right
            TreeNode tempNode = this.getRight().delete(pair);
            return deleteHelper(tempNode, 3);
        }
    }

    /** Helps link the nodes
     * @param node Node to connect to
     * @param centerCheck Indicates whether to change center path or right
     *            path */
    public void splitHelper(InternalNode node, boolean centerCheck) {
        LeafNode left;
        LeafNode center;
        if (!(this.getLeft() instanceof LeafNode)) {
            return;
        }
        else {
            left = (LeafNode) this.getLeft();
            center = (LeafNode) this.getCenter();
            if (centerCheck) { // center
                left.setNext(node.getLeft());
            }
            else if (!centerCheck) { // right
                center.setNext(node.getLeft());
            }
            else {
                return;
            }
        }
    }

    /** Setter for left node
     * @param leftNode Pointer to the left node to set */
    public void setLeft(TreeNode leftNode) {
        if (left != null && leftNode == null) {
            count--;
        }
        else if (left == null && leftNode != null) {
            count++;
        }
        left = leftNode;
    }

    /** Setter for right node
     * @param rightNode Pointer to the right node to set */
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

    /** Setter for center node
     * @param centerNode Pointer to the center node to set */
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

    /** Getter for left node
     * @return Pointer to the left node */
    public TreeNode getLeft() {
        return left;
    }

    /** Getter for right node
     * @return Pointer to the right node */
    public TreeNode getRight() {
        return right;
    }

    /** Getter for center node
     * @return Pointer to the center node */
    public TreeNode getCenter() {
        return center;
    }

    /** Getter for pair 1
     * @return One of the key-value pair */
    public KVPair getPair1() {
        return pair1;
    }

    /** Setter for pair 1
     * @param pair Pair to set one of the values */
    public void setPair1(KVPair pair) {
        pair1 = pair;
    }

    /** Getter for pair 2
     * @return One of the key-value pair */
    public KVPair getPair2() {
        return pair2;
    }

    /** Setter for pair 2
     * @param pair Pair to set one of the values */
    public void setPair2(KVPair pair) {
        pair2 = pair;
    }

    /** @param level Height of the tree
     * @param centerCheck Boolean to check if it's the center child
     * @return Minimum value in the tree */
    public KVPair getMinimum(int level, boolean centerCheck) {
        if (level == 0) {
            if (centerCheck) {
                return this.getCenter().getMinimum(1, false);
            }
            else {
                return this.getRight().getMinimum(1, false);
            }
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

    /** Searches recursively through tree
     * @param pair Search key
     * @return Null if not found, an object otherwise */
    public KVPair search(KVPair pair) {
        if (pair1 == null || pair == null) {
            return null;
        }

        int pair1Comparison = pair.compareTo(pair1);
        int pair2Comparison = 0;
        if (pair2 != null) {
            pair2Comparison = pair.compareTo(pair2);
        }

        if (pair1Comparison >= 0 && pair2 == null) { //center
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

    /** @param pair pair to find
     * @return TreeNode the node containing the pair */
//    public TreeNode searchNode(KVPair pair) {
//        if (pair1 == null || pair == null) {
//            return null;
//        }
//
//        int pair1Comparison = pair.compareTo(pair1);
//        int pair2Comparison = 0;
//        if (pair2 != null) {
//            pair2Comparison = pair.compareTo(pair2);
//        }
//
//        if (pair1Comparison >= 0 && pair2 == null) { // greater start value than
//            return this.getCenter().searchNode(pair);
//        }
//        else if (pair1Comparison < 0) { // go left
//            return this.getLeft().searchNode(pair);
//        }
//        else if (pair2Comparison >= 0) { // go right
//            return this.getRight().searchNode(pair);
//        }
//        else { // go center
//            return this.getCenter().searchNode(pair);
//        }
//    }

    /** @param location Handle location
     * @return TreeNode */
    public TreeNode handleSearch(Handle location) {
        if (this.getPair1() == null) {
            return null;
        }
        int pair1Comparison = location.compareTo(pair1.getKey());
        int pair2Comparison = 0;
        if (pair2 != null) {
            pair2Comparison = location.compareTo(pair2.getKey());
        }

        if (pair1Comparison >= 0 && pair2 == null) { // greater start value than
            if ((this.getLeft().getPair1().compareTo(location) == 0
                    && this.getLeft().getPair1() != null)
                    || (this.getLeft().getPair2() != null && this.getLeft()
                            .getPair2().compareTo(location) == 0)) {
                return this.getLeft().handleSearch(location);
            }
            else {
                return this.getCenter().handleSearch(location);
            }
        }
        else if (pair1Comparison < 0) {
            // go left
            return this.getLeft().handleSearch(location);
        }
        else if (pair2Comparison > 0 && pair2 != null) {
            // go right
            return this.getRight().handleSearch(location);
        }
        else {
            if ((this.getLeft().getPair1().compareTo(location) == 0
                    && this.getLeft().getPair1() != null)
                    || (this.getLeft().getPair2() != null && this.getLeft()
                            .getPair2().compareTo(location) == 0)) {
                return this.getLeft().handleSearch(location);
            }
            else {
                return this.getCenter().handleSearch(location);
            }
        }
    }

}