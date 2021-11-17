package com.app.epbmsystem.model.Forms;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;


@Data
@Entity
public class Disease implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private Date createdDate;
    private Date updatedDate;
    private boolean active;

}
