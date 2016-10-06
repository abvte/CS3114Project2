
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
        return root.search(pair);
    }

    // public KVPair delete(KVPair toDelete) {
    // if (search(toDelete) == null) {
    // if ()
    // }
    // }

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
     */
    public void processHandles(Handle first, Handle second, String song,
            String artist) {

        KVPair firstPair = new KVPair(first, second);
        KVPair secondPair = new KVPair(second, first);
        if (this.search(firstPair) != null) {
            System.out.println("The KVPair (|" + artist + "|,|" + song + "|),("
                    + first.toString() + "," + second.toString()
                    + ") duplicates a record already in the tree.");
            System.out.println("The KVPair (|" + song + "|,|" + artist + "|),("
                    + second.toString() + "," + first.toString()
                    + ") duplicates a record already in the tree.");
        }
        else {
            this.insert(firstPair);
            System.out.println("The KVPair (|" + artist + "|,|" + song + "|),("
                    + first.toString() + "," + second.toString()
                    + ") is added to the tree.");
            this.insert(secondPair);
            System.out.println("The KVPair (|" + song + "|,|" + artist + "|),("
                    + second.toString() + "," + first.toString()
                    + ") is added to the tree.");
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
