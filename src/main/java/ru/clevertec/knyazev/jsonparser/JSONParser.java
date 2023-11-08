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


}
