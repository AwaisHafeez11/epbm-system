package com.app.epbmsystem.repository;

import com.app.epbmsystem.model.Forms.FinancialForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialRepository extends JpaRepository<FinancialForm,Long> {
}
