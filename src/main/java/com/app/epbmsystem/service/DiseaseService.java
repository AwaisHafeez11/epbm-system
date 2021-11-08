package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Forms.Disease;
import com.app.epbmsystem.repository.DiseaseRepository;
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
public class DiseaseService {
    final DiseaseRepository diseaseRepository;
    Disease disease;
    private static final Logger LOG = LogManager.getLogger(DiseaseService.class);

    public DiseaseService(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    public ResponseEntity<Object> listAllDisease(){   // List of all residentialForm
        try {
            List<Disease> diseaseList= diseaseRepository.findAll();
            if (diseaseList.isEmpty())
            {
                return new ResponseEntity<>("No disease exists in the database", HttpStatus.NOT_FOUND);
            }
            else
            {
                return new ResponseEntity<>(diseaseList, HttpStatus.OK);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> saveDisease(Disease disease) {
        diseaseRepository.save(disease);
        return new ResponseEntity<>("disease Added /n Thank you for adding   ",HttpStatus.OK);
    }

    public ResponseEntity<Object> updateDisease(Disease disease){                  // Update user
        try{
            Long id = disease.getId();
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            if (diseaseRepository.existsById(id)) {
                diseaseRepository.save(disease);
                return new ResponseEntity<>("Disease updated thank you", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Disease not exist", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteDisease(Long id){                   //Financialform deleted
        try{
            if (diseaseRepository.existsById(id)) {
                diseaseRepository.delete(disease);
                return new ResponseEntity<>(" disease has been Deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("disease Not exists Please enter Valid ID", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            LOG.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("disease deleted", HttpStatus.BAD_REQUEST);}
    }

    public ResponseEntity<Object> getDisease(Long id){
        try{
            Optional<Disease> diseaseList = diseaseRepository.findById(id);
            if (diseaseList.isPresent())
            {return new ResponseEntity<>(diseaseList, HttpStatus.FOUND); }
            else
            {return new ResponseEntity<>(diseaseList, HttpStatus.NOT_FOUND); }
        }
        catch (Exception exception)
        {return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
    }

}
