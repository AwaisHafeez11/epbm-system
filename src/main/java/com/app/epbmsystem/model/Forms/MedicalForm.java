package com.app.epbmsystem.model.Forms;

//import lombok.Data;

import com.app.epbmsystem.model.Entity.Role;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//@Data
@Entity

public class MedicalForm implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @Column(nullable = false)
    private Long fatherCnic;
    @Column(nullable = false)
    private String fatherName;
    @Column(nullable = false)
    private Long fatherContactNumber;
    @Column(nullable = false)
    private String diseaseDescription;
    @Column(nullable = false)
    private String applicationType;
    @Column(nullable = false)
    private String maritalStatus;
    @Column(nullable = false)
    private String description;
    private String applicationStatus;
    private boolean active;

    /**
     * One Medical forms can have multiple diseases, and one diseases can have multiple Medical forms
     */
    @ManyToMany(targetEntity = Disease.class,fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Disease> diseases = new ArrayList<>();

    /**
     * One Medical forms can have multiple diseases, and one diseases can have multiple Medical forms
     */




    public MedicalForm(Long id, Long fatherCnic, String fatherName, Long contactNumber, String diseaseDescription, String applicationType, String maritalStatus, String description, String applicationStatus, boolean active) {
        this.id = id;
        this.fatherCnic = fatherCnic;
        this.fatherName = fatherName;
        this.fatherContactNumber = contactNumber;
        this.diseaseDescription = diseaseDescription;
        this.applicationType = applicationType;
        this.maritalStatus = maritalStatus;
        this.description = description;
        this.applicationStatus = applicationStatus;
        this.active = active;
    }

    public MedicalForm() {

    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFatherCnic() {
        return fatherCnic;
    }

    public void setFatherCnic(Long fatherCnic) {
        this.fatherCnic = fatherCnic;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public Long getContactNumber() {
        return fatherContactNumber;
    }

    public void setContactNumber(Long contactNumber) {
        this.fatherContactNumber = contactNumber;
    }

    public String getDiseaseDescription() {
        return diseaseDescription;
    }

    public void setDiseaseDescription(String diseaseDescription) {
        this.diseaseDescription = diseaseDescription;
    }


    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        description = description;
    }
}
