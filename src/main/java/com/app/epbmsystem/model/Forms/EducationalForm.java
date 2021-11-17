package com.app.epbmsystem.model.Forms;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;


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
    private Date createdDate;
    private Date updatedDate;


}

