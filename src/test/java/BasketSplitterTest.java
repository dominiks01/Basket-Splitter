import org.example.BasketSplitter;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BasketSplitterTest {
    private final String configFile = "config.json";

    @Test
    void givenValidConfigValidItemsReturnValidMap(){
        URL resourcUrl = getClass().getClassLoader().getResource(configFile);
        assert resourcUrl != null;

        ArrayList<String> items = new ArrayList<>(
                List.of("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White",
                        "Flower - Daisies", "Fond - Chocolate", "Cookies - Englishbay Wht"));

        BasketSplitter _sut = new BasketSplitter(resourcUrl.getPath());
        Map<String, List<String>> result = _sut.split(items);

        Map<String, List<String>> validOutput = new HashMap<>();
        validOutput.put("Mailbox delivery", List.of("Fond - Chocolate"));
        validOutput.put("Courier", Arrays.asList("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White", "Flower - Daisies", "Cookies - Englishbay Wht"));

        assertEquals(result, validOutput);
    }

    @Test
    void givenValidConfigInvalidItemsReturnEmptyMap(){
        URL resourcUrl = getClass().getClassLoader().getResource(configFile);
        assert resourcUrl != null;

        ArrayList<String> items = new ArrayList<>(
                List.of("Steak (300g)", "Carrot (1kg)", "Cold Beer(330ml)", "AA Battery (4 Pcs.)","Espresso Machine", "Garden Chair"));

        BasketSplitter _sut = new BasketSplitter(resourcUrl.getPath());
        Map<String, List<String>> result = _sut.split(items);

        assertTrue(result.isEmpty());
    }

    @Test
    void giverInvalidConfigItemsReturnEmptyMap(){
        URL resourcUrl = getClass().getClassLoader().getResource(configFile);
        assert resourcUrl != null;

        ArrayList<String> items = new ArrayList<>(
                List.of("Cocoa Invalid Butter", "Tart - Pecan"));

        BasketSplitter _sut = new BasketSplitter(resourcUrl.getPath() + "InvalidSuffix");
        Map<String, List<String>> result = _sut.split(items);

        assertTrue(result.isEmpty());
    }

}
