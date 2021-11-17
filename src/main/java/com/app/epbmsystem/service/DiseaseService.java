package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Forms.Disease;
import com.app.epbmsystem.repository.DiseaseRepository;
import com.app.epbmsystem.util.SqlDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    public ResponseEntity<Object> listAllDisease(){
        try {
            List<Disease> diseaseList= diseaseRepository.findAll();
            if (diseaseList.isEmpty())
            {
                LOG.info("No record in disease Table");
                return new ResponseEntity<>("No disease exists in the database", HttpStatus.NOT_FOUND);
            }
            else
            {
                LOG.info("Returning a list of disease via listAllDisease Method at diseaseService class");
                return new ResponseEntity<>(diseaseList, HttpStatus.OK);
            }
        }
        catch (Exception e){
            LOG.info("Exception throws by method listAllDisease at diseaseService class ");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This method is adding the disease
     * @param disease
     * @return
     */
    public ResponseEntity<Object> saveDisease(Disease disease) {
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
                    return new ResponseEntity<>(" added inactive disease successfully",HttpStatus.OK);
                }
            }
            else
            {
                disease.setCreatedDate(SqlDate.getDateInSqlFormat());
                disease.setUpdatedDate(SqlDate.getDateInSqlFormat());
                disease.setActive(true);
                diseaseRepository.save(disease);
                LOG.info("adding disease into its table");
                return new ResponseEntity<>("disease Successfully added",HttpStatus.OK);
            }

        }
        catch (Exception e)
        {
            LOG.info("Exception throws by SaveDisease method  ");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This serivce method is updating the disease
     * @param disease
     * @return
     */
    public ResponseEntity<Object> updateDisease(Disease disease){                  // Update user
        try{
            Long id = disease.getId();
            if (diseaseRepository.existsById(id)) {
                disease.setUpdatedDate(SqlDate.getDateInSqlFormat());
                diseaseRepository.save(disease);
                return new ResponseEntity<>("Disease updated thank you", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Disease not exist", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception throws by UpdateDisease method");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete disease from db by using disease ID
     * @param id
     * @return
     */
    public ResponseEntity<Object> deleteDisease(Long id){
        try{
            Optional<Disease> existingDisease= diseaseRepository.findById(id);
            if (existingDisease.isPresent())
            {
                existingDisease.get().setUpdatedDate(SqlDate.getDateInSqlFormat());
                existingDisease.get().setActive(false);
                diseaseRepository.save(existingDisease.get());
                return new ResponseEntity<>(" disease has been Deleted/Inactivated", HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>("disease Not exists Please enter Valid ID", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("disease deleted", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * getting disease by id
     * @param id
     * @return
     */
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

    /**
     * display list of all active diseases
     * @return
     */
    public ResponseEntity<Object> listAllActivediseases() {
        try {
            List<Disease> diseaseList = diseaseRepository.findAllByActive(true);
            if (diseaseList.isEmpty()) {
                return new ResponseEntity<>("There are no Disease in the database", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(diseaseList, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * display list of all inactive diseases
     * @return
     */
    public ResponseEntity<Object> listAllInactiveDiseases(){
        try {
            List<Disease> diseaseList = diseaseRepository.findAllByActive(false);
            if (diseaseList.isEmpty()) {
                return new ResponseEntity<>("There are no Disease in the database", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(diseaseList, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("Exception throws by listAllInactiveDiseases at diseaseService  "+ e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }

}
