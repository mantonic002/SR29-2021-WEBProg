package com.sr29_2021.Model;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


public class User {

    private Integer id;

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private String jmbg;

    private String address;

    private String phoneNum;

    private UserRole role;

    private LocalDateTime registrationTime;

    private LocalDate birthDate;


    public User(Integer id,
                String email,
                String firstName,
                String lastName,
                String password,
                String jmbg,
                String address,
                String phoneNum,
                UserRole role,
                LocalDateTime registrationTime,
                LocalDate birthDate
                ) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.jmbg = jmbg;
        this.address = address;
        this.phoneNum = phoneNum;
        this.role = role;
        this.birthDate = birthDate;
        this.registrationTime = registrationTime;
    }

    public User() {
        this.role = UserRole.PATIENT;
        this.registrationTime = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", birthDate=" + birthDate +
                ", jmbg='" + jmbg + '\'' +
                ", address='" + address + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", registrationTime=" + registrationTime +
                ", role=" + role +
                '}';
    }
}
