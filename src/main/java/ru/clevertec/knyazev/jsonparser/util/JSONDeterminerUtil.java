package ru.clevertec.knyazev.jsonparser.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface JSONDeterminerUtil {

    /**
     *
     * Get simple field value from JSON
     * Example: id:12, should return 12
     *
     * @param simpleJson simple json
     * @return key from key:value json pair
     */
    default String getSimpleFieldValueFromJSON(String simpleJson) {

        if (simpleJson == null || simpleJson.isEmpty()) {
            return "";
        }

        return simpleJson.split(":")[1]
                .replace(",", "");
    }

    /**
     *
     * Get simple array values from json
     * Example abv:[1,2,3,4] should return list of 1,2,3,4
     *
     * @param jSON input array json
     * @return simple values of array json
     */
    default List<String> getSimpleArrayValuesFromJSON(String jSON) {

        if (jSON == null || jSON.isEmpty()) {
            return Collections.emptyList();
        }

        String jsonValue = jSON.replaceFirst("\"\\w*\"\\s*:\\s*\\[", "")
                .replaceFirst("\\],?", "");

        return Arrays.stream(jsonValue.split(","))
                .toList();
    }

    /**
     * Get simple JSON by field name
     *
     * @param fieldName key name in JSON string
     * @param jSON      string of JSON
     * @return JSON simple "key":"value", pair or empty string
     */
    default String getSimpleJSON(String fieldName, String jSON) {

        String fieldPattern = "(\\s*\"" + fieldName + "\"\\s*?:\\s*?[\\w\"]+,?)";

        Matcher fieldMatcher = Pattern.compile(fieldPattern).matcher(jSON);

        if (fieldMatcher.find()) {
            return fieldMatcher.group();
        }

        return "";
    }

    /**
     * Removing composite or simple field from JSON and return the rest JSON
     *
     * @param jSON         input JSON String
     * @param removingJSON json String for removing
     * @return simple JSON without given removing json string
     */
    default String removeFromJSON(String jSON, String removingJSON) {

        String restJSON = jSON;

        if (restJSON.contains(removingJSON)) {
            int endIndex = restJSON.indexOf(removingJSON);
            int beginIndex = endIndex + removingJSON.length();

            restJSON = restJSON.substring(0, endIndex)
                    + restJSON.substring(beginIndex);
        }


        restJSON = restJSON.replaceFirst(",}$", "}");

        return restJSON;
    }

    /**
     * Removing composite fields from JSON and returns only simple JSON fields :
     * values
     *
     * @param jSON            input String
     * @param compositeFields Map with fieldName and composite fields JSON value
     * @return JSON without composite fields
     */
    default String removeCompositeFieldsFromJSON(String jSON, Map<String, String> compositeFields) {
        String simpleFieldsJSON = jSON;

        for (String compositeField : compositeFields.values()) {
            if (simpleFieldsJSON.contains(compositeField)) {
                int endIndex = simpleFieldsJSON.indexOf(compositeField);
                int beginIndex = endIndex + compositeField.length();

                simpleFieldsJSON = simpleFieldsJSON.substring(0, endIndex)
                        + simpleFieldsJSON.substring(beginIndex);
            }
        }

        simpleFieldsJSON = simpleFieldsJSON.replaceFirst(",}$", "}");

        return simpleFieldsJSON;
    }

    /**
     * Check if jSON String contains only simple data like {"data": "1", "some" : 2} or ["data": "1", "some" : 2]
     * or {"data": "1", "some" : 2}, or [2, "5", "ara"]
     *
     * @param jSON String
     * @return boolean
     */
    default boolean hasJSONSimpleData(String jSON) {

        return Pattern.compile("^[\\[\\{](\\s*?\"\\w+?\"\\s*?:\\s*?[\\w\"]+,?)+\\s*?[\\]\\}],?$")
                .matcher(jSON).matches() ||
                Pattern.compile("^\\[(\\\"?\\w*\\\"?,?\\s?)+\\],?$")
                        .matcher(jSON).matches();
    }

    /**
     * Get JSON String for gaven composite field name like "compositeFieldName":{[
     * complex value ]}
     *
     * @param fieldName String
     * @param jSON      String
     * @return JSON String for giving composite field name or empty String
     */
    default String getCompositeJSON(String fieldName, String jSON) {

        Integer startIndex;
        Integer endIndex;

        startIndex = findStartIndex(fieldName, jSON);

        if (startIndex == null) {
            return "";
        }

        endIndex = findEndIndex(jSON.substring(startIndex).strip());

        if (endIndex == null) {
            return "";
        }

        endIndex = startIndex + endIndex + 1;

        return jSON.substring(startIndex, endIndex);
    }

    /**
     * Searching start index in composite JSON String for method getCompositeJSON(String fieldName, String jSON)
     *
     * @param fieldName field name
     * @param jSON      String
     * @return Integer end index
     */
    private Integer findStartIndex(String fieldName, String jSON) {
        Integer startIndex = null;
        String compositeFieldPattern = "\\s{0,}" + "\"" + fieldName + "\"" + "\\s{0,}:\\s{0,}[\\[\\{]";

        Matcher fieldMathcer = Pattern.compile(compositeFieldPattern).matcher(jSON);

        if (fieldMathcer.find())
            startIndex = fieldMathcer.start();

        return startIndex;
    }

    /**
     * Searching end index in composite JSON String for method getCompositeJSON(String fieldName, String jSON)
     *
     * @param jsonCompositeObject JSON String that starts with "key" : { or [
     * @return Integer end index
     */
    private Integer findEndIndex(String jsonCompositeObject) {
        Integer endIndex = null;

        Matcher beginJsonMatcher = Pattern.compile("^\"\\w+?\"\\s*?:\\s*?[\\[\\{]").matcher(jsonCompositeObject);

        Integer bracketBeginIndex = null;
        if (beginJsonMatcher.find()) {
            bracketBeginIndex = beginJsonMatcher.end();
        }

        if (bracketBeginIndex == null) {
            return endIndex;
        }

        char bracketChar = jsonCompositeObject.charAt(bracketBeginIndex - 1);

        int counter = 1;

        for (int i = bracketBeginIndex; i < jsonCompositeObject.length(); i++) {
            char charCompositeJsonObject = jsonCompositeObject.charAt(i);

            if (charCompositeJsonObject == bracketChar) {
                counter++;
            }

            if (charCompositeJsonObject == bracketChar + 2) {
                counter--;
            }

            if (counter == 0) {
                if (i == jsonCompositeObject.length()) {
                    endIndex = i;
                }

                if (jsonCompositeObject.charAt(i + 1) == ',') {
                    endIndex = i + 1;
                } else {
                    endIndex = i;
                }
                break;
            }
        }

        return endIndex;
    }
}
