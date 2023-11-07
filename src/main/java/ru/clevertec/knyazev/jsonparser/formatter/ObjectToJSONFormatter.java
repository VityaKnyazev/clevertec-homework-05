package ru.clevertec.knyazev.jsonparser.formatter;

import ru.clevertec.knyazev.jsonparser.util.ObjectDeterminerUtil;

import javax.naming.OperationNotSupportedException;
import java.lang.reflect.Field;

/**
 * Represents Formatter for formatting simple and composite objects to JSON
 */
public abstract class ObjectToJSONFormatter implements ObjectDeterminerUtil {

    /**
     * Format any object to JSON String
     *
     * Example:
     * Class A {
     * private String b = "1";
     * private int id = 5;
     * }
     * <p>
     * or
     * <p>
     * String b = "1";
     * <p>
     * should return {"b":"1","id":5}, or throws {@link IllegalArgumentException},
     *
     * @param object input object
     * @param <T>    given object type
     * @return JSON result as String
     * @throws IllegalArgumentException if object has primitive type (String, Boolean, Number)
     */
    public abstract <T> String formatObjectToJSON(T object) throws IllegalArgumentException;

    /**
     * Format to JSON simple Object or Object's field type
     * <p>
     * Example:
     * Class A {
     * private String b = "1";
     * }
     * <p>
     * or
     * <p>
     * String b = "1";
     * <p>
     * should return "b":"1", or "1",
     *
     * @param baseObject object that field should be formatted to JSON or object that
     *                   should be formatted to JSON
     * @param baseField  field of base object that should be formatted to JSON
     * @return formatted JSON String
     */
    abstract String formatSimple(Object baseObject, Field... baseField);

    /**
     * Format to JSON array or object's array field
     * <p>
     * Example:
     * <p>
     * class A{
     * private Integer[] arr = {1, 2 ,3};
     * }
     * <p>
     * or
     * <p>
     * private Integer[] arr = {1, 2 ,3}
     * <p>
     * should return "arr":[1,2,3], or [1,2,3],
     *
     * @param baseObject   object which field is array that should be formatted to JSON or
     *                     object array that should be formatted to JSON
     * @param baseArrField array field of base object that should be formatted to JSON
     * @return formatted JSON String
     */
    abstract String formatArray(Object baseObject, Field... baseArrField);

    /**
     * Format to JSON Collection or object's collection field
     * <p>
     * Example:
     * <p>
     * class A{
     * private Collection<Integer> values = List.of(1, 2, 3);
     * }
     * <p>
     * or
     * <p>
     * private Collection<Integer> values = List.of(1, 2, 3);
     * <p>
     * should return "values":[1,2,3], or [1,2,3],
     *
     * @param baseObject          object which field is collection that should be formatted to JSON or
     *                            object collection that should be formatted to JSON
     * @param baseCollectionField collection field of base object that should be formatted to JSON
     * @return formatted JSON String
     */
    abstract String formatCollection(Object baseObject, Field... baseCollectionField);

    /**
     * Format to JSON Map or object's Map field
     * <p>
     * Example:
     * <p>
     * class A{
     * private Map<String, Integer> map = new HashMap<>() {{
     * put("Чай", 1);
     * put("Кофе", 2);
     * }};
     * }
     *
     * <p>
     * or
     * <p>
     * private Map<String, Integer> map = new HashMap<>() {{
     * put("Чай", 1);
     * put("Кофе", 2);
     * }};
     * <p>
     * should return "map":{"Чай":1,"Кофе":2}, or {"Чай":1,"Кофе":2},
     *
     * @param baseObject   object which field is Map that should be formatted to JSON or
     *                     object Map that should be formatted to JSON
     * @param baseMapField Map field of base object that should be formatted to JSON
     * @return formatted JSON String
     */
    abstract String formatMap(Object baseObject, Field... baseMapField);

    /**
     * Formatting given Object to JSON depends on object type or formatting
     * object's field to JSON depends on field type
     *
     * @param parsingObject object for parsing to JSON
     * @return JSON parsing result
     */
    String formatByCase(Object parsingObject, Field... objectField) {
        String result = "";

        Class<?> checkingClass = objectField == null ? parsingObject.getClass()
                                                     : objectField[0].getType();

        if (isComposite(checkingClass)) {
            if (isArray(checkingClass)) {
                result = formatArray(parsingObject, objectField);
            } else if (isCollection(checkingClass)) {
                result = formatCollection(parsingObject, objectField);
            } else if (isMap(checkingClass)) {
                result = formatMap(parsingObject, objectField);
            } else {
                result = formatObjectToJSON(parsingObject);
            }
        } else {
            result = formatSimple(parsingObject, objectField);
        }

        return result;
    }
}
