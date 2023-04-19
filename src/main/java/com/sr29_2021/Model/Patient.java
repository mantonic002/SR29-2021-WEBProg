package com.sr29_2021.Model;

import java.time.LocalDateTime;

public class Patient {
    private Integer userId;
    private Boolean vaxxed;
    private Integer receivedDoses;
    private LocalDateTime lastDoseDate;
    private User user;

    public Patient(Integer userId, Boolean vaxxed, Integer receivedDoses, LocalDateTime lastDoseDate, User user) {
        this.userId = userId;
        this.vaxxed = vaxxed;
        this.receivedDoses = receivedDoses;
        this.lastDoseDate = lastDoseDate;
        this.user = user;
    }

    public Patient() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getVaxxed() {
        return vaxxed;
    }

    public void setVaxxed(Boolean vaxxed) {
        this.vaxxed = vaxxed;
    }

    public Integer getReceivedDoses() {
        return receivedDoses;
    }

    public void setReceivedDoses(Integer receivedDoses) {
        this.receivedDoses = receivedDoses;
    }

    public LocalDateTime getLastDoseDate() {
        return lastDoseDate;
    }

    public void setLastDoseDate(LocalDateTime lastDoseDate) {
        this.lastDoseDate = lastDoseDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "userId=" + userId +
                ", vaxxed=" + vaxxed +
                ", recievedDoses=" + receivedDoses +
                ", lastDoseDate=" + lastDoseDate +
                ", user=" + user +
                '}';
    }
}
