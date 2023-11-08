package ru.clevertec.knyazev.jsonparser.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * Represents methods for determination parsing params of object's class
 */
public interface ObjectDeterminerUtil {

    /**
     *
     * Check if class has not primitive type, not extends Number, Boolean and String.
     *
     * @param clazz input class
     * @return true if class represents object that has not primitive type and
     *         not extends Number and String, otherwise - return false
     * @param <T> type of input class
     */
    default <T> boolean isComposite(Class<T> clazz) {

        return clazz != null &&
               !(clazz.isPrimitive() ||
               Number.class.isAssignableFrom(clazz) ||
               String.class.isAssignableFrom(clazz) ||
               Boolean.class.isAssignableFrom(clazz));

    }

    /**
     *
     * Check if input class is array
     *
     * @param clazz input class for checking
     * @return true if class is array, oterwise - false.
     * @param <T> input class type
     */
    default <T> boolean isArray(Class<T> clazz) {

        return clazz != null && clazz.isArray();

    }

    /**
     *
     * Check if input class is Collection
     *
     * @param clazz input class for checking
     * @return true if class is Collection, oterwise - false.
     * @param <T> input class type
     */
    default <T> boolean isCollection(Class<T> clazz) {

        return clazz != null && Collection.class.isAssignableFrom(clazz);

    }

    /**
     *
     * Check if input class is Map
     *
     * @param clazz input class for checking
     * @return true if class is Map, oterwise - false.
     * @param <T> input class type
     */
    default <T> boolean isMap(Class<T> clazz) {

        return clazz != null && Map.class.isAssignableFrom(clazz);

    }
}
