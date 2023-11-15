package ru.clevertec.knyazev.jsonparser;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.clevertec.knyazev.jsonparser.formatter.JSONToObjectFormatter;
import ru.clevertec.knyazev.jsonparser.formatter.ObjectToJSONFormatter;
import ru.clevertec.knyazev.jsonparser.formatter.ObjectToJSONFormatterImpl;
import ru.clevertec.knyazev.jsonparser.util.*;

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONParserImplTest {

    private ObjectToJSONFormatter objectToJSONFormatter;
    private JSONToObjectFormatter jsonToObjectFormatter;
    private JSONParser jsonParserImpl;

    private Gson gson;

    @BeforeEach
    public void setUp() {
        objectToJSONFormatter = new ObjectToJSONFormatterImpl();
        jsonToObjectFormatter = new JSONToObjectFormatter();
        jsonParserImpl = new JSONParserImpl(objectToJSONFormatter, jsonToObjectFormatter);

        gson = new Gson();
    }

    @Test
    public void checkToJsonShouldReturnJSONStringOnSimpleObject() {
        Human human = Human.builder()
                .name("Zafar")
                .family("Khalid")
                .age(29)
                .isGod(false)
                .childrenQuantity(3)
                .build();

        String expectedJson = gson.toJson(human);
        String actualJson = jsonParserImpl.toJSON(human);

        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @Test
    public void checkToJsonShouldReturnJSONStringOnCompositeObject() {
        Family family = Family.builder()
                .name("Standard")
                .ageTogether(15)
                .man(Human.builder()
                        .name("Miko")
                        .family("Veter")
                        .age(45)
                        .isGod(false)
                        .childrenQuantity(3)
                        .build())
                .woman(Human.builder()
                        .name("Margo")
                        .family("Veter")
                        .age(37)
                        .isGod(true)
                        .childrenQuantity(2)
                        .build())
                .build();

        String expectedJson = gson.toJson(family);
        String actualJson = jsonParserImpl.toJSON(family);

        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @Test
    public void checkToJsonShouldReturnJSONStringOnCompositeObjectWithArray() {
        Human[][] humans = {{Human.builder()
                .name("Anna")
                .family("Vera")
                .age(27)
                .isGod(false)
                .childrenQuantity(0)
                .build()}};

        Car car = Car.builder()
                .producers(new String[]{"Alex Antonov", "Andre Bogomazov", "Paul Liney"})
                .isExclusive(false)
                .productionYear(1958)
                .passengers(humans)
                .build();

        String expectedJson = gson.toJson(car);
        String actualJson = jsonParserImpl.toJSON(car);

        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @Test
    public void checkToJsonShouldReturnJSONStringOnCompositeObjectWithCollection() {
        ManPassport manPassport = new ManPassport.Builder()
                .setName("Sergo")
                .setFamily("Alkin")
                .setChildrens(new HashSet<>() {

                    @Serial
                    private static final long serialVersionUID = -7399310972513077651L;

                    {
                        add(Human.builder()
                                .name("Anton")
                                .family("Alkin")
                                .age(0)
                                .childrenQuantity(0)
                                .isGod(false)
                                .build());
                        add(Human.builder()
                                .name("Dasha")
                                .family("Alkin")
                                .childrenQuantity(0)
                                .isGod(false)
                                .age(0)
                                .build());
                    }})
                .setAddresses(new ArrayList<>() {

                    @Serial
                    private static final long serialVersionUID = 7043119155940843813L;

                    {
                        add("Minsk, ul. Plehanova, 5, 125");
                        add("Minsk, ul. Lenina, 12, 18");
                        add("Gomel, ul. Lepeshinskogo, 12, 38");
                    }})
                .setWives(new LinkedList<>() {

                    @Serial
                    private static final long serialVersionUID = -3167322156514520118L;

                    {
                        add(Human.builder()
                                .name("Galka")
                                .family("Alkin")
                                .childrenQuantity(3)
                                .isGod(true)
                                .age(35)
                                .build());
                        add(Human.builder()
                                .name("Valya")
                                .family("Alkin")
                                .childrenQuantity(1)
                                .isGod(false)
                                .age(19)
                                .build());
                    }})
                .build();

        String expectedJson = gson.toJson(manPassport);
        String actualJson = jsonParserImpl.toJSON(manPassport);

        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @Test
    public void checkToJsonShouldReturnJSONStringOnCompositeObjectWithMap() {
        Human[][] passengers = {{Human.builder()
                .name("Manya")
                .family("Galya")
                .age(27)
                .isGod(true)
                .childrenQuantity(0)
                .build()}};

        Dealer dealer = new Dealer.Builder()
                .setName("Alex")
                .setAge(34)
                .setIsTop(true)
                .setSellingHistory(new HashMap<>() {
                    @Serial
                    private static final long serialVersionUID = -4280431847393918672L;

                    {
                        put(1,
                                Car.builder()
                                        .producers(new String[]{"Alex Antonov", "Andre Bogomazov", "Paul Liney"})
                                        .isExclusive(false)
                                        .productionYear(1958)
                                        .passengers(passengers)
                                        .build());
                    }})
                .build();

        String expectedJson = gson.toJson(dealer);
        String actualJson = jsonParserImpl.toJSON(dealer);

        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @Test
    public void checkToJsonShouldReturnEmptyJSONStringWhenGivingNull() {

        String actualJson = jsonParserImpl.toJSON(null);

        assertThat(actualJson).isEqualTo("{}");
    }

    @Test
    public void checkToJsonShouldReturnEmptyJSONStringWhenGivingEmptyFieldsObject() {
        Empty objectWithEmptyFields = new Empty();

        String actualJson = jsonParserImpl.toJSON(objectWithEmptyFields);

        assertThat(actualJson).isEqualTo("{}");
    }


    @Test
    public void checkToObjectShouldReturnObject() {

        Family expectedFamily = Family.builder()
                .name("Standard")
                .ageTogether(15)
                .man(Human.builder()
                        .name("Miko")
                        .family("Veter")
                        .age(45)
                        .isGod(false)
                        .childrenQuantity(3)
                        .build())
                .woman(Human.builder()
                        .name("Margo")
                        .family("Veter")
                        .age(37)
                        .isGod(true)
                        .childrenQuantity(2)
                        .build())
                .build();

        String json = gson.toJson(expectedFamily);

        Family actualFamily = jsonParserImpl.toObject(Family.class, json);

        assertThat(actualFamily).isEqualTo(expectedFamily);

    }

    @Test
    @Disabled
    public void checkToObjectShouldReturnObjectWithArrays() {

        Human[][] passengers = {{Human.builder()
                .name("Manya")
                .family("Galya")
                .age(27)
                .isGod(true)
                .childrenQuantity(0)
                .build()}};

        Car expectedCar = Car.builder()
                .passengers(passengers)
                .isExclusive(true)
                .producers(new String[] {"Antonio Carravatti"})
                .productionYear(1989)
                .build();

        String json = gson.toJson(expectedCar);

        Car actualCar = jsonParserImpl.toObject(Car.class, json);
    }

}
