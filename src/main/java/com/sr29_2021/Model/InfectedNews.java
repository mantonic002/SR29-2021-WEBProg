package com.sr29_2021.Model;

import java.time.LocalDateTime;

public class InfectedNews {
    private Integer id;
    private Integer infected;
    private Integer tested;
    private Integer hospitalized;
    private Integer on_respirator;
    private Integer infectedAllTime;
    private LocalDateTime dateTime;

    public InfectedNews(Integer id,
                        Integer infected,
                        Integer tested,
                        Integer hospitalized,
                        Integer on_respirator,
                        Integer infectedAllTime,
                        LocalDateTime dateTime) {
        this.id = id;
        this.infected = infected;
        this.tested = tested;
        this.hospitalized = hospitalized;
        this.on_respirator = on_respirator;
        this.infectedAllTime = infectedAllTime;
        this.dateTime = dateTime;
    }

    public InfectedNews() {
        this.dateTime = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInfected() {
        return infected;
    }

    public void setInfected(Integer infected) {
        this.infected = infected;
    }

    public Integer getTested() {
        return tested;
    }

    public void setTested(Integer tested) {
        this.tested = tested;
    }

    public Integer getHospitalized() {
        return hospitalized;
    }

    public void setHospitalized(Integer hospitalized) {
        this.hospitalized = hospitalized;
    }

    public Integer getOn_respirator() {
        return on_respirator;
    }

    public void setOn_respirator(Integer on_respirator) {
        this.on_respirator = on_respirator;
    }

    public Integer getInfectedAllTime() {
        return infectedAllTime;
    }

    public void setInfectedAllTime(Integer infectedAllTime) {
        this.infectedAllTime = infectedAllTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "InfectedNews{" +
                "id=" + id +
                ", infected=" + infected +
                ", tested=" + tested +
                ", hospitalized=" + hospitalized +
                ", on_respirator=" + on_respirator +
                ", infectedAllTime=" + infectedAllTime +
                ", dateTime=" + dateTime +
                '}';
    }
}
