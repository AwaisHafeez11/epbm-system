package com.app.epbmsystem.repository;

import com.app.epbmsystem.model.Forms.ResidentialForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidentialRepository extends JpaRepository<ResidentialForm,Long> {
}
