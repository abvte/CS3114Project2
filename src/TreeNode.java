/**
 * Interface for nodes in the 2-3+ Tree
 * 
 * @author Kevin Zhang
 * @author Adam Bishop
 * @version 1.0
 * 
 *
 */
interface TreeNode {
    /**
     * Setter for left pair
     * 
     * @param newPair
     *            Pair to be set
     */
    void setPair1(KVPair newPair);

    /**
     * Setter for right pair
     * 
     * @param newPair
     *            Pair to be set
     */
    void setPair2(KVPair newPair);

    /**
     * Getter for left pair
     * 
     * @return left pair
     * 
     */
    public KVPair getPair1();

    /**
     * Getter for right pair
     * 
     * @return right pair
     */
    public KVPair getPair2();

    /**
     * Simple swap function
     */
    void swap();

    /**
     * Insert function for tree nodes
     * 
     * @param pair
     *            Pair to be inserted
     * @return root node
     */
    TreeNode insert(KVPair pair);

    /**
     * Helper function with insert to return minimum value
     * 
     * @param level
     *            Height of the tree
     * @param center
     *            Boolean to check if it's the center child
     * @return Minimum value in the tree
     */
    KVPair getMinimum(int level, boolean center);
    // remove

    /**
     * Searches recursively through tree
     * 
     * @param pair
     *            Search key Node to start at
     * @return Null if not found, an object otherwise
     */
    KVPair search(KVPair pair);

    /**
     * @param location
     *            Handle location
     * @return TreeNode
     */
    TreeNode handleSearch(Handle location);
    
    /**
     * @param pair
     *            pair to find
     * @return TreeNode the node containing the pair
     */
    //TreeNode searchNode(KVPair pair);
    
    TreeNode delete(KVPair toDelete);
}
