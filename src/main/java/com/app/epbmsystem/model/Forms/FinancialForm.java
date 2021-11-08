package com.app.epbmsystem.model.Forms;

import com.app.epbmsystem.model.Entity.User;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
public class FinancialForm implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private Long cnic;
    @Column(nullable = false)
    private long contact;

    private String fatherName;
    private Long fatherCnic;
    private Long fatherContactNumber;
    private Long fatherOccupation;

    private Integer applicantAge;
    private String currentAddress;

    private String permanentAddress;
    private boolean maritalStatus;

    private String applicationType;
    private String description;
    private String status;
    private boolean active;

    public FinancialForm(Long id, String firstName, String lastName, Long cnic, long contact, String fatherName, Long fatherCnic, Long fatherContactNumber, Long fatherOccupation, Integer applicantAge, String currentAddress, String permanentAddress, boolean maritalStatus, String applicationType, String description, String status, boolean active) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cnic = cnic;
        this.contact = contact;
        this.fatherName = fatherName;
        this.fatherCnic = fatherCnic;
        this.fatherContactNumber = fatherContactNumber;
        this.fatherOccupation = fatherOccupation;
        this.applicantAge = applicantAge;
        this.currentAddress = currentAddress;
        this.permanentAddress = permanentAddress;
        this.maritalStatus = maritalStatus;
        this.applicationType = applicationType;
        this.description = description;
        this.status = status;
        this.active = active;
    }

    public FinancialForm() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getFatherContactNumber() {
        return fatherContactNumber;
    }

    public void setFatherContactNumber(Long fatherContactNumber) {
        this.fatherContactNumber = fatherContactNumber;
    }

    public Long getFatherOccupation() {
        return fatherOccupation;
    }

    public void setFatherOccupation(Long fatherOccupation) {
        this.fatherOccupation = fatherOccupation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        lastName = lastName;
    }

    public Long getCnic() {
        return cnic;
    }

    public void setCnic(Long cnic) {
        this.cnic = cnic;
    }

    public long getContact() {
        return contact;
    }

    public void setContact(long contact) {
        this.contact = contact;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public Long getFatherCnic() {
        return fatherCnic;
    }

    public void setFatherCnic(Long fatherCnic) {
        this.fatherCnic = fatherCnic;
    }

    public Integer getApplicantAge() {
        return applicantAge;
    }

    public void setApplicantAge(Integer applicantAge) {
        this.applicantAge = applicantAge;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        currentAddress = currentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public boolean isMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(boolean maritalStatus) {
        maritalStatus = maritalStatus;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        applicationType = applicationType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
