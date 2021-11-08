package com.app.epbmsystem.model.Forms;

import com.app.epbmsystem.model.Entity.User;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
public class EducationalForm implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @Column(nullable = false)
    private String fatherName;
    @Column(nullable = false)
    private String fatherCnic;
    @Column(nullable = false)
    private boolean governmentEmploy;
    @Column(nullable = false)
    private String fatherOccupation;
    @Column(nullable = false)
    private int numberOfSiblings;
    @Column(nullable = false)
    private String applyingForGrade;
    @Column(nullable = false)
    private String applyingForBoard;
    @Column(nullable = false)
    private String subjects;

    private Integer metricTotalMarks;
    private Integer metricObtainedMarks;
    private String metricBoard;
    private String schoolName;

    private Integer interTotalMarks;
    private Integer interObtainedMarks;
    private String interBoard;



    @Column(nullable = false)
    private boolean applicationType;
    private String remarks;
    @Column(nullable = false)
    private String description;
    private String applicationStatus;
    private boolean active;


    public EducationalForm(Long id, String fatherName, String fatherCnic, boolean governmentEmploy, String fatherOccupation, int numberOfSiblings, String applyingForGrade, String applyingForBoard, String subjects, Integer metricTotalMarks, Integer metricObtainedMarks, String metricBoard, String schoolName, Integer interTotalMarks, Integer interObtainedMarks, String interBoard, boolean applicationType, String remarks, String description, String applicationStatus, boolean active) {
        this.id = id;
        this.fatherName = fatherName;
        this.fatherCnic = fatherCnic;
        this.governmentEmploy = governmentEmploy;
        this.fatherOccupation = fatherOccupation;
        this.numberOfSiblings = numberOfSiblings;
        this.applyingForGrade = applyingForGrade;
        this.applyingForBoard = applyingForBoard;
        this.subjects = subjects;
        this.metricTotalMarks = metricTotalMarks;
        this.metricObtainedMarks = metricObtainedMarks;
        this.metricBoard = metricBoard;
        this.schoolName = schoolName;
        this.interTotalMarks = interTotalMarks;
        this.interObtainedMarks = interObtainedMarks;
        this.interBoard = interBoard;
        this.applicationType = applicationType;
        this.remarks = remarks;
        this.description = description;
        this.applicationStatus = applicationStatus;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFatherCnic() {
        return fatherCnic;
    }

    public void setFatherCnic(String fatherCnic) {
        this.fatherCnic = fatherCnic;
    }

    public boolean isGovernmentEmploy() {
        return governmentEmploy;
    }

    public void setGovernmentEmploy(boolean governmentEmploy) {
        this.governmentEmploy = governmentEmploy;
    }

    public String getFatherOccupation() {
        return fatherOccupation;
    }

    public void setFatherOccupation(String fatherOccupation) {
        this.fatherOccupation = fatherOccupation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public int getNumberOfSiblings() {
        return numberOfSiblings;
    }

    public void setNumberOfSiblings(int numberOfSiblings) {
        this.numberOfSiblings = numberOfSiblings;
    }

    public String getApplyingForGrade() {
        return applyingForGrade;
    }

    public void setApplyingForGrade(String applyingForGrade) {
        this.applyingForGrade = applyingForGrade;
    }

    public String getApplyingForBoard() {
        return applyingForBoard;
    }

    public void setApplyingForBoard(String applyingForBoard) {
        this.applyingForBoard = applyingForBoard;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public Integer getMetricTotalMarks() {
        return metricTotalMarks;
    }

    public void setMetricTotalMarks(Integer metricTotalMarks) {
        this.metricTotalMarks = metricTotalMarks;
    }

    public Integer getMetricObtainedMarks() {
        return metricObtainedMarks;
    }

    public void setMetricObtainedMarks(Integer metricObtainedMarks) {
        this.metricObtainedMarks = metricObtainedMarks;
    }

    public String getMetricBoard() {
        return metricBoard;
    }

    public void setMetricBoard(String metricBoard) {
        this.metricBoard = metricBoard;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Integer getInterTotalMarks() {
        return interTotalMarks;
    }

    public void setInterTotalMarks(Integer interTotalMarks) {
        this.interTotalMarks = interTotalMarks;
    }

    public Integer getInterObtainedMarks() {
        return interObtainedMarks;
    }

    public void setInterObtainedMarks(Integer interObtainedMarks) {
        this.interObtainedMarks = interObtainedMarks;
    }

    public String getInterBoard() {
        return interBoard;
    }

    public void setInterBoard(String interBoard) {
        this.interBoard = interBoard;
    }

    public boolean isApplicationType() {
        return applicationType;
    }

    public void setApplicationType(boolean applicationType) {
        this.applicationType = applicationType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public EducationalForm() {

    }





}

