package com.app.epbmsystem.model.Entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Permission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @Column(nullable = false)
    private String name;
    private String createdDate;
    private String updatedDate;
    private boolean active;

    public Permission() {

    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Permission(Long id, String name, String createdDate, String updatedDate, boolean active) {
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

    /**
     * One Role can have multiple Permissions, and one permission can have multiple roles
     */
    @ManyToMany(targetEntity = Permission.class,fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Permission> permissions = new ArrayList<>();
}
