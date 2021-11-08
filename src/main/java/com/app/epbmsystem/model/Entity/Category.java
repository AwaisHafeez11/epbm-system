package com.app.epbmsystem.model.Entity;

import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @Column(nullable = false)
    private String name;
    private String createdDate;
    private String updatedDate;
    private boolean active;

    public Category() {

    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Category(Long id, String name, String createdDate, String updatedDate, boolean active) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}
