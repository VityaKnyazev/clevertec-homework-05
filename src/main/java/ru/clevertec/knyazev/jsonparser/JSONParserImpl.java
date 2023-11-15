package ru.clevertec.knyazev.jsonparser;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.clevertec.knyazev.jsonparser.formatter.JSONToObjectFormatter;
import ru.clevertec.knyazev.jsonparser.formatter.ObjectToJSONFormatter;
import ru.clevertec.knyazev.jsonparser.json.JSON;

@NoArgsConstructor
@AllArgsConstructor
public class JSONParserImpl implements JSONParser {

    private ObjectToJSONFormatter objectToJSONFormatter;
    private JSONToObjectFormatter jsonToObjectFormatter;

    @Override
    public <T> String toJSON(T object) {

        String json = objectToJSONFormatter.formatObjectToJSON(object);

        return json.substring(0, json.length() - 1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T toObject(Class<T> objectClass, String json) {

        JSON jSon = new JSON(json);

        return jsonToObjectFormatter.formatJSONToObject(objectClass, jSon);
    }
}
