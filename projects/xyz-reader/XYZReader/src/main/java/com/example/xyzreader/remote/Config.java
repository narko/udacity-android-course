package com.example.xyzreader.remote;

import java.net.MalformedURLException;
import java.net.URL;

public class Config {
    public static final URL BASE_URL;
    private static final String TAG = Config.class.toString();
    private static final String URL1 = "https://go.udacity.com/xyz-reader-json";
    private static final String URL2 = "https://raw.githubusercontent.com/TNTest/xyzreader/master/data.json";


    static {
        URL url = null;
        try {
            url = new URL(URL2);
        } catch (MalformedURLException ignored) {
            // throw a real error
            throw new RuntimeException("Error creating URL");
        }

        BASE_URL = url;
    }
}
