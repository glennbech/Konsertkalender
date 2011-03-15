package com.glennbech.events.parser;

import java.io.Serializable;
import java.util.Date;


public class VEvent implements Serializable {

    private long id = -1;
    private Date startDate;
    private Date endDate;
    private String summary;
    private String location;
    private String url;
    private String uid;
    private boolean brandSpankingNew;
    private boolean isFavorite;

    public VEvent(long id, Date startDate, Date endDate, String summary, String location, String url, String uid) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.summary = summary;
        this.location = location;
        this.url = url;
        this.uid = uid;
    }

    public boolean isBrandSpankingNew() {
        return brandSpankingNew;
    }


    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setBrandSpankingNew(boolean brandSpankingNew) {
        this.brandSpankingNew = brandSpankingNew;
    }

    public VEvent() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VEvent vEvent = (VEvent) o;

        if (uid != null ? !uid.equals(vEvent.uid) : vEvent.uid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return uid != null ? uid.hashCode() : 0;
    }


    @Override
    public String toString() {
        return "VEvent{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", summary='" + summary + '\'' +
                ", location='" + location + '\'' +
                ", url='" + url + '\'' +
                ", uid='" + uid + '\'' +
                ", brandSpankingNew=" + brandSpankingNew +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
