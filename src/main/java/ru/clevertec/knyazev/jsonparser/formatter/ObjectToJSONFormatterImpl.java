package ru.clevertec.knyazev.jsonparser.formatter;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.knyazev.jsonparser.exception.JSONParserException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@NoArgsConstructor
public class ObjectToJSONFormatterImpl extends ObjectToJSONFormatter {

    private static final String PRIMITIVE_TYPE_FORMATTING_ERROR = "Primitive type formatting to JSON illegal";
    private static final String FORMATTING_SIMPLE_FIELD_ERROR = "Error when formatting simple field";
    private static final String FORMATTING_ARRAY_ERROR = "Error when formatting array";
    private static final String FORMATTING_COLLECTION_ERROR = "Error when formatting collection";
    private static final String FORMATTING_MAP_ERROR = "Error when formatting map";

    @Override
    public <T> String formatObjectToJSON(T object, String... objectTypeName) {

        String objTypeName = "";

        if (objectTypeName != null && objectTypeName.length == 1) {
            objTypeName = "\"" + objectTypeName[0] +"\":";
        }

        if (object == null) {
            return "{},";
        }

        if (!isComposite(object.getClass())) {
            throw new JSONParserException(PRIMITIVE_TYPE_FORMATTING_ERROR);
        }

        Field[] fields = object.getClass().getDeclaredFields();

        if (fields.length == 0) {
            return "{},";
        }

        return objTypeName + Stream.of(fields)
                .peek(field -> field.setAccessible(true))
                .map(field -> formatByCase(object, field))
                .collect(Collectors.joining("", "{", "}"))
                .replaceFirst(",}$", "},");
    }

    @Override
    String formatSimple(Object baseObject, Field... baseField) {

        String result;

        if (baseObject == null)
            return "";

        if (baseField != null && baseField.length == 1) {

            try {
                Object value = baseField[0].get(baseObject);
                if (value instanceof String) {
                    result = "\"" + baseField[0].getName() + "\"" + ":" + "\"" + value + "\",";
                } else {
                    result = "\"" + baseField[0].getName() + "\"" + ":" + value + ",";
                }
            } catch (IllegalAccessException e) {
                throw new JSONParserException(FORMATTING_SIMPLE_FIELD_ERROR, e);
            }

        } else {
            result = "\"" + baseObject + "\",";
        }

        return result;
    }

    @Override
    String formatArray(Object baseObject, Field... baseArrField) {
        String result;
        Object array;

        if (baseObject == null)
            return "";

        if (baseArrField != null && baseArrField.length == 1) {

            try {
                array = baseArrField[0].get(baseObject);
            } catch (IllegalAccessException e) {
                throw new JSONParserException(FORMATTING_ARRAY_ERROR, e);
            }

            result = "\"" + baseArrField[0].getName() + "\"" + ":[";

        } else {
            array = baseObject;
            result = "[";
        }

        int arrLength = Array.getLength(array);
        if (arrLength == 0) {
            return "";
        }

        for (int i = 0; i < arrLength; i++) {
            Object arrObject = Array.get(array, i);

            result += formatByCase(arrObject);
        }

        result = result.substring(0, result.length() - 1);
        result += "],";

        return result;
    }

    @Override
    String formatCollection(Object baseObject, Field... baseCollectionField) {
        String result;
        Collection<?> collectionObject;

        if (baseObject == null)
            return "";

        if (baseCollectionField != null && baseCollectionField.length == 1) {
            try {
                collectionObject = (Collection<?>) baseCollectionField[0].get(baseObject);
            } catch (IllegalAccessException e) {
                throw new JSONParserException(FORMATTING_COLLECTION_ERROR, e);
            }
            result = "\"" + baseCollectionField[0].getName() + "\"" + ":[";
        } else {
            collectionObject = (Collection<?>) baseObject;
            result = "[";
        }

        if (collectionObject == null || collectionObject.size() == 0)
            return "";

        for (Object innerCollObject : collectionObject) {
            result += formatByCase(innerCollObject);
        }

        result = result.substring(0, result.length() - 1);
        result += "],";

        return result;
    }

    @Override
    String formatMap(Object baseObject, Field... baseMapField) {
        String result;
        Map<?, ?> mapObject;

        if (baseObject == null)
            return "";

        if (baseMapField != null && baseMapField.length == 1) {
            try {
                mapObject = (Map<?, ?>) baseMapField[0].get(baseObject);
            } catch (IllegalAccessException e) {
                throw new JSONParserException(FORMATTING_MAP_ERROR, e);
            }

            result = "\"" + baseMapField[0].getName() + "\"" + ":{";
        } else {
            mapObject = (Map<?, ?>) baseObject;
            result = "{";
        }

        if (mapObject == null || mapObject.isEmpty())
            return "";

        for (Map.Entry<?, ?> kvMap : mapObject.entrySet()) {
            Object keyKvMap = kvMap.getKey();
            Object valueKvMap = kvMap.getValue();

            result += formatByCase(keyKvMap);

            result = result.substring(0, result.length() - 1);
            result += ":";

            result += formatByCase(valueKvMap);

            result = result.substring(0, result.length() - 1);
            result += ",";
        }

        result = result.substring(0, result.length() - 1);
        result += "},";

        return result;
    }
}
