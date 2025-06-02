package code_generation.bean;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringJoiner;


/**
 * A binary tree node implementation commonly used in LeetCode-style problems.
 * Each node contains an integer value and references to left and right child nodes.
 * @author: wuxin0011
 * @since 1.0
 */
public class TreeNode {

    /**
     * The integer value stored in this node.
     */
    public int val;

    /**
     * Reference to the left child node.
     */
    public TreeNode left;

    /**
     * Reference to the right child node.
     */
    public TreeNode right;

    /**
     * Constructs a tree node with the given value and no children.
     *
     * @param val the integer value to store in this node
     */
    public TreeNode(int val) {
        this(val, null, null);
    }

    /**
     * Constructs a tree node with the given value and child nodes.
     *
     * @param val the integer value to store in this node
     * @param left reference to the left child node
     * @param right reference to the right child node
     */
    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    /**
     * Returns a string representation of this tree node.
     *
     * @return a string containing the node's value
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", TreeNode.class.getSimpleName() + "[", "]")
                .add("val=" + val)
                .toString();
    }

    /**
     * Creates a complete binary tree with 4 levels (15 nodes) where each node's value
     * corresponds to its position in a level-order traversal (0-14).
     *
     * @return the root node of the complete binary tree
     */
    public static TreeNode fullTreeNode() {
        // First level
        TreeNode treeNode = new TreeNode(0);

        // Second level
        treeNode.left = new TreeNode(1);
        treeNode.right = new TreeNode(2);

        // Third level
        treeNode.left.left = new TreeNode(3);
        treeNode.left.right = new TreeNode(4);
        treeNode.right.left = new TreeNode(5);
        treeNode.right.right = new TreeNode(6);

        // Fourth level
        treeNode.left.left.left = new TreeNode(7);
        treeNode.left.left.right = new TreeNode(8);
        treeNode.left.right.left = new TreeNode(9);
        treeNode.left.right.right = new TreeNode(10);
        treeNode.right.left.left = new TreeNode(11);
        treeNode.right.left.right = new TreeNode(12);
        treeNode.right.right.left = new TreeNode(13);
        treeNode.right.right.right = new TreeNode(14);

        return treeNode;
    }

    /**
     * Builds a binary tree from a level-order traversal array (string format).
     * Supports "null" or "#" to represent empty nodes (compatible with LeetCode and Niuke formats).
     *
     * @param ls the level-order traversal array where each element is either a number or null marker
     * @return the root node of the constructed binary tree
     */
    public static TreeNode widthBuildTreeNode(String[] ls) {
        if (ls == null || ls.length == 0 || ls[0] == null || isNullNode(ls[0])) {
            return null;
        }
        Deque<TreeNode> q = new ArrayDeque<>();
        TreeNode root = new TreeNode(Integer.parseInt(ls[0]));
        q.add(root);
        int i = 1;
        while (i < ls.length && !q.isEmpty()) {
            int s = q.size();
            while (s > 0 && i < ls.length) {
                s--;
                TreeNode node = q.poll();
                if (node == null) continue;
                if (!isNullNode(ls[i])) {
                    TreeNode left = new TreeNode(Integer.parseInt(ls[i]));
                    node.left = left;
                    q.add(left);
                }
                i++;
                if (i >= ls.length) break;
                if (!isNullNode(ls[i])) {
                    TreeNode right = new TreeNode(Integer.parseInt(ls[i]));
                    node.right = right;
                    q.add(right);
                }
                i++;
            }
        }
        return root;
    }

    /**
     * Checks if a string represents a null node in serialized tree formats.
     *
     * @param s the string to check
     * @return true if the string represents a null node ("null", "#", or empty)
     */
    private static boolean isNullNode(String s) {
        return s == null || s.length() == 0 || "null".equals(s) || "#".equals(s);
    }

    /**
     * Builds a binary tree from a level-order traversal array (Integer format).
     * Supports null values to represent empty nodes.
     *
     * @param ls the level-order traversal array where each element is either an Integer or null
     * @return the root node of the constructed binary tree
     */
    public static TreeNode widthBuildTreeNode(Integer[] ls) {
        if (ls == null || ls.length == 0 || ls[0] == null || "null".equals(String.valueOf(ls[0]))) {
            return null;
        }
        String[] s = new String[ls.length];
        for (int i = 0; i < ls.length; i++) {
            s[i] = String.valueOf(ls[i]);
        }
        return widthBuildTreeNode(s);
    }
}