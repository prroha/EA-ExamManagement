package org.proha.model.dto;

import java.util.UUID;

public class StudentDTO {

    private UUID id;
    private String name;
    private String email;
    private String phone;
    private AddressDTO address;

    // Constructors, getters, and setters

    public StudentDTO() {}

    public StudentDTO(UUID id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    public StudentDTO(UUID id, String name, String email, String phone, AddressDTO address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }
}
