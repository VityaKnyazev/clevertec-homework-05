package ru.clevertec.knyazev.jsonparser.formatter;

import ru.clevertec.knyazev.jsonparser.exception.JSONParserException;
import ru.clevertec.knyazev.jsonparser.json.JSON;
import ru.clevertec.knyazev.jsonparser.util.JSONDeterminerUtil;
import ru.clevertec.knyazev.jsonparser.util.ObjectDeterminerUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class JSONToObjectFormatter implements JSONDeterminerUtil, ObjectDeterminerUtil {

    @SuppressWarnings("unchecked")
    public <T, U> U formatJSONToObject(Class<T> objectClass, JSON jSon, U... parentObject) {

        boolean isValidParentObject = parentObject != null &&
                parentObject.length == 1 &&
                parentObject[0] != null;

        U parentObjectInstance;

        if (isValidParentObject) {
            T childObjectInstance = instantiateObject(objectClass);
            parentObjectInstance = parentObject[0];

            formatFields(childObjectInstance, jSon);

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

            formatFields(parentObjectInstance, jSon);
        }

        return parentObjectInstance;
    }

    <T> T formatSimple(T object, Field field, JSON jSon) {

        String fieldValue = getSimpleFieldValueFromJSON(jSon.getFormattingJSON());
        setSimpleObjectField(object, field, fieldValue);

        return object;
    }

    <T> T formatArray(T object, Field field, JSON jSon) {


        if (isComposite(field.getType().getComponentType())) {
            //TODO
            throw new JSONParserException("Error parsing composite array");
        } else {
            List<String> arrayValuesFromJSON = getSimpleArrayValuesFromJSON(jSon.getFormattingJSON());
            setSimpleArrayField(object, field, arrayValuesFromJSON);
        }

        return object;
    }

    @SuppressWarnings("unchecked")
    <T> T formatByCase(T object, JSON jSon, Field... field) {

        boolean isValidObjectField = field != null &&
                field.length == 1 &&
                field[0] != null;

        Class<?> checkingClass = isValidObjectField
                ? field[0].getType()
                : object.getClass();

        String formattingJSON = isComposite(checkingClass)
                ? getCompositeJSON(jSon.getFormattingField(), jSon.getAllJSON())
                : getSimpleJSON(jSon.getFormattingField(), jSon.getAllJSON());
        jSon.setFormattingJSON(formattingJSON);

        if (isComposite(checkingClass)) {

            if (isArray(checkingClass)) {
                object = formatArray(object, field[0], jSon);
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

    private <T> void formatFields(T objectInstance, JSON jSON) {

        Field[] objectFields = objectInstance.getClass().getDeclaredFields();

        Arrays.stream(objectFields)
                .peek(objectField -> objectField.setAccessible(true))
                .peek(objectField -> jSON.setFormattingField(objectField.getName()))
                .peek(objectField -> {
                    if (isComposite(objectField.getType())) {
                        jSON.setFormattingField(objectField.getName());
                    }
                })
                .forEach(objectField -> formatByCase(objectInstance, jSON, objectField));
    }

}
