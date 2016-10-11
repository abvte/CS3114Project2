/**
 * Main class to hold nodes for the 2-3 Tree. Major functions of this ADT are
 * implemented via the nodes rather than the tree.
 * 
 * @author Adam Bishop
 * @author Kevin Zhang
 * @version 1.0
 *
 */
public class TTTree {
    private TreeNode root;

    /**
     * Constructor
     */
    TTTree() { // Create a filler node for the root
        root = new LeafNode(null, null, null);
    }

    /**
     * Insert method for tree which uses nodes' recursive insert method
     * 
     * @param pair
     *            KVPair to be inserted
     */
    public void insert(KVPair pair) {
        root = root.insert(pair);
    }

    /**
     * Searches for a pair using the nodes' recursive search
     * 
     * @param pair
     *            Pair to search for
     * @return Null if the pair is not found, an object otherwise
     */
    public KVPair search(KVPair pair) {
        if (root.getPair1() == null) { // If tree is empty
            return null;
        }
        else {
            return root.search(pair);
        }
    }

    /**
     * Calls the root node's delete function to recursively delete a KVPair
     * 
     * @param toDelete
     *            Pair to be deleted
     * @return root
     */
    public TreeNode delete(KVPair toDelete) {
        TreeNode newRoot = root.delete(toDelete);
        // the root can turn into a LeafNode, so check
        if (newRoot instanceof InternalNode && newRoot.getPair1() == null) {
            root = ((InternalNode) newRoot).getLeft();
        }
        else { // Else it is a LeafNode
            root = newRoot;
        }
        if (root == null) { // If tree is completely empty
            // Creates a new empty leaf node when the tree is empty
            root = new LeafNode(null, null, null);
        }
        return root;
    }

    /**
     * Method to process handles
     * 
     * @param first
     *            First handle
     * @param second
     *            Second handle
     * @param song
     *            Song name
     * @param artist
     *            Artist name
     * @param insert
     *            Boolean to check if it's insert or delete
     * @return 0 if it doesn't need to be removed from hash tables. 1 if it
     *         needs to be deleted from artist hash table. 2 if it needs to be
     *         deleted from song hash table. 3 if it needs to be deleted from
     *         both hash tables.
     */
    public int processHandles(Handle first, Handle second, String song,
            String artist, boolean insert) {
        // Creates pairs to be inserted
        KVPair firstPair = new KVPair(first, second);
        KVPair secondPair = new KVPair(second, first);
        int removeHash = 0;
        TreeNode artistHandle;
        TreeNode songHandle;
        // Checks to see if it already exists in the tree
        KVPair searchResult = this.search(firstPair);
        if (insert) {
            if (searchResult != null) {
                // When the pair already exists in the tree
                System.out.println("The KVPair (|" + artist + "|,|" + song
                        + "|),(" + first.toString() + "," + second.toString()
                        + ") duplicates a record already in the tree.");
                System.out.println("The KVPair (|" + song + "|,|" + artist
                        + "|),(" + second.toString() + "," + first.toString()
                        + ") duplicates a record already in the tree.");
            }
            else { // Insert into tree
                this.insert(firstPair);
                System.out.println("The KVPair (|" + artist + "|,|" + song
                        + "|),(" + first.toString() + "," + second.toString()
                        + ") is added to the tree.");
                this.insert(secondPair);
                System.out.println("The KVPair (|" + song + "|,|" + artist
                        + "|),(" + second.toString() + "," + first.toString()
                        + ") is added to the tree.");
            }
            return removeHash;
        }
        else { // Delete operation
            if (searchResult == null) {
                System.out.println("The KVPair (|" + artist + "|,|" + song
                        + "|) was not found in the database.");
                System.out.println("The KVPair (|" + song + "|,|" + artist
                        + "|) was not found in the database.");
                return 0;
            }

            this.delete(firstPair);
            System.out.println("The KVPair (|" + artist + "|,|" + song
                    + "|) is deleted from the tree.");
            this.delete(secondPair);
            System.out.println("The KVPair (|" + song + "|,|" + artist
                    + "|) is deleted from the tree.");
            artistHandle = root.handleSearch(first);
            songHandle = root.handleSearch(second);
            if (artistHandle == null && songHandle == null) {
                removeHash = 3; // Remove from both hash tables
            }
            else if (artistHandle != null && songHandle == null) {
                removeHash = 2; // Remove from song hash table
            }
            else if (artistHandle == null && songHandle != null) {
                removeHash = 1; // Remove from artist hash table
            }
            return removeHash; // Else there are still references
        }

    }

    /**
     * @param location
     *            Artist/song to be removed
     * @param artist
     *            Boolean to determine if it is artist or not.
     * @param converter
     *            Byte array
     * @return Pair to be deleted
     */
    public KVPair removeTree(Handle location, boolean artist,
            MemoryManager converter) {
        TreeNode temp = root.handleSearch(location); // grab first handle
        if (temp == null) { // Make sure it is even in the tree
            return null;
        }
        TreeNode artistHandle;
        TreeNode songHandle;
        LeafNode leaf = (LeafNode) temp;
        KVPair firstDeleted = null;
        KVPair secondDeleted = null;
        String firstEntry;
        String secondEntry;
        // If the entry is in the second pair of the node returned
        if (leaf.getPair2() != null
                && leaf.getPair2().compareTo(location) == 0) {
            firstDeleted = leaf.getPair2();
            secondDeleted = new KVPair(leaf.getPair2().getValue(),
                    leaf.getPair2().getKey());
        }
        // If the entry is in the first pair of the node returned.
        // With the way that the statements are arranged, it
        // would delete the first pair before the second pair.
        if (leaf.getPair1().compareTo(location) == 0) {
            firstDeleted = leaf.getPair1();
            secondDeleted = new KVPair(leaf.getPair1().getValue(),
                    leaf.getPair1().getKey());
        }
        // Converts the 2 handles into strings
        firstEntry = converter.handle2String(firstDeleted.getKey(),
                converter.getPool());
        secondEntry = converter.handle2String(firstDeleted.getValue(),
                converter.getPool());
        this.delete(firstDeleted);
        System.out.println("The KVPair (|" + firstEntry + "|,|" + secondEntry
                + "|) is deleted from the tree.");
        this.delete(secondDeleted);
        System.out.println("The KVPair (|" + secondEntry + "|,|" + firstEntry
                + "|) is deleted from the tree.");
        if (artist) { // Searches for remaining entries in the tree
            // 1st pair is artist
            artistHandle = root.handleSearch(firstDeleted.getKey());
            // 2nd pair is song
            songHandle = root.handleSearch(firstDeleted.getValue());
        }
        else {
            // 1st pair is song
            songHandle = root.handleSearch(firstDeleted.getKey());
            // 2nd pair is artist
            artistHandle = root.handleSearch(firstDeleted.getValue());
        }
        if (artistHandle == null && songHandle == null) {
            // Means that both entries need to be deleted from hash tables
            return firstDeleted;
        }
        else if (artistHandle != null && songHandle == null) {
            // Means that artist still exists but song does not
            if (artist) {
                return new KVPair(firstDeleted.getValue(), null);
            }
            else {
                return new KVPair(firstDeleted.getKey(), null);
            }
        }
        else if (artistHandle == null && songHandle != null) {
            // Means that song still exists but artist does not
            if (artist) {
                return new KVPair(firstDeleted.getKey(), null);
            }
            else {
                return new KVPair(firstDeleted.getValue(), null);
            }
        }
        else {
            // Means that both artist and song still exists in tree
            return new KVPair(null, null);
        }
    }

    /**
     * Print method. Uses preorder traversal
     */
    public void print() {
        System.out.println("Printing 2-3 tree:");
        this.preorder(root, "");
    }

    /**
     * List method for TTTree
     * 
     * @param location
     *            Handle location
     * @param pool
     *            MemoryManager object
     */
    public void list(Handle location, MemoryManager pool) {
        String poolItem;
        TreeNode temp = root.handleSearch(location); // Find left-most node
        LeafNode treeList = (LeafNode) temp; // to start listing from
        while (treeList != null) { // While there are nodes to print
            boolean complete = true;
            if (treeList.getPair1().getKey().getStart() == location
                    .getStart()) { // Make sure the node represents the handle
                poolItem = pool.handle2String(treeList.getPair1().getValue(),
                        pool.getPool());
                System.out.println("|" + poolItem + "| ");
                complete = false;
            }
            if (treeList.getPair2() != null && treeList.getPair2().getKey()
                    .getStart() == location.getStart()) { // Check pair2 also
                poolItem = pool.handle2String(treeList.getPair2().getValue(),
                        pool.getPool());
                System.out.println("|" + poolItem + "| ");
                complete = false;
            }
            if (complete) { // This should be true if both nodes above didn't
                break; // contain what we were looking for
            }
            treeList = (LeafNode) treeList.getNext();
        }
    }

    /**
     * Pre-order traversal. Used by the print function
     * 
     * @param node
     *            Root node of tree
     * @param indent
     *            Spacing to print tree
     */
    private void preorder(TreeNode node, String indent) {
        if (node == null || root.getPair1() == null) { // root can be null here
            return;
        }
        if (node.getPair2() != null) { // Full node
            System.out.println(indent + node.getPair1().toString() + " "
                    + node.getPair2().toString());
        }
        else { // Only one node full
            System.out.println(indent + node.getPair1().toString());
        }

        if (node instanceof LeafNode) { // We've already listed the value,
            return; // return.
        }
        else { // Else it is an InternalNode, go to its children
            InternalNode temp = (InternalNode) node;
            this.preorder(temp.getLeft(), "  " + indent);
            this.preorder(temp.getCenter(), "  " + indent);
            this.preorder(temp.getRight(), "  " + indent);
        }
    }
}