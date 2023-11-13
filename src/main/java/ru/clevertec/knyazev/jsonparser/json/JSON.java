package ru.clevertec.knyazev.jsonparser.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents JSON String WRAP
 */
@NoArgsConstructor
@Data
public class JSON {

    private String allJSON;

    private String formattingJSON;

    private List<String> formattingFields;

    public JSON(String json) {
        this.allJSON = json;
        this.formattingJSON = json;

        this.formattingFields = new ArrayList<>();
    }

    public boolean setFormattingField(String formattingField) {
        return formattingFields.add(formattingField);
    }

    public String getFormattingField() {

        int formattingSize = formattingFields.size();
        String result = "";

        if (formattingSize > 0) {
            result = formattingFields.get(formattingFields.size() - 1);
            formattingFields.remove(result);
        }

        return result;

    }

}
