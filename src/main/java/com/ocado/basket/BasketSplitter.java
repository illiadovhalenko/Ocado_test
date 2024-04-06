package com.ocado.basket;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.google.gson.reflect.TypeToken;
import org.json.*;
import com.google.gson.*;
public class BasketSplitter {
    JSONObject jsonObject;

    /**
     * @param absolutePathToConfigFile Constructor, create JSONObject from file, catch exceptions when path is null or wrong
     */
    public BasketSplitter(String absolutePathToConfigFile) {
        try {
            jsonObject = new JSONObject(Files.readString(Path.of(absolutePathToConfigFile)));
        } catch (NullPointerException | IOException e) {
            System.out.println(e);
        }
    }

    /**
     * @param sortedEntries -- sorted entries from map
     *                      delete values(from maximal) in others lists,
     */
    private void deleteRepetitiveItems(List<Map.Entry<String, List<String>>> sortedEntries) {
        for (int i = sortedEntries.size() - 1; i > 0; i--) {
            List<String> maxList = sortedEntries.get(i).getValue();
            sortedEntries.stream().limit(i).forEach(entry -> entry.getValue().removeAll(maxList));
        }
    }

    /**
     * @param entries -- without repetitive values in lists
     *                remove empty lists
     */
    private void removeEmptyDeliveries(List<Map.Entry<String, List<String>>> entries) {
        entries.removeIf(entry -> entry.getValue().isEmpty());
    }

    /**
     * @param items -- our basket
     * @return Map< TypeOfDelivery, Lis t <ItemsThatCanBeDeliveredThisWay>>
     */
    private Map<String, List<String>> segregationItemsByDelivery(List<String> items ){
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>()
        {}.getType();
        Map<String, List<String>> typesOfDelivery = new LinkedHashMap<>();
        for (String item : items) {
            List<String> list = gson.fromJson(String.valueOf(jsonObject.getJSONArray(item)), listType);
            for (String typeOfDeliveryOneItem : list) {
                if (!typesOfDelivery.containsKey(typeOfDeliveryOneItem)) {
                    typesOfDelivery.put(typeOfDeliveryOneItem, new ArrayList<>());
                }
                typesOfDelivery.get(typeOfDeliveryOneItem).add(item);
            }
        }
        return typesOfDelivery;
    }

    /**
     * sort typesOfDelivery using number of items
     *
     * @param entries
     */
    private void sortEntries(List<Map.Entry<String, List<String>>> entries) {
        entries.sort(Comparator.comparingInt(entry -> entry.getValue().size()));
    }

    /**
     * Firstly segregate items by type of delivery, than sort using number of items, delete repetitive items,
     * remove empty deliveries, and create result map
     *
     * @param items -- our basket
     * @return
     */
    public Map<String, List<String>> split(List<String> items) {
        if (items == null || items.isEmpty())
            return new HashMap<>();
        Map<String, List<String>> typesOfDelivery = segregationItemsByDelivery(items);
        List<Map.Entry<String, List<String>>> entries = new ArrayList<>(typesOfDelivery.entrySet());
        sortEntries(entries);
        deleteRepetitiveItems(entries);
        removeEmptyDeliveries(entries);
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : entries) {
            result.put(entry.getKey(), entry.getValue());
        }
//        System.out.println(result);
        return result;
    }
}