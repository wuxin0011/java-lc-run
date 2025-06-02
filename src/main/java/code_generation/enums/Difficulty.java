package code_generation.enums;


/**
 * Represents the difficulty levels for coding problems.
 * This enum provides standardized difficulty classifications with Chinese descriptions.
 * @author wuxin0011
 * @since 1.0
 */
public enum Difficulty {

    /**
     * Simple difficulty level - suitable for beginners
     */
    SIMPLE("简单"),

    /**
     * Medium difficulty level - requires some programming experience
     */
    MEDIUM("中等"),

    /**
     * Hard difficulty level - challenging problems for experienced programmers
     */
    HARD("困难"),

    /**
     * Unknown or unspecified difficulty level
     */
    NULL("unknown");

    /**
     * The Chinese description of the difficulty level
     */
    final String desc;

    /**
     * Constructs a Difficulty enum constant with the specified description.
     * @param desc The Chinese description of the difficulty level
     */
    Difficulty(String desc) {
        this.desc = desc;
    }

    /**
     * Gets the Chinese description of this difficulty level.
     * @return The Chinese description string
     */
    public String getDesc() {
        return this.desc;
    }
}