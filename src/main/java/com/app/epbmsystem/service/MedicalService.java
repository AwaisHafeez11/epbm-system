package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Forms.FinancialForm;
import com.app.epbmsystem.model.Forms.MedicalForm;
import com.app.epbmsystem.repository.MedicalRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalService {
    final MedicalRepository medicalRepository;
    MedicalForm medicalForm;


    public MedicalService(MedicalRepository medicalRepository) {
        this.medicalRepository = medicalRepository;
    }

    private static final Logger LOG = LogManager.getLogger(MedicalService.class);

    public ResponseEntity<Object> listAllMedicalFroms(){   // List of all Medical applications
        try {
            List<MedicalForm> medicalForms= medicalRepository.findAll();
            if (medicalForms.isEmpty())
            {
                return new ResponseEntity<>("No MedicalForm exists in the database", HttpStatus.NOT_FOUND);
            }
            else
            {
                return new ResponseEntity<>(medicalForms, HttpStatus.OK);
            }
        }
        catch (Exception e){
             return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> saveMedicalForm(MedicalForm medicalForm) {
               medicalRepository.save(medicalForm);
               return new ResponseEntity<>("Medical Application Added /n Thank you for adding   ",HttpStatus.OK);
    }

    public ResponseEntity<Object> updateMedicalForm(MedicalForm medicalForm){                  // Update user
        try{
            Long id = medicalForm.getId();
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            if (medicalRepository.existsById(id)) {
                medicalRepository.save(medicalForm);
                return new ResponseEntity<>("medicalForm updated thank you", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("medicalForm not exist", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteMedicalForm(Long id){                   //Financialform deleted
        try{
            if (medicalRepository.existsById(id)) {
                medicalRepository.delete(medicalForm);
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
            Optional<MedicalForm> medicalForm = medicalRepository.findById(id);
            if (medicalForm.isPresent())
            {return new ResponseEntity<>(medicalForm, HttpStatus.FOUND); }
            else
            {return new ResponseEntity<>(medicalForm, HttpStatus.NOT_FOUND); }
        }
        catch (Exception exception)
            {return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
    }

}
