package com.app.epbmsystem.repository;

import com.app.epbmsystem.model.Forms.EducationalForm;
import com.app.epbmsystem.model.Forms.MedicalForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;


@Repository
public interface EducationalRepository extends JpaRepository<EducationalForm,Long> {

    @Query(value = "SELECT * FROM educational_form where created_date like CONCAT(:date,'%')", nativeQuery = true)
    List<EducationalForm> findByCreatedDate(Date date);
    @Query(value = "SELECT * from interest_rates where created_date >= :startDate order by created_date asc", nativeQuery = true)
    List<EducationalForm> findByStartDate(Date startDate);
    List<EducationalForm> findAllByActive(Boolean active);

    List<EducationalForm> findByCreatedDateBetweenOrderByUpdatedDateAsc(Date startDate,Date EndDate);

    @Query(value = "SELECT * from educational_form where user_id",nativeQuery = true)
     List<EducationalForm> findEducationalFormsByUserId(Long user_id);

    @Query(value = "SELECT * from educational_form where applicationStatus",nativeQuery = true)
    List<EducationalForm> findEducationalFormsByApplicationStatus(String applicationStatus);



}
