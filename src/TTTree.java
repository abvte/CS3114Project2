/**
 * Main class to hold nodes for the 2-3 Tree
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
    TTTree() {
        root = new LeafNode(null, null, null);
    }

    /**
     * Getter for root
     * 
     * @return root of the tree
     */
    public TreeNode getRoot() {
        return root;
    }

    /**
     * Insert method for tree
     * 
     * @param pair
     *            KVPair to be inserted
     */
    public void insert(KVPair pair) {
        root = root.insert(pair);
    }

    /**
     * Searches for a pair
     * 
     * @param pair
     *            Pair to search for
     * @return Null if the pair is not found, an object otherwise
     */
    public KVPair search(KVPair pair) {
        if (root == null) {
            return null;
        }
        else {
            return root.search(pair);
        }
    }

    /**
     * @param toDelete
     *            Pair to be deleted
     * @return root
     */
    public TreeNode delete(KVPair toDelete) {
        TreeNode newRoot = root.delete(toDelete);
        if (newRoot instanceof InternalNode && newRoot.getPair1() == null) {
            root = ((InternalNode) newRoot).getLeft();
        }
        else {
            root = newRoot;
        }
        if (root == null) {
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
     * @return 0 if it doesn't need to be removed from hash tables.
     *         1 if it needs to be deleted from artist hash table.
     *         2 if it needs to be deleted from song hash table.
     *         3 if it needs to be deleted from both hash tables.
     */
    public int processHandles(Handle first, Handle second, String song,
            String artist, boolean insert) {
        KVPair firstPair = new KVPair(first, second);
        KVPair secondPair = new KVPair(second, first);
        int removeHash = 0;
        TreeNode artistHandle;
        TreeNode songHandle;
        if (insert) {
            if (this.search(firstPair) != null) {
                System.out.println("The KVPair (|" + artist + "|,|" + song
                        + "|),(" + first.toString() + "," + second.toString()
                        + ") duplicates a record already in the tree.");
                System.out.println("The KVPair (|" + song + "|,|" + artist
                        + "|),(" + second.toString() + "," + first.toString()
                        + ") duplicates a record already in the tree.");
            }
            else {
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
        else {
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
            else {
                removeHash = 1; // Remove from artist hash table
            }
            return removeHash;
        }

    }

    /**
     * Print method
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
        TreeNode temp = root.handleSearch(location);
        LeafNode treeList = (LeafNode) temp;
        while (treeList != null) {
            boolean complete = true;
            if (treeList.getPair1().getKey().getStart() == location
                    .getStart()) {
                poolItem = pool.handle2String(treeList.getPair1().getValue(),
                        pool.getPool());
                System.out.println("|" + poolItem + "| ");
                complete = false;
            }
            if ((treeList.getPair2() != null) && (treeList.getPair2().getKey()
                    .getStart() == location.getStart())) {
                poolItem = pool.handle2String(treeList.getPair2().getValue(),
                        pool.getPool());
                System.out.println("|" + poolItem + "| ");
                complete = false;
            }
            if (complete) {
                break;
            }
            treeList = (LeafNode) treeList.getNext();
        }
    }

    /**
     * Pre order traversal
     * 
     * @param node
     *            Root node of tree
     * @param indent
     *            Spacing to print tree
     */
    public void preorder(TreeNode node, String indent) {
        if (node == null || root.getPair1() == null) {
            return;
        }
        if (node.getPair2() != null) { // Full node
            System.out.println(indent + node.getPair1().toString() + " "
                    + node.getPair2().toString());
        }
        else { // Only one node full
            System.out.println(indent + node.getPair1().toString());
        }

        if (node instanceof LeafNode) {
            return;
        }
        else {
            InternalNode temp = (InternalNode) node;
            this.preorder(temp.getLeft(), "  " + indent);
            this.preorder(temp.getCenter(), "  " + indent);
            this.preorder(temp.getRight(), "  " + indent);
        }

    }

}