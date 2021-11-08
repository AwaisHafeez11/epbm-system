package com.app.epbmsystem.model.Forms;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class Hospital implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String city;
    private String address;
    @Column(nullable = false)
    private boolean hospitalType; // True is Government/ false is private hospital

    private String createdDate;
    private String updatedDate;
    private boolean active;

    public Hospital(Long id, String name, String city, String address, boolean hospitalType, String createdDate, String updatedDate, boolean active) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.hospitalType = hospitalType;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Hospital() {

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isHospitalType() {
        return hospitalType;
    }

    public void setHospitalType(boolean hospitalType) {
        this.hospitalType = hospitalType;
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
