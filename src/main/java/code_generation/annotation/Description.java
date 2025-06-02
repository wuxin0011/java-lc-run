package code_generation.annotation;

import code_generation.enums.Difficulty;
import code_generation.enums.Tag;
import code_generation.enums.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * An annotation used to provide detailed metadata about a coding problem or element.
 * This annotation allows developers to specify various attributes such as a description,
 * associated URL, predefined and custom tags, difficulty level, predefined and custom types,
 * and reference links. It is primarily intended for use with methods or types to document
 * their purpose, context, and classification in a structured manner.
 *
 * The annotation supports both predefined categories (e.g., tags and types) and custom
 * user-defined classifications, enabling flexibility in categorization. Additionally,
 * it provides a way to associate external resources or references for further information.
 *
 * This annotation is retained at runtime, allowing for reflection-based access to its
 * attributes. It can be applied to methods or types, making it versatile for use in
 * documenting code elements within a project.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {

    /**
     *
     * @return 题目描述 简介
     */
    String value() default "";


    /**
     * Returns the URL associated with the description.
     * This method provides a way to retrieve the web address or resource location
     * related to the annotated element, such as a link to the problem description
     * or additional documentation.
     *
     * @return a string representing the URL, defaulting to an empty string if no URL is specified
     */
    String url() default "";


    /**
     * Returns the predefined tag associated with the description.
     * This method provides a standardized way to classify the problem or element
     * being annotated using predefined categories represented by the {@link Tag} enum.
     * Each tag corresponds to a specific data structure or algorithmic concept.
     *
     * @return the predefined tag as an instance of {@link Tag},
     *         defaulting to {@link Tag#NULL} if no tag is specified
     */
    Tag tag() default Tag.NULL;

    /**
     * Returns a custom tag associated with the description.
     * This method allows for the specification of user-defined tags that extend beyond the predefined tag options.
     * The custom tag can be used to provide additional categorization or labeling for the annotated element.
     *
     * @return a string representing the custom tag, defaulting to an empty string if no custom tag is specified
     */
    String customTag() default "";


    /**
     * Returns the difficulty level associated with the description.
     * This method provides a standardized way to classify the complexity
     * of the problem or element being annotated. The difficulty level is
     * represented by the {@link Difficulty} enum, which includes predefined
     * classifications such as SIMPLE, MEDIUM, HARD, and NULL for unspecified cases.
     *
     * @return the difficulty level as an instance of {@link Difficulty},
     *         defaulting to {@link Difficulty#NULL} if no difficulty is specified
     */
    Difficulty diff() default Difficulty.NULL;


    /**
     * Returns an array of predefined types associated with the description.
     * These types represent standard categories that classify the problem or
     * element being annotated. Each type corresponds to a specific algorithmic
     * concept or data structure, as defined in the {@link Type} enumeration.
     * The array may be empty if no predefined types are specified.
     *
     * @return an array of {@link Type} objects representing the predefined types,
     *         defaulting to an empty array if none are provided
     */
    Type[] types() default {};


    /**
     * Returns an array of custom types associated with the description.
     * These custom types represent additional or user-defined categories
     * that extend the predefined types provided by the annotation.
     * The array may be empty if no custom types are specified.
     *
     * @return an array of strings representing the custom types,
     *         defaulting to an empty array if none are provided
     */
    String[] customType() default {};




    /**
     * Returns an array of reference links or views associated with the description.
     * These views typically represent external resources, references, or additional
     * information related to the annotated element. The array may be empty if no
     * views are specified.
     *
     * @return an array of strings representing the reference links or views,
     *         defaulting to an empty array if none are provided
     */
    String[] views() default {};
}
