package com.genecab.bank;

import jakarta.persistence.Entity;

@Entity
public class Account {

    @jakarta.persistence.Id
    private Long id;
    private String name;
    private String type;
    private String owner;

    // JPA requires a no argument constructor
    protected Account() {}

    public Account(Long id, String name, String type, String owner) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}