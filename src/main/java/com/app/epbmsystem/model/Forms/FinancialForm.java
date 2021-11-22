package com.app.epbmsystem.model.Forms;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;


@Data
@Entity
@Table(name = "financialForm", indexes = {
        @Index(name = "created_date_index", columnList = "createdDate"),
        @Index(name = "active_index", columnList = "active"),
        @Index(name = "applicationStatus_index",columnList = "applicationStatus")
})
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
    private boolean active;
    private Date createdDate;
    private Date updatedDate;
    private String applicationStatus;
    private String adminRemarks;

}
