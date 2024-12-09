package com.example.API_Cinema.utils;

public class MovieAliasUtils {
    public static String generateAlias(String movieName) {
        if (movieName == null || movieName.isEmpty()) {
            return null;
        }
        String nameAlias = movieName.toLowerCase();
        nameAlias.replaceAll("[^a-z0-9\\s]", "");
        nameAlias.replaceAll("\\s+", "-");
        return nameAlias;
    }
}
