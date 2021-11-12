package com.app.epbmsystem.model.Entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @Column(nullable = false,unique = true)
    private String name;
    private Date createdDate;
    private Date updatedDate;
    private boolean active;

    /**
     * One Role can have multiple Permissions, and one permission can have multiple roles
     */
    @ManyToMany(targetEntity = Permission.class,fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Permission> permissions = new ArrayList<>();
}

