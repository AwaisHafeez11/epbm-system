package com.app.epbmsystem.repository;

import com.app.epbmsystem.model.Forms.EducationalForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationalRepository extends JpaRepository<EducationalForm,Long> {
}
