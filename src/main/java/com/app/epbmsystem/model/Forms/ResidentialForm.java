package com.app.epbmsystem.model.Forms;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class ResidentialForm implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @Column(nullable = false)
    private String occupation;
    @Column(nullable = false)
    private String fatherName;
    @Column(nullable = false)
    private Long fatherContactNumber;
    @Column(nullable = false)
    private Long fatherCnic;
    @Column(nullable = false)
    private Integer monthlyIncome;
    @Column(nullable = false)
    private Integer NoOfFamilyMembers;
    @Column(nullable = false)
    private String description;

    public ResidentialForm(Long id, String occupation, String fatherName, Long fatherContactNumber, Long fatherCnic, Integer monthlyIncome, Integer noOfFamilyMembers, String description) {
        this.id = id;
        this.occupation = occupation;
        this.fatherName = fatherName;
        this.fatherContactNumber = fatherContactNumber;
        this.fatherCnic = fatherCnic;
        this.monthlyIncome = monthlyIncome;
        NoOfFamilyMembers = noOfFamilyMembers;
        this.description = description;
    }


    public ResidentialForm() {

    }




}
