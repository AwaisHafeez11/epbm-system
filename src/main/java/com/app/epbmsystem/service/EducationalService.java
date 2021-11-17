package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Forms.EducationalForm;
import com.app.epbmsystem.model.Forms.MedicalForm;
import com.app.epbmsystem.model.Forms.ResidentialForm;
import com.app.epbmsystem.repository.EducationalRepository;
import com.app.epbmsystem.util.SqlDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Optional;

@Service
public class EducationalService {
    final EducationalRepository educationalRepository;
    EducationalForm educationalForm;

    /**
     * autowiring throygh constructor
     * @param educationalRepository
     */
    public EducationalService(EducationalRepository educationalRepository) {
        this.educationalRepository = educationalRepository;
    }

    private static final Logger LOG = LogManager.getLogger(EducationalService.class);

    /**
     * List of all Educational Forms
     * @return
     */
    public ResponseEntity<Object> listAllEducationalFroms() {
        try {
            List<EducationalForm> educationalFormList = educationalRepository.findAll();
            if (educationalFormList.isEmpty()) {
                return new ResponseEntity<>("No EducationalForm exists in the database", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(educationalFormList, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Adding Educational form into datebase
     * @param educationalForm
     * @return
     */
    public ResponseEntity<Object> saveEducationalForm(EducationalForm educationalForm) {
        try {

            educationalForm.setCreatedDate(SqlDate.getDateInSqlFormat());
            educationalForm.setUpdatedDate(SqlDate.getDateInSqlFormat());
            educationalForm.setActive(true);
            educationalForm.setApplicationStatus("InReview");

            educationalRepository.save(educationalForm);

            return new ResponseEntity<>("Educational Application Added /n Thank you for adding   ", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<Object>("An error occured ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update Educational form by this method
     * @param educationalForm
     * @return
     */
    public ResponseEntity<Object> updateEducationalForm(EducationalForm educationalForm) {
        try {
            Long id = educationalForm.getId();
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            if (educationalRepository.existsById(id)) {
                educationalRepository.save(educationalForm);
                return new ResponseEntity<>("EducationalForm updated thank you", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("EducationalForm not exist", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * this method deletes Educational forms by its id
     * @param id
     * @return
     */
    public ResponseEntity<Object> deleteEducationalForm(Long id) {                   //Financialform deleted
        try {
            if (educationalRepository.existsById(id)) {
                educationalRepository.delete(educationalForm);
                return new ResponseEntity<>(" EducationalForm has been Deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("EducationalForm Not exists Please enter Valid ID", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            LOG.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("EducationalForm deleted", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get educational form by its id
     * @param id
     * @return
     */
    public ResponseEntity<Object> getEducationalForm(Long id) {
        try {
            Optional<EducationalForm> educationalForm = educationalRepository.findById(id);
            if (educationalForm.isPresent()) {
                return new ResponseEntity<>(educationalForm, HttpStatus.FOUND);
            } else {
                return new ResponseEntity<>(educationalForm, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This method returns a list of forms w.r.t to specific date
     * @param date
     * @return
     */
    public ResponseEntity<Object> searchByDate(Date date) {
        try {
            LOG.info("Checking Weather data is present or not");
            List<EducationalForm> existingForms = educationalRepository.findByCreatedDate(date);
            if (existingForms.isEmpty()) {
                return new ResponseEntity<>("Data not found for the entered date in database.", HttpStatus.NOT_FOUND);
            } else {
                LOG.info("List of educational application: Sorted by date ");
                return new ResponseEntity<>(existingForms, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("error in searchbydate in class educationalService :"+e.getMessage() + e.getCause());

            return new ResponseEntity<Object>("An error occured ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * this method returns a list of forms from start date to the end of existing fields
     * @param date
     * @return
     */
    public ResponseEntity<Object> searchByStartDate(Date date) {
        try {
            LOG.info("Checking Weather data is present or not");
            List<EducationalForm> existingForms = educationalRepository.findByStartDate(date);
            if (existingForms.isEmpty()) {
                return new ResponseEntity<>("Data not found for the entered date in database.", HttpStatus.NOT_FOUND);
            } else {
                LOG.info("List of educational application: Sorted by date ");
                return new ResponseEntity<>(existingForms, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("error in searchbydate in class educationalService :"+e.getMessage() + e.getCause());

            return new ResponseEntity<Object>("An error occured ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * this method returns a list w.r.t start and end date
     * @param startDate
     * @param endDate
     * @return
     */
    public ResponseEntity<Object> searchByStartDateAndEndDate(Date startDate,Date endDate) {
        try {
            LOG.info("Checking Weather data is present or not between two dates");
            List<EducationalForm> existingForms = educationalRepository.findByCreatedDateBetweenOrderByUpdatedDateAsc(startDate,endDate);
            if (existingForms.isEmpty()) {
                return new ResponseEntity<>("Data not found for the entered date in database.", HttpStatus.NOT_FOUND);
            } else {
                LOG.info("List of educational application: Sorted by date ");
                return new ResponseEntity<>(existingForms, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("error in searchbydate in class educationalService :"+e.getMessage() + e.getCause());
            return new ResponseEntity<Object>("An error occured ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * display list of all active Educational applications
     * @return
     */
    public ResponseEntity<Object> listAllActive() {
        try {
            List<EducationalForm> existingForms = educationalRepository.findAllByActive(true);
            if (existingForms.isEmpty()) {
                LOG.info("Application exists in the database");
                return new ResponseEntity<>("There are no Educational application in the database", HttpStatus.NOT_FOUND);
            } else {
                LOG.info("Application does not exist in the database");
                return new ResponseEntity<>(existingForms, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * display list of all inactive Educational applications
     * @return
     */
    public ResponseEntity<Object> listAllInactive(){
        try {
            List<EducationalForm> existingForms = educationalRepository.findAllByActive(false);
            if (existingForms.isEmpty()) {
                LOG.info("Application does not exist in the database");
                return new ResponseEntity<>("There are no Educational applications in the database", HttpStatus.NOT_FOUND);
            } else {
                LOG.info("Application exists in the database. Returning it to the controller");
                return new ResponseEntity<>(existingForms, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("Exception throws by listAllInactive Educational applications at EducationalService  "+ e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }

    /**
     * Returns a list of Forms of specific user
     * @param id
     * @return
     */
    public ResponseEntity<Object> ListOfUserEducationalForms(Long id){
        try {
            List<EducationalForm> existingForm = educationalRepository.findEducationalFormsByUserId(id);
            if (existingForm.isEmpty()) {
                return new ResponseEntity<>("There are no application forms for the entered user ID", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(existingForm, HttpStatus.OK);
            }
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * returns a List of Forms by specific application Status
     * @param applicationStatus
     * @return
     */
    public ResponseEntity<Object> ListOfEducationalFormsByApplicationStatus(String applicationStatus)
    {
        try{
            List<EducationalForm> existingForms= educationalRepository.findEducationalFormsByApplicationStatus(applicationStatus);
            if (existingForms.isEmpty())
            {
                LOG.info("There is no In review forms exists",HttpStatus.NO_CONTENT);
                return new ResponseEntity<>("There is no In review forms exists",HttpStatus.NO_CONTENT);
            }
            else
            {
                LOG.info("Status: In review Forms exists");
                return new ResponseEntity<>("Status: In review forms exists" + existingForms,HttpStatus.OK);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception throws by method(ListOfMedicalFormsByApplicationStatus) "+e.getMessage());
            return new ResponseEntity<>("Exception"+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


}