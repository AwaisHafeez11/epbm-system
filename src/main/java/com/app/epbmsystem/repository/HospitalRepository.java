package com.app.epbmsystem.repository;

import com.app.epbmsystem.model.Forms.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital,Long> {


}
