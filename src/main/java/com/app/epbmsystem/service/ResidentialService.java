package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Forms.ResidentialForm;
import com.app.epbmsystem.repository.ResidentialRepository;
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
public class ResidentialService {
    final ResidentialRepository residentialRepository;
    ResidentialForm residentialForm;

    public ResidentialService(ResidentialRepository residentialRepository) {
        this.residentialRepository = residentialRepository;
    }

    private static final Logger LOG = LogManager.getLogger(ResidentialService.class);

    public ResponseEntity<Object> listAllResidentialFroms(){   // List of all residentialForm
        try {
            List<ResidentialForm> residentialFormList= residentialRepository.findAll();
            if (residentialFormList.isEmpty())
            {
                return new ResponseEntity<>("No residentialForm exists in the database", HttpStatus.NOT_FOUND);
            }
            else
            {
                return new ResponseEntity<>(residentialFormList, HttpStatus.OK);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> saveResidentialForm(ResidentialForm residentialForm) {
        residentialRepository.save(residentialForm);
        return new ResponseEntity<>("residential Application Added /n Thank you for adding   ",HttpStatus.OK);
    }

    public ResponseEntity<Object> updateResidentialForm(ResidentialForm residentialForm){                  // Update user
        try{
            Long id = residentialForm.getId();
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            if (residentialRepository.existsById(id)) {
                residentialRepository.save(residentialForm);
                return new ResponseEntity<>("residentialForm updated thank you", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("residentialForm not exist", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteResidentialForm(Long id){                   //Financialform deleted
        try{
            if (residentialRepository.existsById(id)) {
                residentialRepository.delete(residentialForm);
                return new ResponseEntity<>(" residentialForm has been Deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("residentialForm Not exists Please enter Valid ID", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            LOG.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("residentialForm deleted", HttpStatus.BAD_REQUEST);}
    }

    public ResponseEntity<Object> getResidentialForm(Long id){
        try{
            Optional<ResidentialForm> residentialForm = residentialRepository.findById(id);
            if (residentialForm.isPresent())
            {return new ResponseEntity<>(residentialForm, HttpStatus.FOUND); }
            else
            {return new ResponseEntity<>(residentialForm, HttpStatus.NOT_FOUND); }
        }
        catch (Exception exception)
        {return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
    }

}
