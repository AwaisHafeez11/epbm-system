package com.app.epbmsystem.repository;

import com.app.epbmsystem.model.Entity.Category;
import com.app.epbmsystem.model.Forms.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease,Long> {
    Optional<Disease> findDiseaseByName(String name);
    List<Disease> findAllByActive(Boolean active);

}
