package com.app.epbmsystem.repository;

import com.app.epbmsystem.model.Forms.FinancialForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface FinancialRepository extends JpaRepository<FinancialForm,Long> {
    @Query(value = "SELECT * FROM financial_form where created_date like CONCAT(:date,'%')", nativeQuery = true)
    List<FinancialForm> findByCreatedDate(Date date);
    @Query(value = "SELECT * from financial_form where created_date >= :startDate order by created_date asc", nativeQuery = true)
    List<FinancialForm> findByStartDate(Date startDate);
    List<FinancialForm> findByCreatedDateBetweenOrderByUpdatedDateAsc(Date startDate,Date EndDate);
    List<FinancialForm> findByActive(Boolean active);
}
