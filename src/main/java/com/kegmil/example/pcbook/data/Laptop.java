package com.kegmil.example.pcbook.data;

public class Laptop {
    private String id;
    private String brand;

    public Laptop() {
    }

    public Laptop(String id, String brand) {
        this.id = id;
        this.brand = brand;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
