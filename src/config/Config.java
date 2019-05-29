package config;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Config {

    private static HashMap<String, String> values = new HashMap<>();

    private static void setValuesFromFile() {

        FileReader reader;
        try {
            reader = new FileReader("src/input.json");
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject configObject;

        try {
            configObject = (JSONObject) jsonParser.parse(reader);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return;
        }

        configObject.forEach((key, value) -> {
            values.put(key.toString(), value.toString());
        });

    }


    public static HashMap<String, String> getValues() {
        setValuesFromFile();

        return values;

    }




}
