package com.example.myrestservice.model;
import java.time.LocalDate;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;

public class Datetime {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_TIME;

    private String timeValue; // Stored as a String
    private String dateValue; // Stored as a String

    // Getters
    public OffsetTime getTimeValue() {
        return timeValue != null ? OffsetTime.parse(timeValue, TIME_FORMATTER) : null;
    }

    public LocalDate getDateValue() {
        return dateValue != null ? LocalDate.parse(dateValue, DATE_FORMATTER) : null;
    }

    // Setters
    public void setTimeValue(OffsetTime time) {
        this.timeValue = time != null ? time.format(TIME_FORMATTER) : null;
    }

    public void setDateValue(LocalDate date) {
        this.dateValue = date != null ? date.format(DATE_FORMATTER) : null;
    }
}
