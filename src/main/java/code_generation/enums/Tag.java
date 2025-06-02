package code_generation.enums;

/**
 * An enumeration representing different problem categories/tags for coding problems.
 * Each tag has a Chinese description and can be used to classify problems by their
 * primary data structure or algorithmic concept.
 * @author wuxin0011
 * @since 1.0
 */
public enum Tag {

    /**
     * Problems primarily dealing with string manipulation and operations
     */
    STRING("字符串"),

    /**
     * Problems that don't fit into other specific categories
     */
    OTHER("其他"),

    /**
     * Problems involving singly linked list operations
     */
    LINKLIST("单链表"),

    /**
     * Problems focused on array manipulation and algorithms
     */
    ARRAY("数组"),

    /**
     * Problems involving tree data structures and algorithms
     */
    TREE("树"),

    /**
     * Problems dealing with graph theory and graph algorithms
     */
    GRAPHICAl("图"),

    /**
     * Default tag for problems without a specified category
     */
    NULL("unknown");

    /**
     * Chinese description of the tag
     */
    private String tag;

    /**
     * Constructs a Tag enum constant with the specified Chinese description.
     * @param tag The Chinese description of the problem tag
     */
    Tag(String tag) {
        this.tag = tag;
    }

    /**
     * Gets the Chinese description of this problem tag.
     * @return The Chinese tag description
     */
    public String getTag() {
        return this.tag;
    }
}