package ru.clevertec.knyazev.jsonparser.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * Represents JSON String WRAP
 *
 */
@NoArgsConstructor
@Data
public class JSON {

    private String allJSON;

    private String formattingJSON;

    private String formattingField;

    public JSON(String json) {
        this.allJSON = json;
        this.formattingJSON = json;
    }

}
