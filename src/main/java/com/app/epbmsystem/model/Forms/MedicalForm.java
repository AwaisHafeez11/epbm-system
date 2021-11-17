package com.app.epbmsystem.model.Forms;

import lombok.Data;

import com.app.epbmsystem.model.Entity.Role;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
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
    private Date createdDate;
    private Date updatedDate;

    /**
     * One Medical forms can have multiple diseases, and one diseases can have multiple Medical forms
     */
    @ManyToMany(targetEntity = Disease.class,fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Disease> diseases = new ArrayList<>();



}
