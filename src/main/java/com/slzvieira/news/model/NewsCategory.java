package com.slzvieira.news.model;

public enum NewsCategory {

    SPORT,
    POLITICS,
    SCIENCE,
    MUSIC;

    public static NewsCategory fromString(String value) {
        return NewsCategory.valueOf(value.trim().toUpperCase());
    }
}
