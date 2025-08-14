package com.example.productsorting.infrastructure;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StockParserTest {

    @Test
    public void testParseStockString() {
        String stockString = "S: 4 / M:9 / L:0";
        Map<String, Integer> parsedStock = StockParser.parseStockString(stockString);

        assertEquals(3, parsedStock.size());
        assertEquals(4, parsedStock.get("S"));
        assertEquals(9, parsedStock.get("M"));
        assertEquals(0, parsedStock.get("L"));
    }

    @Test
    public void testParseStockStringWithExtraWhitespace() {
        String stockString = " S: 4  /  M: 9 / L: 0 ";
        Map<String, Integer> parsedStock = StockParser.parseStockString(stockString);

        assertEquals(3, parsedStock.size());
        assertEquals(4, parsedStock.get("S"));
        assertEquals(9, parsedStock.get("M"));
        assertEquals(0, parsedStock.get("L"));
    }

    @Test
    public void testParseInvalidStockString() {
        String stockString = "S:4,M:9,L:0";
        assertThrows(IllegalArgumentException.class, () -> {
            StockParser.parseStockString(stockString);
        });
    }

    @Test
    public void testParseEmptyStockString() {
        String stockString = "";
        Map<String, Integer> parsedStock = StockParser.parseStockString(stockString);

        assertTrue(parsedStock.isEmpty());
    }

    @Test
    public void testToStockString() {
        Map<String, Integer> stockMap = new LinkedHashMap<>();
        stockMap.put("S", 4);
        stockMap.put("M", 9);
        stockMap.put("L", 0);

        String stockString = StockParser.toStockString(stockMap);

        assertEquals("S: 4 / M: 9 / L: 0", stockString);
    }
}

class StockParser {
    /**
     * Converts a text string with the format ‘S: X / M: Y / L: Z’ into a map.
     * @param stockString String with the format of stock
     * @return Map with sizes as keys and quantities as values
     */
    public static Map<String, Integer> parseStockString(String stockString) {
        Map<String, Integer> stockMap = new LinkedHashMap<>();

        if (stockString == null || stockString.trim().isEmpty()) {
            return stockMap;
        }

        String[] pairs = stockString.split("/");

        for (String pair : pairs) {
            if (pair.trim().isEmpty()) continue;

            String[] parts = pair.trim().split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid format for  stock: " + stockString);
            }

            String size = parts[0].trim();
            int quantity = Integer.parseInt(parts[1].trim());

            stockMap.put(size, quantity);
        }

        return stockMap;
    }

    /**
     * Converts a stock map to a formatted string
     * @param stockMap Map with sizes and quantities
     * @return Formatted string
     */
    public static String toStockString(Map<String, Integer> stockMap) {
        if (stockMap == null || stockMap.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;

        Map<String, Integer> orderedMap = stockMap;
        if (!(stockMap instanceof LinkedHashMap)) {
            orderedMap = new LinkedHashMap<>();
            for (String size : new String[]{"S", "M", "L"}) {
                if (stockMap.containsKey(size)) {
                    orderedMap.put(size, stockMap.get(size));
                }
            }
            for (Map.Entry<String, Integer> entry : stockMap.entrySet()) {
                if (!orderedMap.containsKey(entry.getKey())) {
                    orderedMap.put(entry.getKey(), entry.getValue());
                }
            }
        }

        for (Map.Entry<String, Integer> entry : orderedMap.entrySet()) {
            if (!first) {
                sb.append(" / ");
            }
            sb.append(entry.getKey()).append(": ").append(entry.getValue());
            first = false;
        }

        return sb.toString();
    }
}