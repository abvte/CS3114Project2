
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
        root = new TreeNode(null, null);
        count = 0;
    }

    /**
     * @param pair
     *            KVPair to be inserted
     */
    public void insert(KVPair pair) {
        if (count == 0) {
            if (root.getPair1() == null) {
                root.setPair1(pair);
            }
            else {
                root.setPair2(pair);
                count++;
            }
        }
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
     * 
     */
    public void print() {
        System.out.println("Printing 2-3 tree:");
        if (count == 1) {
            System.out.println(root.getPair1().toString() + " "
                    + root.getPair2().toString());
        }
    }
}
