package com.app.epbmsystem.model.Forms;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ResidentialForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @Column(nullable = false)
    private String name;

    public ResidentialForm(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ResidentialForm() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
