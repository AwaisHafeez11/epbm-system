package com.app.epbmsystem.model.Forms;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Data
@Entity
@Table(name = "role", indexes = {
        @Index(name = "created_date_index", columnList = "createdDate"),
        @Index(name = "active_index", columnList = "active"),
        @Index(name = "applicationStatus_index",columnList = "applicationStatus")
})
public class ResidentialForm implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @Column(nullable = false)
    private String occupation;
    @Column(nullable = false)
    private String fatherName;
    @Column(nullable = false, unique = true)
    private Long fatherContactNumber;
    @Column(nullable = false)
    private Long fatherCnic;
    @Column(nullable = false)
    private Integer monthlyIncome;
    @Column(nullable = false)
    private Integer NoOfFamilyMembers;
    @Column(nullable = false)
    private String description;
    private Date createdDate;
    private Date updatedDate;
    private String remarks;
    private String applicationStatus;
    private Boolean active;
    private String currentLivingAddress;
    private String qualification;

}
