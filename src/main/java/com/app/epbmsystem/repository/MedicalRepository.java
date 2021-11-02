package com.app.epbmsystem.repository;

import com.app.epbmsystem.model.Forms.MedicalForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRepository extends JpaRepository<MedicalForm,Long> {
}
