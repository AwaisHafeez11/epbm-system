package com.app.epbmsystem.model.Forms;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "medicalForm", indexes = {
        @Index(name = "created_date_index", columnList = "createdDate"),
        @Index(name = "active_index", columnList = "active"),
        @Index(name = "applicationStatus_index",columnList = "applicationStatus")
})
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
    private Date createdDate;
    private Date updatedDate;

    /**
     * One Medical forms can have multiple diseases, and one diseases can have multiple Medical forms
     */
    @ManyToMany(targetEntity = Disease.class,fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Disease> diseases = new ArrayList<>();



}
