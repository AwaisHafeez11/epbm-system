package com.app.epbmsystem.model.Entity;

import com.app.epbmsystem.model.Forms.EducationalForm;
import com.app.epbmsystem.model.Forms.FinancialForm;
import com.app.epbmsystem.model.Forms.MedicalForm;
import com.app.epbmsystem.model.Forms.ResidentialForm;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "user", indexes = {
        @Index(name = "created_date_index", columnList = "createdDate"),
        @Index(name = "active_index", columnList = "active")
})
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    private String firstName;
    private String lastname;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String gender;
    private String dateOfBirth;
    @Column(nullable = false, unique = true)
    private String cnic;
    private long age;
    private Date createdDate;
    private Date updatedDate;
    private boolean active;
    private String smsToken;
    private String emailToken;

    /**
     * One user can have multiple residentialForms, and one residentialForm can have one user
     */
    @OneToMany(targetEntity = ResidentialForm.class,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<ResidentialForm> residentialForms = new ArrayList<>();

    /**
     * One user can have multiple EducationalForms, and one educationalForms can have one user
     */
    @OneToMany(targetEntity = EducationalForm.class,fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<EducationalForm> educationalForms = new ArrayList<>();

    /**
     * One user can have multiple medicalForms, and one medicalForm can have one user
     */
    @OneToMany(targetEntity = MedicalForm.class,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<MedicalForm> medicalForms = new ArrayList<>();

    /**
     * One user can have multiple FinancialForms, and one financialform can have one user
     */
    @OneToMany(targetEntity = FinancialForm.class,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<FinancialForm> financialForms = new ArrayList<>();

    /**
     * One user can have multiple roles, and one role can have multiple users
     */
    @ManyToMany(targetEntity = Role.class,fetch = FetchType.LAZY)
    private List<Role> roles = new ArrayList<>();

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    /**
     * One user can have one category, and one category can have multiple users
     */
    @ManyToOne(targetEntity = Category.class,fetch = FetchType.LAZY)
    private Category category;

}