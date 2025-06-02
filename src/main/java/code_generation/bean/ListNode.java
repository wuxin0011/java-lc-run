package code_generation.bean;

/**
 * A singly-linked list node implementation commonly used in algorithm problems.
 * Each node contains an integer value and a reference to the next node in the list.
 * @author: wuxin0011
 * @since 1.0
 */
public class ListNode {

    /**
     * The integer value stored in this node.
     */
    public int val;

    /**
     * Reference to the next node in the linked list.
     */
    public ListNode next;

    /**
     * Constructs a list node with the given value and no next node.
     *
     * @param val the integer value to store in this node
     */
    public ListNode(int val) {
        this(val, null);
    }

    /**
     * Constructs a list node with the given value and next node reference.
     *
     * @param val the integer value to store in this node
     * @param next the next node in the linked list, or null if this is the last node
     */
    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    /**
     * Creates a linked list from a variable number of integer arguments.
     * The order of arguments determines the sequence of nodes in the list.
     *
     * @param args variable number of integers to create nodes from
     * @return the head node of the constructed linked list
     * @example
     * {@code
     * // Creates list: 1 -> 2 -> 3
     * ListNode head = ListNode.createListNode(1, 2, 3);
     * }
     */
    public static ListNode createListNode(int... args) {
        ListNode dummy = new ListNode(-1); // Temporary dummy node
        ListNode current = dummy;
        for (int val : args) {
            current.next = new ListNode(val);
            current = current.next;
        }
        return dummy.next; // Return the actual head node
    }

    /**
     * Generates a string representation of the linked list in the format "[v1, v2, v3]".
     * The string shows all node values in order, separated by commas.
     *
     * @param node the head node of the linked list to print
     * @return a formatted string representation of the linked list
     * @example
     * {@code
     * ListNode list = ListNode.createListNode(1, 2, 3);
     * String str = ListNode.print(list); // Returns "[1, 2, 3]"
     * }
     */
    public static String print(ListNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        while (node != null) {
            sb.append(node.val);
            if (node.next != null) {
                sb.append(", ");
            }
            node = node.next;
        }
        sb.append("]");
        return sb.toString();
    }
}
