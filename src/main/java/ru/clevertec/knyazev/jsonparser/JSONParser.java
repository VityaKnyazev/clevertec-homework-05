package ru.clevertec.knyazev.jsonparser;

/**
 *
 * Represents JSON parser
 *
 */
public interface JSONParser {

    /**
     *
     * Parse object to JSON
     *
     * @param object for parsing
     * @return JSON string
     * @param <T> object type
     */
    <T> String toJSON(T object);

    /**
     *
     * Parse JSON to object
     *
     * @param objectClass object class of T type
     * @param json string for parsing to object
     * @return Object from JSON string
     * @param <T> object type
     */
    <T> T toObject(Class<T> objectClass, String json);

}
