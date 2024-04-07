package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.*;

public class Main {
    public static void main(String[] args) {

        String configFile = "/home/dominiq/Desktop/Ocado/src/main/resources/config.json";
        BasketSplitter splitter = new BasketSplitter(configFile);

        List<String> basket = loadArrayFromFile("/home/dominiq/Desktop/Ocado/src/main/resources/basket-1.json");
        System.out.println(splitter.split(basket));

    }


    private static ArrayList<String> loadArrayFromFile(String fileName)  {
        ArrayList<String> resultList = new ArrayList<>();

        try(InputStream inputStream = new FileInputStream(fileName)){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            JsonElement jsonElement = JsonParser.parseReader(reader);

            if(jsonElement.isJsonArray()){

                JsonArray jsonArray = jsonElement.getAsJsonArray();

                for (JsonElement element : jsonArray) {
                    resultList.add(element.getAsString());
                }

                return resultList;
//                return new ArrayList<>();

            }

        } catch (IOException exc ){
            exc.printStackTrace();
            return null;
        }

        return null;
    }
}