package ru.clevertec.knyazev.jsonparser.util;

import ru.clevertec.knyazev.jsonparser.converter.Converter;
import ru.clevertec.knyazev.jsonparser.exception.JSONParserException;

import java.lang.reflect.Array;
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
        T objInstance;

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
     * Create instance of new T array using its class
     *
     * @param arrayClass array class
     * @param <T> object type
     * @return new instance of T array
     * @throws JSONParserException when instantiation error
     */
    @SuppressWarnings("unchecked")
    default <T> T instantiateArray(Class<T> arrayClass, int arrayLength) throws JSONParserException {


        T arrayInstance;

        if (arrayClass == null || arrayLength < 1) {
            throw new JSONParserException(OBJECT_INSTANTIATION_ERROR);
        }

        try {
            arrayInstance = (T) Array.newInstance(arrayClass, arrayLength);
        } catch (IllegalArgumentException e) {
            throw new JSONParserException(OBJECT_INSTANTIATION_ERROR, e);
        }

        return arrayInstance;
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
     * Set simple object field using given field value
     *
     * @param object object to set simple field value
     * @param field object field
     * @param fieldValue value to set on field
     * @param <T> object type
     * @throws JSONParserException if field not accessible
     */
    default <T, U> void setSimpleObjectField(T object, Field field, String fieldValue) throws JSONParserException {

        field.setAccessible(true);

        try {
            U determinedFieldTypeValue = determineSimpleFieldTypeValue(field.getType(), fieldValue);
            field.set(object, determinedFieldTypeValue);

        } catch (IllegalAccessException e) {
            throw new JSONParserException(e);
        }
    }

    /**
     *
     * Set simple array field values to object
     *
     * @param object object to set simple array field value
     * @param field array field
     * @param fieldValues values to set on array field
     * @param <T> object type
     * @param <U> array type
     * @throws JSONParserException if field not accessible
     */
    @SuppressWarnings("unchecked")
    default <T, U> void setSimpleArrayField(T object, Field field, List<String> fieldValues) {

        field.setAccessible(true);

        U array = (U) instantiateArray(field.getType().getComponentType(), fieldValues.size());

        for (int i = 0; i < fieldValues.size(); i++) {
            Array.set(array, i, determineSimpleFieldTypeValue(field.getType().getComponentType(), fieldValues.get(i)));
        }

        try {
            field.set(object, array);
        } catch (IllegalAccessException e) {
            throw new JSONParserException(e);
        }

    }

    @SuppressWarnings("unchecked")
    private <T, U> U determineSimpleFieldTypeValue(Class<T> fieldClass, String fieldValue) {

        U value = null;

        if (fieldClass.isAssignableFrom(boolean.class) || fieldClass.isAssignableFrom(Boolean.class)) {
            value = (U) Converter.convertToBoolean(fieldValue);
        }
        if (fieldClass.isAssignableFrom(byte.class) || fieldClass.isAssignableFrom(Byte.class)) {
            value = (U) Converter.convertToByte(fieldValue);
            if (fieldClass.isAssignableFrom(byte.class)) {
                value = (value == null) ? (U) Byte.valueOf("0") : value;
            }
        }
        if (fieldClass.isAssignableFrom(short.class) || fieldClass.isAssignableFrom( Short.class)) {
            value = (U) Converter.convertToShort(fieldValue);
            if (fieldClass.isAssignableFrom(short.class)) {
                value = (value == null) ? (U) Short.valueOf("0") : value;
            }
        }
        if (fieldClass.isAssignableFrom(int.class) || fieldClass.isAssignableFrom(Integer.class)) {
            value = (U) Converter.convertToInteger(fieldValue);
            if (fieldClass.isAssignableFrom(int.class)) {
                value = (value == null) ? (U) Integer.valueOf("0") : value;
            }
        }
        if (fieldClass.isAssignableFrom(long.class) || fieldClass.isAssignableFrom(Long.class)) {
            value = (U) Converter.convertToLong(fieldValue);
            if (fieldClass.isAssignableFrom(long.class)) {
                value = (value == null) ? (U) Long.valueOf("0L") : value;
            }
        }
        if (fieldClass.isAssignableFrom(float.class) || fieldClass.isAssignableFrom(Float.class)) {
            value = (U) Converter.convertToFloat(fieldValue);
            if (fieldClass.isAssignableFrom(float.class)) {
                value = (value == null) ? (U) Float.valueOf("0F") : value;
            }
        }
        if (fieldClass.isAssignableFrom(double.class) || fieldClass.isAssignableFrom(Double.class)) {
            value = (U) Converter.convertToDouble(fieldValue);
            if (fieldClass.isAssignableFrom(double.class)) {
                value = (value == null) ? (U) Double.valueOf("0d") : value;
            }
        }
        if (fieldClass.isAssignableFrom( char.class) || fieldClass.isAssignableFrom(Character.class)) {
            value = (U) Converter.convertToCharacter(fieldValue);
            if (fieldClass.isAssignableFrom(char.class)) {
                value = (value == null) ? (U) Character.valueOf('\u0000') : value;
            }
        }
        if (fieldClass.isAssignableFrom(String.class)) {
            value = (U) fieldValue.substring(1, fieldValue.length() - 1);
        }

        return value;
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
