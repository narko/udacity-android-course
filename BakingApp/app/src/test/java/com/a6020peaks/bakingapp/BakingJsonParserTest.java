package com.a6020peaks.bakingapp;

import com.a6020peaks.bakingapp.data.database.RecipeEntry;
import com.a6020peaks.bakingapp.data.network.BakingJsonParser;
import com.a6020peaks.bakingapp.data.network.BakingResponse;

import org.json.JSONException;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by narko on 20/09/17.
 */
public class BakingJsonParserTest {
    @Test
    public void parseTest() throws IOException, JSONException {
        InputStream is = getClass().getResourceAsStream("/baking.json");
        String json = new BufferedReader(new InputStreamReader(is))
                .lines().parallel().collect(Collectors.joining("\n"));
        BakingJsonParser parser = new BakingJsonParser();
        BakingResponse bakingResponse = parser.parse(json);
        assertNotNull(bakingResponse);
        assertTrue(bakingResponse.getRecipes().size() == 4);
        RecipeEntry recipe1 = bakingResponse.getRecipes().get(0);
        assertEquals("Nutella Pie", recipe1.getName());
    }
}
