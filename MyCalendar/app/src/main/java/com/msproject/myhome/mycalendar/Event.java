package com.msproject.myhome.mycalendar;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Date;

public class Event {
    String name;
    DateTime date;

    public Event(String name, DateTime date){
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }
}
