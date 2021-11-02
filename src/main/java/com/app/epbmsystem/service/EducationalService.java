package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Forms.EducationalForm;
import com.app.epbmsystem.model.Forms.FinancialForm;
import com.app.epbmsystem.repository.EducationalRepository;
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
public class EducationalService {
    final EducationalRepository educationalRepository;
    EducationalForm educationalForm;

    public EducationalService(EducationalRepository educationalRepository) {
        this.educationalRepository = educationalRepository;
    }


    private static final Logger LOG = LogManager.getLogger(EducationalService.class);

    public ResponseEntity<Object> listAllEducationalFroms(){   // List of all financial applications
        try {
            List<EducationalForm> educationalFormList= educationalRepository.findAll();
            if (educationalFormList.isEmpty())
            {
                return new ResponseEntity<>("No EducationalForm exists in the database", HttpStatus.NOT_FOUND);
            }
            else
            {
                return new ResponseEntity<>(educationalFormList, HttpStatus.OK);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> saveEducationalForm(EducationalForm educationalForm) {
        educationalRepository.save(educationalForm);
        return new ResponseEntity<>("Educational Application Added /n Thank you for adding   ",HttpStatus.OK);
    }

    public ResponseEntity<Object> updateEducationalForm(EducationalForm educationalForm){                  // Update user
        try{
            Long id = educationalForm.getId();
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            if (educationalRepository.existsById(id)) {
                educationalRepository.save(educationalForm);
                return new ResponseEntity<>("EducationalForm updated thank you", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("EducationalForm not exist", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteEducationalForm(Long id){                   //Financialform deleted
        try{
            if (educationalRepository.existsById(id)) {
                educationalRepository.delete(educationalForm);
                return new ResponseEntity<>(" EducationalForm has been Deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("EducationalForm Not exists Please enter Valid ID", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            LOG.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("EducationalForm deleted", HttpStatus.BAD_REQUEST);}
    }

    public ResponseEntity<Object> getEducationalForm(Long id){
        try{
            Optional<EducationalForm> educationalForm = educationalRepository.findById(id);
            if (educationalForm.isPresent())
            {return new ResponseEntity<>(educationalForm, HttpStatus.FOUND); }
            else
            {return new ResponseEntity<>(educationalForm, HttpStatus.NOT_FOUND); }
        }
        catch (Exception exception)
        {return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
    }
}
