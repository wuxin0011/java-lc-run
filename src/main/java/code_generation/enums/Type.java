package code_generation.enums;

/**
 * An enumeration representing various algorithmic and data structure categories
 * commonly encountered in coding problems. Each type is associated with a Chinese
 * description that provides a human-readable representation of the category.
 *
 * This enum can be used to classify problems based on their primary algorithmic
 * concept or data structure, facilitating organization and retrieval of problems
 * by type. The types include fundamental data structures like arrays and trees,
 * as well as algorithmic paradigms such as dynamic programming and greedy algorithms.
 *
 * Each type is immutable, with its description set at construction time. The
 * description can be retrieved using the provided method, and the enum's string
 * representation corresponds to its description.
 * @author wuxin0011
 * @since 1.0
 */
/**
 * An enumeration representing different categories/types of coding problems.
 * Each type corresponds to a specific data structure, algorithm, or problem-solving technique.
 * Provides both programmatic access and human-readable descriptions (in Chinese) for each problem type.
 */
public enum Type {
    /**
     * Represents the category of problems associated with arrays.
     * This type is used to classify coding problems that primarily involve operations
     * or algorithms related to array data structures. The description "数组" provides
     * a human-readable representation of this category in Chinese.
     *
     * Arrays are fundamental data structures used to store and manipulate collections
     * of elements in a linear sequence. Problems classified under this category may
     * involve tasks such as searching, sorting, filtering, or transforming array elements.
     */
    ARRAY("数组"),

    /**
     * Represents the category of problems associated with hash tables.
     * This type is used to classify coding problems that primarily involve operations
     * or algorithms related to hash table data structures. The description "哈希表" provides
     * a human-readable representation of this category in Chinese.
     *
     * Hash tables are data structures that store key-value pairs and provide efficient
     * insertion, deletion, and lookup operations. Problems classified under this category
     * may involve tasks such as implementing hash functions, handling collisions, or
     * performing operations like searching, inserting, or removing elements in a hash table.
     */
    HASHTABLE("哈希表"),

    /**
     * Represents the category of problems associated with recursion.
     * This type is used to classify coding problems that involve solving problems by
     * breaking them down into smaller subproblems of the same type. The description "递归"
     * provides a human-readable representation of this category in Chinese.
     *
     * Recursion is a method where the solution to a problem depends on solutions to
     * smaller instances of the same problem. Problems classified under this category
     * often involve recursive algorithms and require understanding of base cases and
     * recursive cases.
     */
    RECURSION("递归"),

    /**
     * Represents the category of problems associated with string manipulation.
     * The description "字符串" provides a human-readable representation in Chinese.
     * Includes problems involving string operations, pattern matching, and text processing.
     */
    STRING("字符串"),

    /**
     * Represents the category of problems associated with dynamic programming.
     * The description "动态规划" provides a human-readable representation in Chinese.
     * Includes problems that can be broken down into overlapping subproblems and
     * optimal substructures.
     */
    DB("动态规划"),

    /**
     * Represents the category of problems solved using two-pointer technique.
     * The description "双指针" provides a human-readable representation in Chinese.
     * Commonly used for problems involving arrays or linked lists where two pointers
     * traverse the data structure in different ways.
     */
    DOUBLE_POINT("双指针"),

    /**
     * Represents the category of problems associated with sorting algorithms.
     * The description "排序" provides a human-readable representation in Chinese.
     * Includes problems involving various sorting techniques and their applications.
     */
    SORT("排序"),

    /**
     * Represents the category of problems involving bitwise operations.
     * The description "位运算" provides a human-readable representation in Chinese.
     * Includes problems that require manipulation of data at the bit level.
     */
    BITWISE_OPERATION("位运算"),

    /**
     * Represents the category of problems solved using binary search.
     * The description "二分查找" provides a human-readable representation in Chinese.
     * Includes problems where the solution involves dividing the search space in half
     * repeatedly to find a target value.
     */
    BINARY_SEARCH("二分查找"),

    /**
     * Represents the category of problems solved using monotonic stacks.
     * The description "单调栈" provides a human-readable representation in Chinese.
     * A specialized stack that helps solve problems involving next greater/smaller elements.
     */
    SINGLE_STACK("单调栈"),

    /**
     * Represents the category of problems solved using greedy algorithms.
     * The description "贪心" provides a human-readable representation in Chinese.
     * Includes problems where locally optimal choices lead to a global solution.
     */
    GREEDY("贪心"),

    /**
     * Represents the category of problems associated with matrix operations.
     * The description "矩阵" provides a human-readable representation in Chinese.
     * Includes problems involving 2D arrays and matrix-specific algorithms.
     */
    MATRIX("矩阵"),

    /**
     * Represents the category of problems associated with database operations.
     * The description "数据库" provides a human-readable representation in Chinese.
     * Includes SQL problems and database-related algorithms.
     */
    DATABASE("数据库"),

    /**
     * Represents the category of problems involving iterator patterns.
     * The description "迭代器" provides a human-readable representation in Chinese.
     * Includes problems related to implementing or using iterators.
     */
    ITERATOR("迭代器"),

    /**
     * Represents the category of game theory problems.
     * The description "博弈" provides a human-readable representation in Chinese.
     * Includes problems involving game strategies and winning conditions.
     */
    GAME("博弈"),

    /**
     * Represents the category of problems solved using prefix sums.
     * The description "前缀和" provides a human-readable representation in Chinese.
     * Technique for efficient range sum queries on arrays.
     */
    PREFIX_AND("前缀和"),

    /**
     * Represents the category of graph theory problems.
     * The description "图" provides a human-readable representation in Chinese.
     * Includes problems involving nodes, edges, and graph algorithms.
     */
    MAP("图"),

    /**
     * Represents the category of problems involving topological graphs.
     * The description "拓扑图" provides a human-readable representation in Chinese.
     * Includes problems about directed acyclic graphs and topological sorting.
     */
    Topology_Map("拓扑图"),

    /**
     * Represents the category of problems solved using segment trees.
     * The description "线段树" provides a human-readable representation in Chinese.
     * A data structure for efficient range queries and updates.
     */
    SEGMENT_TREE("线段树"),

    /**
     * Represents the category of computational geometry problems.
     * The description "几何" provides a human-readable representation in Chinese.
     * Includes problems involving points, lines, polygons, and other geometric entities.
     */
    GEOMETRY("几何"),

    /**
     * Represents the category of mathematical problems.
     * The description "数学" provides a human-readable representation in Chinese.
     * Includes number theory, combinatorics, and other math-related problems.
     */
    MATH("数学"),

    /**
     * Represents the category of problems using memoization techniques.
     * The description "记忆化搜索" provides a human-readable representation in Chinese.
     * Optimization technique that stores previously computed results.
     */
    MEMORY_BASE_SEARCH("记忆化搜索"),

    /**
     * Represents the category of tree-related problems.
     * The description "树" provides a human-readable representation in Chinese.
     * Includes general tree data structure problems.
     */
    TREE("树"),

    /**
     * Represents the category of binary tree problems.
     * The description "二叉树" provides a human-readable representation in Chinese.
     * Includes traversal, construction, and property-checking problems.
     */
    BINARY_TREE("二叉树"),

    /**
     * Represents the category of balanced tree problems.
     * The description "平衡树" provides a human-readable representation in Chinese.
     * Includes AVL trees, red-black trees, and other self-balancing trees.
     */
    BALANCED_TREE("平衡树"),

    /**
     * Represents the category of problems solved using depth-first search.
     * The description "深度优先遍历" provides a human-readable representation in Chinese.
     * Tree/graph traversal algorithm that explores as far as possible along each branch.
     */
    DEPTH_FIRST_TRAVERSAL("深度优先遍历"),

    /**
     * Represents the category of problems solved using breadth-first search.
     * The description "宽度优先遍历" provides a human-readable representation in Chinese.
     * Tree/graph traversal algorithm that explores all neighbors at current depth first.
     */
    WIDTH_FIRST_TRAVERSAL("宽度优先遍历"),

    /**
     * Represents the category of problems involving divide and conquer strategies.
     * The description "分治" provides a human-readable representation in Chinese.
     * Algorithm design paradigm that recursively breaks problems into subproblems.
     */
    PARTITION("分治"),

    /**
     * Represents the category of stack-related problems.
     * The description "栈" provides a human-readable representation in Chinese.
     * Includes LIFO (Last-In-First-Out) data structure problems.
     */
    STACK("栈"),

    /**
     * Represents the category of queue-related problems.
     * The description "队列" provides a human-readable representation in Chinese.
     * Includes FIFO (First-In-First-Out) data structure problems.
     */
    QUEUE("队列"),

    /**
     * Represents the category of heap-related problems.
     * The description "堆" provides a human-readable representation in Chinese.
     * Includes priority queue implementations and heap algorithms.
     */
    PILE_UP("堆"),

    /**
     * Represents the category of priority queue problems.
     * The description "优先队列" provides a human-readable representation in Chinese.
     * Abstract data type similar to regular queue but with priority elements.
     */
    PRIORITY_QUEUE("优先队列"),

    /**
     * Represents the category of backtracking problems.
     * The description "回溯" provides a human-readable representation in Chinese.
     * Algorithmic technique for finding solutions by trying partial solutions.
     */
    BACKTRACk("回溯"),

    /**
     * Represents the category of problems solved using prefix/suffix techniques.
     * The description "前后缀" provides a human-readable representation in Chinese.
     * Includes problems involving prefix sums, suffix arrays, etc.
     */
    PREFIX_SUF("前后缀"),

    /**
     * Represents the category of problems solved using sliding window technique.
     * The description "滑动窗口" provides a human-readable representation in Chinese.
     * Algorithmic technique for maintaining a subset of data that satisfies certain constraints.
     */
    SLIDE_WINDOW("滑动窗口"),

    /**
     * Represents the category of problems involving union-find data structures.
     * The description "并查集" provides a human-readable representation in Chinese.
     * Data structure that tracks partitions of elements into disjoint sets.
     */
    UNION("并查集"),

    /**
     * Represents the category of problems involving trie data structures.
     * The description "前缀树" provides a human-readable representation in Chinese.
     * Tree-like data structure for efficient string prefix searches.
     */
    TRIE("前缀树"),

    /**
     * Represents the category of problems involving shortest path algorithms.
     * The description "最短路" provides a human-readable representation in Chinese.
     * Includes Dijkstra's, Bellman-Ford, and other shortest path algorithms.
     */
    SHORTEST_PATH("最短路"),

    /**
     * Represents the category of problems involving binary indexed trees.
     * The description "树状数组" provides a human-readable representation in Chinese.
     * Data structure that efficiently maintains prefix sums.
     */
    TREE_ARRAY("树状数组"),

    /**
     * Represents the category of problems involving hash functions.
     * The description "哈希函数" provides a human-readable representation in Chinese.
     * Includes problems about designing or analyzing hash functions.
     */
    HASH_FUNCTION("哈希函数"),

    /**
     * Represents an unknown or unclassified problem type.
     * The description "unknown" indicates the type hasn't been categorized.
     */
    UNKNOWN("unknown");

    final String type;

    /**
     * Private constructor for enum initialization.
     * @param type The Chinese description of the problem type
     */
    private Type(String type) {
        this.type = type;
    }

    /**
     * Gets the Chinese description of the problem type.
     * @return The type description in Chinese
     */
    public String getType() {
        return this.type;
    }

    /**
     * Returns the Chinese description of the problem type.
     * @return The type description in Chinese (same as getType())
     */
    @Override
    public String toString() {
        return this.getType();
    }
}