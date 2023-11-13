package ru.clevertec.knyazev.jsonparser.util;

import ru.clevertec.knyazev.jsonparser.converter.Converter;
import ru.clevertec.knyazev.jsonparser.exception.JSONParserException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Predicate;

/**
 * Represents reflection methods for determination classes, class fields, instantiation class
 * objects and etc
 *
 */
public interface ObjectDeterminerUtil {

    String OBJECT_INSTANTIATION_ERROR = "Error. Can't instantiate object from class constructor";

    /**
     * Create instance of new T object using its class
     *
     * @param objClass object class
     * @param <T>      object type
     * @return new instance of T object
     * @throws JSONParserException when instantiation error
     */
    default <T> T instantiateObject(Class<T> objClass) throws JSONParserException {
        T objInstance = null;

        if (objClass == null) {
            throw new JSONParserException(OBJECT_INSTANTIATION_ERROR);
        }

        try {
            objInstance = objClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new JSONParserException(OBJECT_INSTANTIATION_ERROR, e);
        }

        return objInstance;
    }

    /**
     *
     * Get simple class field's names
     *
     * @param objClass class
     * @return simple class field's names or empty list
     * @param <T> class type
     */
    default <T> List<String> getSimpleClassFields(Class<T> objClass) {

        Field[] fields = objClass.getDeclaredFields();

        if (fields == null || fields.length == 0) {
            return new ArrayList<>();
        }

        return Arrays.stream(fields)
                .filter(Predicate.not(field -> isComposite(field.getType())))
                .map(field -> field.getName())
                .toList();
    }

    /**
     *
     * Get composite class field's names
     *
     * @param objClass class
     * @return composite class field's names or empty list
     * @param <T> class type
     */
    default <T> List<String> getCompositeClassFields(T objClass) {

        Field[] fields = objClass.getClass().getDeclaredFields();

        if (fields == null || fields.length == 0) {
            return new ArrayList<>();
        }

        return Arrays.stream(fields)
                .filter(field -> isComposite(field.getType()))
                .map(field -> field.getName())
                .toList();
    }

    /**
     *
     * Set simple object field using given value of object field
     *
     * @param object object to set simple field value
     * @param field object field
     * @param fieldValue value to set on field
     * @param <T> object type
     * @throws JSONParserException if field not accessible
     */
    default <T> void setSimpleObjectField(T object, Field field, String fieldValue) {

        field.setAccessible(true);

        try {
            if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                Boolean value = Converter.convertToBoolean(fieldValue);
                if (field.getType() == boolean.class) {
                    value = (value == null) ? false : value;
                }
                field.set(object, value);
            }
            if (field.getType() == byte.class || field.getType() == Byte.class) {
                Byte value = Converter.convertToByte(fieldValue);
                if (field.getType() == byte.class) {
                    value = (value == null) ? 0 : value;
                }
                field.set(object, value);
            }
            if (field.getType() == short.class || field.getType() == Short.class) {
                Short value = Converter.convertToShort(fieldValue);
                if (field.getType() == short.class) {
                    value = (value == null) ? 0 : value;
                }
                field.set(object, value);
            }
            if (field.getType() == int.class || field.getType() == Integer.class) {
                Integer value = Converter.convertToInteger(fieldValue);
                if (field.getType() == int.class) {
                    value = (value == null) ? 0 : value;
                }
                field.set(object, value);
            }
            if (field.getType() == long.class || field.getType() == Long.class) {
                Long value = Converter.convertToLong(fieldValue);
                if (field.getType() == long.class) {
                    value = (value == null) ? 0L : value;
                }
                field.set(object, value);
            }
            if (field.getType() == float.class || field.getType() == Float.class) {
                Float value = Converter.convertToFloat(fieldValue);
                if (field.getType() == float.class) {
                    value = (value == null) ? 0F : value;
                }
                field.set(object, value);
            }
            if (field.getType() == double.class || field.getType() == Double.class) {
                Double value = Converter.convertToDouble(fieldValue);
                if (field.getType() == double.class) {
                    value = (value == null) ? 0d : value;
                }
                field.set(object, value);
            }
            if (field.getType() == char.class || field.getType() == Character.class) {
                Character value = Converter.convertToCharacter(fieldValue);
                if (field.getType() == char.class) {
                    value = (value == null) ? '\u0000' : value;
                }
                field.set(object, value);
            }
            if (field.getType() == String.class) {
                fieldValue = fieldValue.substring(1, fieldValue.length() - 1);

                field.set(object, fieldValue);
            }

        } catch (IllegalAccessException e) {
            throw new JSONParserException(e);
        }
    }


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
