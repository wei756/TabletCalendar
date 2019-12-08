package com.wei756.tabletcalendar;

public class CalendarEvent extends Item {
    private int year, month, date;
    private String type;
    private String name;

    public static final String HOLIDAY = "국경일";

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDate() {
        return date;
    }

    public CalendarEvent setDate(int year, int month, int date) {
        this.year = year;
        this.month = month;
        this.date = date;
        return this;
    }

    public String getType() {
        return type;
    }

    public CalendarEvent setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public CalendarEvent setName(String name) {
        this.name = name;
        return this;
    }
}
