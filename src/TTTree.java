
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
    private int depth;
    private int count;

    /**
     * Constructor
     */
    TTTree() {
        root = new LeafNode(null, null, null, null);
        count = 0;
    }

    /**
     * @param pair
     *            KVPair to be inserted
     */
    public void insert(KVPair pair) {
        root = root.insert(pair);
    }

    /**
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
        this.insert(firstPair);
        System.out.println("The KVPair (|" + artist + "|,|" + song + "|),("
                + first.toString() + "," + second.toString()
                + ") is added to the tree.");
        this.insert(secondPair);
        System.out.println("The KVPair (|" + song + "|,|" + artist + "|),("
                + second.toString() + "," + first.toString()
                + ") is added to the tree.");

    }

    /**
     * Print method
     */
    public void print() {
        System.out.println("Printing 2-3 tree:");
        this.preorder(root, "");
    }

    /**
     * Preorder traversal
     * 
     * @param node
     *            Root node of tree
     */
    public void preorder(TreeNode node, String indent) {
        if (node == null) {
            return;
        }
        if (node.getPair2() != null) { // Full node
            System.out.println(indent + node.getPair1().toString() + " "
                    + node.getPair2().toString());
        }
        else { // Only one node full
            System.out.println(indent + node.getPair1().toString());
        }

        if (node instanceof LeafNode)
            return;
        else {
            InternalNode temp = (InternalNode) node;
            this.preorder(temp.getLeft(), "  " + indent);
            this.preorder(temp.getCenter(), "  " + indent);
            this.preorder(temp.getRight(), "  " + indent);
        }

    }
}
