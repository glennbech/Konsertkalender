package com.glennbech.events.persistence;

import java.util.Date;

/**
 * @author Glenn Bech
 */
public class Reminder {

    private String uid;
    private Date eventDate;
    private int hourToDeliver;
    private int daysInAdvance;


    public Reminder(String uid, Date eventDate, int hourToDeliver, int daysInAdvance) {
        this.uid = uid;
        this.eventDate = eventDate;
        this.hourToDeliver = hourToDeliver;
        this.daysInAdvance = daysInAdvance;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public int getDaysInAdvance() {
        return daysInAdvance;
    }

    public void setDaysInAdvance(int daysInAdvance) {
        this.daysInAdvance = daysInAdvance;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public int getHourToDeliver() {
        return hourToDeliver;
    }

    public void setHourToDeliver(int hourToDeliver) {
        this.hourToDeliver = hourToDeliver;
    }
}
