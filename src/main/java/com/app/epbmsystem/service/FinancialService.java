package com.app.epbmsystem.service;


import com.app.epbmsystem.model.Forms.FinancialForm;
import com.app.epbmsystem.repository.FinancialRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
public class FinancialService {
    @Autowired
    final FinancialRepository financialRepository;
    FinancialForm financialForm;

    public FinancialService(FinancialRepository financialRepository) {
        this.financialRepository = financialRepository;
    }
    private static final Logger LOG = LogManager.getLogger(FinancialService.class);

    public ResponseEntity<Object> listAllFinancialFroms(){   // List of all financial applications
        try {
                List<FinancialForm> financialFormList= financialRepository.findAll();
                if (financialFormList.isEmpty())
                {
                    return new ResponseEntity<>("No FinancialForm exists in the database", HttpStatus.NOT_FOUND);
                }
                else
                {
                    return new ResponseEntity<>(financialFormList, HttpStatus.OK);
                }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> saveFinancialForm(FinancialForm financialForm) {
            financialRepository.save(financialForm);
            return new ResponseEntity<>("Financial Application Added /n Thank you for adding   ",HttpStatus.OK);
    }

    public ResponseEntity<Object> updateFinancialForm(FinancialForm financialForm){                  // Update user
        try{
                Long id = financialForm.getId();
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                if (financialRepository.existsById(id)) {
                    financialRepository.save(financialForm);
                    return new ResponseEntity<>("financialForm updated thank you", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("financialForm not exist", HttpStatus.NOT_FOUND);
                }
        }
        catch (Exception e)
            {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    public ResponseEntity<Object> deleteFinancialForm(Long id){                   //Financialform deleted
        try{
            if (financialRepository.existsById(id)) {
                financialRepository.delete(financialForm);
                return new ResponseEntity<>(" financialForm has been Deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("financialForm Not exists Please enter Valid ID", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            LOG.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("financialForm deleted", HttpStatus.BAD_REQUEST);}
    }

    public ResponseEntity<Object> getFinancialForm(Long id){
        try{
            Optional<FinancialForm> financialForm = financialRepository.findById(id);
            if (financialForm.isPresent())
            {return new ResponseEntity<>(financialForm, HttpStatus.FOUND); }
            else
            {return new ResponseEntity<>(financialForm, HttpStatus.NOT_FOUND); }
        }
        catch (Exception exception)
        {return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
    }
}
