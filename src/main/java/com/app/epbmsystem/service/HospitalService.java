package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Forms.Disease;
import com.app.epbmsystem.model.Forms.Hospital;
import com.app.epbmsystem.repository.HospitalRepository;
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
public class HospitalService {
    final HospitalRepository hospitalRepository;
    Hospital hospital;
    private static final Logger LOG = LogManager.getLogger(HospitalService.class);

    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public ResponseEntity<Object> listAllHospitals(){   // List of all Hospitals
        try {
            List<Hospital> hospitalList= hospitalRepository.findAll();
            if (hospitalList.isEmpty())
            {
                return new ResponseEntity<>("No Hospital exists in the database", HttpStatus.NOT_FOUND);
            }
            else
            {
                return new ResponseEntity<>(hospitalList, HttpStatus.OK);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> saveHospital(Hospital disease) {
        hospitalRepository.save(hospital);
        return new ResponseEntity<>("hospital Added /n Thank you for adding   ",HttpStatus.OK);
    }

    public ResponseEntity<Object> updateHospital(Hospital hospital){                  // Update user
        try{
            Long id = hospital.getId();
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            if (hospitalRepository.existsById(id)) {
                hospitalRepository.save(hospital);
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

    public ResponseEntity<Object> deleteHospital(Long id){                   //Hospital deleted
        try{
            if (hospitalRepository.existsById(id)) {
                hospitalRepository.delete(hospital);
                return new ResponseEntity<>(" hospital has been Deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("hospital Not exists Please enter Valid ID", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            LOG.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("Hospital deleted", HttpStatus.BAD_REQUEST);}
    }

    public ResponseEntity<Object> getHospital(Long id){
        try{
            Optional<Hospital> hospitalList = hospitalRepository.findById(id);
            if (hospitalList.isPresent())
            {return new ResponseEntity<>(hospitalList, HttpStatus.FOUND); }
            else
            {return new ResponseEntity<>(hospitalList, HttpStatus.NOT_FOUND); }
        }
        catch (Exception exception)
        {return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
    }
}
