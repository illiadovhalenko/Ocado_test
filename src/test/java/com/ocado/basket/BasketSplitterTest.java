package com.ocado.basket;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;


public class BasketSplitterTest {
    @Test
    void nullPath() {
//        Exception exception = assertThrows(java.lang.NullPointerException.class, () -> {
//            BasketSplitter basketSplitter = new BasketSplitter(null);
//        });
//        String expectedMessage = "java.lang.NullPointerException";
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
        BasketSplitter basketSplitter = new BasketSplitter(null);
    }

    @Test
    void emptyPath() {
        BasketSplitter basketSplitter = new BasketSplitter("");
    }

    @Test
    void wrongPath() {
        BasketSplitter basketSplitter = new BasketSplitter("/Users/mac/PwJava/Ocado/conffig.json");
    }

    @Test
    void basket1() {
        BasketSplitter basketSplitter = new BasketSplitter("/Users/mac/PwJava/Ocado/config.json");
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        try {
            JSONArray jsonArray = new JSONArray((Files.readString(Path.of("/Users/mac/PwJava/Ocado/basket-1.json"))));
            Gson gson = new Gson();
            List<String> list = gson.fromJson(String.valueOf(jsonArray), listType);
            basketSplitter.split(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void basket2() {
        BasketSplitter basketSplitter = new BasketSplitter("/Users/mac/PwJava/Ocado/config.json");
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        try {
            JSONArray jsonArray = new JSONArray((Files.readString(Path.of("/Users/mac/PwJava/Ocado/basket-2.json"))));
            Gson gson = new Gson();
            List<String> list = gson.fromJson(String.valueOf(jsonArray), listType);
            basketSplitter.split(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // I don't know how to do jar without main 
    public static void main(String[] args){
        BasketSplitter basketSplitter = new BasketSplitter("/Users/mac/PwJava/Ocado/config.json");
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        try {
            JSONArray jsonArray = new JSONArray((Files.readString(Path.of("/Users/mac/PwJava/Ocado/basket-1.json"))));
            Gson gson = new Gson();
            List<String> list = gson.fromJson(String.valueOf(jsonArray), listType);
            Map<String, List<String>> result = basketSplitter.split(list);
//            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
