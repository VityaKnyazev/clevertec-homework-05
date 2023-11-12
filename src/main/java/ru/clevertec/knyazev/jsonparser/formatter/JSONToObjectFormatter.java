package ru.clevertec.knyazev.jsonparser.formatter;

import ru.clevertec.knyazev.jsonparser.exception.JSONParserException;
import ru.clevertec.knyazev.jsonparser.json.JSON;
import ru.clevertec.knyazev.jsonparser.util.JSONDeterminerUtil;
import ru.clevertec.knyazev.jsonparser.util.ObjectDeterminerUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;

public class JSONToObjectFormatter implements JSONDeterminerUtil, ObjectDeterminerUtil {

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public final <T, U> U formatJSONToObject(Class<T> objectClass, JSON jSon, U... parentObject) {

        boolean isValidParentObject = parentObject != null &&
                parentObject.length == 1 &&
                parentObject[0] != null;

        U parentObjectInstance;

        if (isValidParentObject) {
            T childObjectInstance = instantiateObject(objectClass);
            parentObjectInstance = parentObject[0];

            Field[] childFields = childObjectInstance.getClass().getDeclaredFields();

            Arrays.stream(childFields)
                    .peek(childField -> childField.setAccessible(true))
                    .peek(childField -> jSon.setFormattingField(childField.getName()))
                    .forEach(childField -> formatByCase(childObjectInstance, jSon, childField));

            try {

                Field parentObjectField = parentObjectInstance.getClass()
                        .getDeclaredField(jSon.getFormattingField());

                parentObjectField.setAccessible(true);
                parentObjectField.set(parentObjectInstance, childObjectInstance);

            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new JSONParserException(e);
            }
        } else {
            parentObjectInstance = (U) instantiateObject(objectClass);

            Field[] parentFields = parentObjectInstance.getClass().getDeclaredFields();

            Arrays.stream(parentFields)
                    .peek(parentField -> parentField.setAccessible(true))
                    .peek(parentField -> jSon.setFormattingField(parentField.getName()))
                    .forEach(parentField -> formatByCase(parentObjectInstance, jSon, parentField));
        }

        return parentObjectInstance;
    }

    public <T> T formatSimple(T object, Field field, JSON jSon) {

        String fieldValue = getSimpleFieldValueFromJSON(field.getName(), jSon.getFormattingJSON());
        setSimpleObjectField(object, field, fieldValue);

        return object;
    }

    public <T> T formatArray(T object, Field field, JSON jSon) {

        Object array = null;

        try {
            array = field.get(object);
        } catch (IllegalAccessException e) {
            throw new JSONParserException(e);
        }

        //TODO

        Array.set(array, 0, "123");

        try {
            field.set(object, array);
        } catch (IllegalAccessException e) {
            throw new JSONParserException(e);
        }

        return object;
    }

    <T> T formatByCase(T object, JSON jSon, Field... field) {

        boolean isValidObjectField = field != null && field.length == 1;

        Class<?> checkingClass = isValidObjectField
                ? field[0].getType()
                : object.getClass();

        String formattingJSON = isComposite(checkingClass)
                ? getCompositeJSON(jSon.getFormattingField(), jSon.getAllJSON())
                : getSimpleJSON(jSon.getFormattingField(), jSon.getAllJSON());
        jSon.setFormattingJSON(formattingJSON);

        if (isComposite(checkingClass)) {

            if (isArray(checkingClass)) {

            } else {
                object = formatJSONToObject(field[0].getType(), jSon, object);
            }

        } else {
            formatSimple(object, field[0], jSon);
        }

        String allJSON = removeFromJSON(jSon.getAllJSON(), formattingJSON);
        jSon.setAllJSON(allJSON);

        return object;
    }

}
