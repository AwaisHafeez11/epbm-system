package com.app.epbmsystem.repository;

import com.app.epbmsystem.model.Forms.EducationalForm;
import com.app.epbmsystem.model.Forms.ResidentialForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ResidentialRepository extends JpaRepository<ResidentialForm,Long> {
    @Query(value = "SELECT * FROM residential_form where created_date like CONCAT(:date,'%')", nativeQuery = true)
    List<ResidentialForm> findByCreatedDate(Date date);
    @Query(value = "SELECT * from residential_form where created_date >= :startDate order by created_date asc", nativeQuery = true)
    List<ResidentialForm> findByStartDate(Date startDate);
    List<ResidentialForm> findByCreatedDateBetweenOrderByUpdatedDateAsc(Date startDate,Date endDate);
    List<ResidentialForm> findAllByActive(Boolean active);

    @Query(value = "SELECT * from residential_form where user_id",nativeQuery = true)
    List<ResidentialForm> findResidentialFormsByUserId(Long user_id);

    @Query(value = "SELECT * from residential_form where applicationStatus",nativeQuery = true)
    List<ResidentialForm> findResidentialFormsByApplicationStatus(String applicationStatus);


}
