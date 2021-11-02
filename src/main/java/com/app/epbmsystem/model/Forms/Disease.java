package com.app.epbmsystem.model.Forms;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Disease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    private String createdDate;
    private String updatedDate;

}
