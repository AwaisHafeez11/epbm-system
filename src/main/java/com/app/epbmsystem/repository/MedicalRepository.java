package com.app.epbmsystem.repository;

import com.app.epbmsystem.model.Forms.Disease;
import com.app.epbmsystem.model.Forms.FinancialForm;
import com.app.epbmsystem.model.Forms.MedicalForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface MedicalRepository extends JpaRepository<MedicalForm,Long> {
    @Query(value = "SELECT * FROM medical_form where created_date like CONCAT(:date,'%')", nativeQuery = true)
    List<MedicalForm> findByCreatedDate(Date date);
    @Query(value = "SELECT * from medical_form where created_date >= :startDate order by created_date asc", nativeQuery = true)
    List<MedicalForm> findByStartDate(Date startDate);
    List<MedicalForm> findByCreatedDateBetweenOrderByUpdatedDateAsc(Date startDate,Date EndDate);
    List<MedicalForm> findAllByActive(Boolean active);
}
