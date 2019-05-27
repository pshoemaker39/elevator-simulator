package config;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Config {

    private HashMap<String, String> values = new HashMap<>();

    private void setValuesFromFile() {

        FileReader reader;
        try {
            // Create a FileReader object using your filename
            reader = new FileReader("input.json");
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        JSONParser jsonParser = new JSONParser();
        JSONObject jObj;

        try {
            // Create a JSONParser using the FileReader
            jObj = (JSONObject) jsonParser.parse(reader);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return;
        }

    }


    public HashMap<String, String> getValues() {

        setValuesFromFile();

        return this.values;

    }




}
