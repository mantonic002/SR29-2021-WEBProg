package com.sr29_2021.Model;

import java.time.LocalDateTime;

public class Application {
    private Integer id;
    private LocalDateTime dateTime;
    private Integer patientId;
    private Patient patient;
    private Integer vaxId;
    private Vax vax;

    public Application(Integer id, LocalDateTime dateTime, Patient patient, Vax vax) {
        this.id = id;
        this.dateTime = dateTime;
        this.patient = patient;
        this.vax = vax;
    }

    public Application() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        this.patientId = patient.getUserId();
    }

    public Integer getVaxId() {
        return vaxId;
    }

    public void setVaxId(Integer vaxId) {
        this.vaxId = vaxId;
    }

    public Vax getVax() {
        return vax;
    }

    public void setVax(Vax vax) {
        this.vax = vax;
        this.vaxId = vax.getId();

    }
}
