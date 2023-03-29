package com.sr29_2021.Model;

public class Vax {
    private Integer id;
    private String name;
    private Integer availableNum;
    private Manufacturer manufacturer;

    public Vax(Integer id, String name, Integer availableNum, Manufacturer manufacturer) {
        this.id = id;
        this.name = name;
        this.availableNum = availableNum;
        this.manufacturer = manufacturer;
    }

    public Vax(Integer id, String name, Integer availableNum) {
        this.id = id;
        this.name = name;
        this.availableNum = availableNum;
        this.manufacturer = new Manufacturer();
    }

    public Vax() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAvailableNum() {
        return availableNum;
    }

    public void setAvailableNum(Integer availableNum) {
        this.availableNum = availableNum;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "Vax{" +
                "name='" + name + '\'' +
                ", availableNum=" + availableNum +
                ", manufacturer=" + manufacturer +
                '}';
    }
}
