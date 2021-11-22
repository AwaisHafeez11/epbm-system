package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Forms.Disease;
import com.app.epbmsystem.repository.DiseaseRepository;
import com.app.epbmsystem.util.ResponseHandler;
import com.app.epbmsystem.util.SqlDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
public class DiseaseService {
    final DiseaseRepository diseaseRepository;

    private static final Logger LOG = LogManager.getLogger(DiseaseService.class);

    public DiseaseService(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    /**
     * List of all active and inactive Diseases return
     * @return
     */
    public ResponseEntity<Object> listAllDisease() throws ParseException {
        try {
            List<Disease> diseaseList= diseaseRepository.findAll();
            if (diseaseList.isEmpty())
            {
                LOG.info("No record in disease Table");
                return ResponseHandler.generateResponse(HttpStatus.NO_CONTENT,true,"Data not Found", null );
            }
            else
            {
                LOG.info("Returning a list of disease via listAllDisease Method at diseaseService class");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of diseases",diseaseList);
            }
        }
        catch (Exception e){
            LOG.info("Exception throws by method listAllDisease at diseaseService class ");
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * This method is adding the disease
     * @param disease
     * @return
     */
    public ResponseEntity<Object> saveDisease(Disease disease) throws ParseException {
        try{
            Optional<Disease> existingDisease= diseaseRepository.findDiseaseByName(disease.getName());
            if(existingDisease.isPresent())
            {   if(existingDisease.get().isActive())
                {return new ResponseEntity<>("Disease Already exists",HttpStatus.BAD_REQUEST);}
                else
                {
                    existingDisease.get().setActive(true);
                    existingDisease.get().setUpdatedDate(SqlDate.getDateInSqlFormat());
                    existingDisease.get().setCreatedDate(SqlDate.getDateInSqlFormat());
                    diseaseRepository.save(existingDisease.get());
                    return ResponseHandler.generateResponse(HttpStatus.OK,false,"added inactive disease successfully",null);
                }
            }
            else
            {
                disease.setCreatedDate(SqlDate.getDateInSqlFormat());
                disease.setUpdatedDate(SqlDate.getDateInSqlFormat());
                disease.setActive(true);
                diseaseRepository.save(disease);
                LOG.info("adding disease into its table");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"disease Successfully added",null);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception throws by SaveDisease method  ");
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * This serivce method is updating the disease
     * @param disease
     * @return
     */
    public ResponseEntity<Object> updateDisease(Disease disease) throws ParseException {                  // Update user
        try{
            Long id = disease.getId();
            if (diseaseRepository.existsById(id)) {
                disease.setUpdatedDate(SqlDate.getDateInSqlFormat());
                diseaseRepository.save(disease);
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"Disease updated thank you",null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,true,"Disease not exist",null);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception throws by UpdateDisease method");
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * Delete disease from db by using disease ID
     * @param id
     * @return
     */
    public ResponseEntity<Object> deleteDisease(Long id) throws ParseException {
        try{
            Optional<Disease> existingDisease= diseaseRepository.findById(id);
            if (existingDisease.isPresent())
            {
                existingDisease.get().setUpdatedDate(SqlDate.getDateInSqlFormat());
                existingDisease.get().setActive(false);
                diseaseRepository.save(existingDisease.get());
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"disease has been Deleted/Inactivated",null);
            }
            else
            {
                return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,true,"disease Not exists Please enter Valid ID",null);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception: " + e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * getting disease by id
     * @param id
     * @return
     */
    public ResponseEntity<Object> getDisease(Long id) throws ParseException {
        try{
            Optional<Disease> diseaseList = diseaseRepository.findById(id);
            if (diseaseList.isPresent())
            {
            return ResponseHandler.generateResponse(HttpStatus.FOUND,false,"",diseaseList);
            }
            else
            {
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"",null);
            }
        }
        catch (Exception exception)
        {
            LOG.info("Exception: "+exception.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+exception.getMessage(),null); }
    }

    /**
     * display list of all active diseases
     * @return
     */
    public ResponseEntity<Object> listAllActivediseases() throws ParseException {
        try {
            List<Disease> diseaseList = diseaseRepository.findAllByActive(true);
            if (diseaseList.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no Disease in the database",null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of active diseases",diseaseList);
            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+ e.getMessage(),null);
        }
    }

    /**
     * display list of all inactive diseases
     * @return
     */
    public ResponseEntity<Object> listAllInactiveDiseases() throws ParseException {
        try {
            List<Disease> diseaseList = diseaseRepository.findAllByActive(false);
            if (diseaseList.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no Disease in the database",null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of inactive",diseaseList);
            }
        } catch (Exception e) {
            LOG.info("Exception throws by listAllInactiveDiseases at diseaseService  "+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

}
