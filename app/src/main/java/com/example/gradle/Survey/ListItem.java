package com.example.gradle.Survey;

public class ListItem {
    private String user;
    private String statistics;
    private String updated;
    private String expired;

    public ListItem() {
    }

    public ListItem(String user, String statistics, String updated, String expired) {
        this.user = user;
        this.statistics = statistics;
        this.updated = updated;
        this.expired = expired;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStatistics() {
        return statistics;
    }

    public void setStatistics(String statistics) {
        this.statistics = statistics;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }
}
