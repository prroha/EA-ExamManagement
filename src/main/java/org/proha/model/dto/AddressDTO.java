package org.proha.model.dto;

public class AddressDTO {

    private String city;
    private String state;

    // Constructors, getters, and setters

    public AddressDTO() {}

    public AddressDTO(String city, String state) {
        this.city = city;
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
