package org.example;

import com.google.gson.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.example.MinSetCoverILP.solveSetCoverILP;

/**
 * The BasketSplitter class is responsible for splitting a list
 * of items into delivery sets.
 */
public class BasketSplitter {
    private final ClassLoader classLoader = BasketSplitter.class.getClassLoader();
    private final HashMap<String, List<String>> products;

    /**
     * Constructs a BasketSplitter instance with the given configuration file path.
     * Configuration file contain json data necessary to split items into sets.
     * JSON format:
     * {
     *     "Product_01": ["Shipment_1", "Shipment_02"]
     * }
     *
     * @param absolutePathToConfigFile The absolute path to the configuration file.
     */
    public BasketSplitter(String absolutePathToConfigFile) {
        products = loadMapFromFile(absolutePathToConfigFile);
    }


    /**
     * Splits the given basket of items into delivery sets based on the configured options.
     *
     * @param items The list of items to be split into delivery sets.
     * @return A map of delivery sets containing items.
     */
    public Map<String, List<String>> split(List<String> items ){
        if(items == null || products == null) {
            return new HashMap<>();
        }

        List<String> validItems = new ArrayList<>();

        // Sieve invalid products.
        // In such situation we just ignore invalid products, since it is not our problem to provide item we do not have
        for(String item : items)
            if(products.containsKey(item))
                validItems.add(item);

        items = validItems;

        if(items.isEmpty())
            return new HashMap<>();

        // initialize deliverySet to contain only products' deliveries
        Set<String> deliverySet = items.stream()
                .map(products::get)
                .collect(HashSet::new, Set::addAll, Set::addAll);

        List<String> deliveryMethods = new ArrayList<>(deliverySet);

        // Generate basic utility for SetCover. MinSetCover will contain all
        // necessary info about deliveries option, and products they can deliver.
        List<Set<Integer>> minSetCover = Stream.generate(HashSet<Integer>::new)
                .limit(deliverySet.size())
                .collect(Collectors.toList());

        // Populate minSetCover
        for(int i = 0; i < items.size(); i++)
            for(String delivery : products.get(items.get(i)))
                minSetCover.get(deliveryMethods.indexOf(delivery)).add(i);

        List<Integer> minCover = solveSetCoverILP(minSetCover, IntStream.range(0, items.size()).boxed().collect(Collectors.toSet()));

        // Now when we have optimized deliveries options we can get delivery with most products
        Comparator<Integer> byNumberOfElements = Comparator.comparingInt(i -> minSetCover.get(i).size());
        minCover.sort(byNumberOfElements);

        List<Integer> minCoverReversed = new ArrayList<>();

        for(int i = minCover.size() -1 ; i >=0 ; i--)
            minCoverReversed.add(minCover.get(i));

        // Simplest way to avoid duplicates && add new layer of security is to mark products
        // already taken into account.
        List<Integer> usedProducts = new ArrayList<>(Collections.nCopies(items.size(), 0));
        HashMap<String, List<String >> result = new HashMap<>();

        // Map Creating
        for(Integer i : minCoverReversed) {
            String deliveryMethod = deliveryMethods.get(i);
            List<String> finalProducts = new ArrayList<>();

            for (Integer j : minSetCover.get(i)){
                if(usedProducts.get(j) == 0){
                    finalProducts.add(items.get(j));
                    usedProducts.set(j, 1);
                }
            }

            if(!finalProducts.isEmpty())
                result.put(deliveryMethod, finalProducts);

        }

        return result;
    }


    private HashMap<String, List<String>> loadMapFromFile(String fileName) {
        HashMap<String, List<String>> resultMap = new HashMap<>();

        try (InputStream inputStream =  new FileInputStream(fileName)) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            JsonElement jsonElement = JsonParser.parseReader(reader);

            if(!jsonElement.isJsonObject()){
                throw new JsonSyntaxException("Invalid json format");
            }

            JsonObject jsonObject = jsonElement.getAsJsonObject();

            for (String key : jsonObject.keySet()) {
                JsonElement jsonElements = jsonObject.get(key);

                if(!jsonElements.isJsonArray()) {
                    throw new JsonSyntaxException("Invalid json format");
                }

                JsonArray jsonArray = jsonObject.getAsJsonArray(key);
                resultMap.put(key, jsonArray.getAsJsonArray().asList().stream().map(JsonElement::getAsString).toList());
            }

            return resultMap;
        } catch (IOException | JsonSyntaxException e){
            // Return empty map if some error may occur since
            // structure in task does not provide place for throwing error
            return new HashMap<>();
        }
    }

}
