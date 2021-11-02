package com.app.epbmsystem.model.Forms;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String city;
    private String address;
    @Column(nullable = false)
    private boolean hospitalType; // True is Government/ false is private hospital

    private String createdDate;
    private String updatedDate;

}
