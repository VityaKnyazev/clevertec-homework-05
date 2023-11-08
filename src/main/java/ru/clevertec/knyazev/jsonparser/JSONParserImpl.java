package ru.clevertec.knyazev.jsonparser;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.clevertec.knyazev.jsonparser.formatter.ObjectToJSONFormatter;

@NoArgsConstructor
@AllArgsConstructor
public class JSONParserImpl implements JSONParser {

    private ObjectToJSONFormatter objectToJSONFormatter;

    @Override
    public <T> String toJSON(T object) {

        String json = objectToJSONFormatter.formatObjectToJSON(object);

        return json.substring(0, json.length() - 1);
    }
}
