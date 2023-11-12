package ru.clevertec.knyazev.jsonparser;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.knyazev.jsonparser.formatter.JSONToObjectFormatter;
import ru.clevertec.knyazev.jsonparser.formatter.ObjectToJSONFormatter;
import ru.clevertec.knyazev.jsonparser.formatter.ObjectToJSONFormatterImpl;
import ru.clevertec.knyazev.jsonparser.util.*;

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
        Human human = new Human.Builder()
                .setName("Zafar")
                .setFamily("Khalid")
                .setAge(29)
                .setIsGod(false)
                .setChildrenQuantity(3)
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
                .man(new Human.Builder()
                        .setName("Miko")
                        .setFamily("Veter")
                        .setAge(45)
                        .setIsGod(false)
                        .setChildrenQuantity(3)
                        .build())
                .woman(new Human.Builder()
                        .setName("Margo")
                        .setFamily("Veter")
                        .setAge(37)
                        .setIsGod(true)
                        .setChildrenQuantity(2)
                        .build())
                .build();

        String expectedJson = gson.toJson(family);
        String actualJson = jsonParserImpl.toJSON(family);

        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @Test
    public void checkToJsonShouldReturnJSONStringOnCompositeObjectWithArray() {
        Human[][] humans = {{new Human.Builder()
                .setName("Anna")
                .setFamily("Vera")
                .setAge(27)
                .setIsGod(false)
                .setChildrenQuantity(0)
                .build()}};

        Car car = new Car.Builder()
                .setProducers(new String[]{"Alex Antonov", "Andre Bogomazov", "Paul Liney"})
                .setIsExclusive(false)
                .setProductionYear(1958)
                .setPassengers(humans)
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

                    private static final long serialVersionUID = -7399310972513077651L;

                    {
                        add(new Human.Builder()
                                .setName("Anton")
                                .setFamily("Alkin")
                                .setAge(0)
                                .setChildrenQuantity(0)
                                .setIsGod(false)
                                .build());
                        add(new Human.Builder()
                                .setName("Dasha")
                                .setFamily("Alkin")
                                .setChildrenQuantity(0)
                                .setIsGod(false)
                                .setAge(0)
                                .build());
                    }})
                .setAddresses(new ArrayList<>() {

                    private static final long serialVersionUID = 7043119155940843813L;

                    {
                        add("Minsk, ul. Plehanova, 5, 125");
                        add("Minsk, ul. Lenina, 12, 18");
                        add("Gomel, ul. Lepeshinskogo, 12, 38");
                    }})
                .setWives(new LinkedList<>() {

                    private static final long serialVersionUID = -3167322156514520118L;

                    {
                        add(new Human.Builder()
                                .setName("Galka")
                                .setFamily("Alkin")
                                .setChildrenQuantity(3)
                                .setIsGod(true)
                                .setAge(35)
                                .build());
                        add(new Human.Builder()
                                .setName("Valya")
                                .setFamily("Alkin")
                                .setChildrenQuantity(1)
                                .setIsGod(false)
                                .setAge(19)
                                .build());
                    }})
                .build();

        String expectedJson = gson.toJson(manPassport);
        String actualJson = jsonParserImpl.toJSON(manPassport);

        assertThat(actualJson).isEqualTo(expectedJson);
    }

    @Test
    public void checkToJsonShouldReturnJSONStringOnCompositeObjectWithMap() {
        Human[][] passengers = {{new Human.Builder()
                .setName("Manya")
                .setFamily("Galya")
                .setAge(27)
                .setIsGod(true)
                .setChildrenQuantity(0)
                .build()}};

        Dealer dealer = new Dealer.Builder()
                .setName("Alex")
                .setAge(34)
                .setIsTop(true)
                .setSellingHistory(new HashMap<>() {
                    private static final long serialVersionUID = -4280431847393918672L;

                    {
                        put(1,
                                new Car.Builder()
                                        .setProducers(new String[]{"Alex Antonov", "Andre Bogomazov", "Paul Liney"})
                                        .setIsExclusive(false)
                                        .setProductionYear(1958)
                                        .setPassengers(passengers)
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
                .man(new Human.Builder()
                        .setName("Miko")
                        .setFamily("Veter")
                        .setAge(45)
                        .setIsGod(false)
                        .setChildrenQuantity(3)
                        .build())
                .woman(new Human.Builder()
                        .setName("Margo")
                        .setFamily("Veter")
                        .setAge(37)
                        .setIsGod(true)
                        .setChildrenQuantity(2)
                        .build())
                .build();

        String json = gson.toJson(expectedFamily);

        Family actualFamily = jsonParserImpl.toObject(Family.class, json);

        assertThat(actualFamily).isEqualTo(expectedFamily);

    }

}
