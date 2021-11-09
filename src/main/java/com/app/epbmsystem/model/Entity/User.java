package com.app.epbmsystem.model.Entity;

// import lombok.Data;

import com.app.epbmsystem.model.Forms.EducationalForm;
import com.app.epbmsystem.model.Forms.FinancialForm;
import com.app.epbmsystem.model.Forms.MedicalForm;
import com.app.epbmsystem.model.Forms.ResidentialForm;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Data
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastname;
    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(unique = true,nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String gender;
    private boolean status;
    @Column(nullable = false)
    private String userType;
    private String dateOfBirth;
    @Column(nullable = false)
    private String cnic;
    @Column(nullable = false)
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
    @OneToMany(targetEntity = EducationalForm.class,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
    @ManyToMany(targetEntity = Role.class,fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Role> roles = new ArrayList<>();

    /**
     * One user can have one category, and one category can have multiple users
     */
    @ManyToOne(targetEntity = Category.class,fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Category category;

    public User() {
    }

    public User(String firstName, String lastname, String email, String password, String phoneNumber, String gender, boolean status, String userType, String dateOfBirth, String cnic, long age, Date createdDate, Date updatedDate, boolean active, String smsToken, String emailToken, List<ResidentialForm> residentialForms, List<EducationalForm> educationalForms, List<MedicalForm> medicalForms, List<FinancialForm> financialForms, List<Role> roles, Category category) {
        this.firstName = firstName;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.status = status;
        this.userType = userType;
        this.dateOfBirth = dateOfBirth;
        this.cnic = cnic;
        this.age = age;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.active = active;
        this.smsToken = smsToken;
        this.emailToken = emailToken;
/*        this.residentialForms = residentialForms;
        this.educationalForms = educationalForms;
        this.medicalForms = medicalForms;
        this.financialForms = financialForms;
        this.roles = roles;
        this.category = category;*/
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSmsToken() {
        return smsToken;
    }

    public void setSmsToken(String smsToken) {
        this.smsToken = smsToken;
    }

    public String getEmailToken() {
        return emailToken;
    }

    public void setEmailToken(String emailToken) {
        this.emailToken = emailToken;
    }

    public List<ResidentialForm> getResidentialForms() {
        return residentialForms;
    }

    public void setResidentialForms(List<ResidentialForm> residentialForms) {
        this.residentialForms = residentialForms;
    }

    public List<EducationalForm> getEducationalForms() {
        return educationalForms;
    }

    public void setEducationalForms(List<EducationalForm> educationalForms) {
        this.educationalForms = educationalForms;
    }

    public List<MedicalForm> getMedicalForms() {
        return medicalForms;
    }

    public void setMedicalForms(List<MedicalForm> medicalForms) {
        this.medicalForms = medicalForms;
    }

    public List<FinancialForm> getFinancialForms() {
        return financialForms;
    }

    public void setFinancialForms(List<FinancialForm> financialForms) {
        this.financialForms = financialForms;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


}