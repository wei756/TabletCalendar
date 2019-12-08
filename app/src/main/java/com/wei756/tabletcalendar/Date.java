package com.wei756.tabletcalendar;

import java.util.ArrayList;

public class Date extends Item {
    private int date = 0;
    private ArrayList<CalendarEvent> events = new ArrayList<>();
    private boolean today = false;

    public int getDate() {
        return date;
    }

    public Date setDate(int date) {
        this.date = date;
        return this;
    }

    public ArrayList<CalendarEvent> getEvents() {
        return events;
    }

    public Date putEvent(CalendarEvent event) {
        this.events.add(event);
        return this;
    }

    public boolean isToday() {
        return today;
    }

    public Date setToday(boolean today) {
        this.today = today;
        return this;
    }
}
