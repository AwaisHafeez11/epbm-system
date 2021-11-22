package com.app.epbmsystem.model.Forms;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "hospital", indexes = {
        @Index(name = "created_date_index", columnList = "createdDate"),
        @Index(name = "active_index", columnList = "active")
})
public class Hospital implements Serializable {
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
    private boolean active;

}
