package org.proha.model.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "subject_name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    public Subject() {}
    public Subject(UUID id) {
        this.id = id;
    }

    public Subject(UUID id, String description) {
        this.id = id;
        this.description = description;
    }
    public Subject(String name, String description) {
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
