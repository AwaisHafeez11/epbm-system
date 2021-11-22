package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Forms.EducationalForm;
import com.app.epbmsystem.repository.EducationalRepository;
import com.app.epbmsystem.util.ResponseHandler;
import com.app.epbmsystem.util.SqlDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.text.ParseException;

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
    public ResponseEntity<Object> listAllEducationalFroms() throws ParseException {
        try {
            List<EducationalForm> educationalFormList = educationalRepository.findAll();
            if (educationalFormList.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"No EducationalForm exists in the database",null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of forms",educationalFormList);
            }
        } catch (Exception e) {
            LOG.info("Exception: "+e.getMessage()+"  Cause: "+e.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage()+"   Cause:"+e.getCause(),null);
        }
    }

    /**
     * Adding Educational form into datebase
     * @param educationalForm
     * @return
     */
    public ResponseEntity<Object> saveEducationalForm(EducationalForm educationalForm) throws ParseException {
        try {

            educationalForm.setCreatedDate(SqlDate.getDateInSqlFormat());
            educationalForm.setUpdatedDate(SqlDate.getDateInSqlFormat());
            educationalForm.setActive(true);
            educationalForm.setApplicationStatus("InReview");

            educationalRepository.save(educationalForm);
            return ResponseHandler.generateResponse(HttpStatus.OK,false,"Added! Thank you",null);
        }
        catch (Exception e) {
            LOG.info("Exception: "+e.getMessage()+"  cause: "+e.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * Update Educational form by this method
     * @param educationalForm
     * @return
     */
    public ResponseEntity<Object> updateEducationalForm(EducationalForm educationalForm) throws ParseException {
        try {
            Long id = educationalForm.getId();
            if (educationalRepository.existsById(id)) {
                educationalRepository.save(educationalForm);
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"EducationalForm updated thank you",null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"EducationalForm not exist",null);
            }
        } catch (Exception e) {
            LOG.info("Exception: "+e.getMessage()+"  cause: "+e.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage()+"  cause: "+e.getCause(),null);
        }
    }

    /**
     * this method deletes Educational forms by its id
     * @param id
     * @return
     */
    public ResponseEntity<Object> deleteEducationalForm(Long id) throws ParseException {                   //Financialform deleted
        try {
            if (educationalRepository.existsById(id)) {
                educationalRepository.delete(educationalForm);
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"EducationalForm has been Deleted/Inactivated",null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"EducationalForm Not exists Please enter Valid ID",null);
            }
        } catch (Exception e) {
            LOG.info("Exception: " + e.getMessage()+"   cause: "+e.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage()+"  cause: "+e.getCause(),null);
        }
    }

    /**
     * Get educational form by its id
     * @param id
     * @return
     */
    public ResponseEntity<Object> getEducationalForm(Long id) throws ParseException {
        try {
            Optional<EducationalForm> educationalForm = educationalRepository.findById(id);
            if (educationalForm.isPresent()) {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"Form by id",educationalForm);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Form not exists",null);
            }
        } catch (Exception exception) {
            LOG.info("exception: "+exception.getMessage()+"   cause: "+exception.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+exception.getMessage()+"  Cause: "+exception.getCause(),null);
        }
    }

    /**
     * This method returns a list of forms w.r.t to specific date
     * @param date
     * @return
     */
    public ResponseEntity<Object> searchByDate(Date date) throws ParseException {
        try {
            LOG.info("Checking Weather data is present or not");
            List<EducationalForm> existingForms = educationalRepository.findByCreatedDate(date);
            if (existingForms.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Data not found for the entered date in database",null);
            } else {
                LOG.info("List of educational application: Sorted by date ");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of educational application: Sorted by date",existingForms);
            }
        } catch (Exception e) {
            LOG.info("error in searchbydate in class educationalService :"+e.getMessage() + e.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage()+"    cause: "+e.getCause(),null);
        }
    }

    /**
     * this method returns a list of forms from start date to the end of existing fields
     * @param date
     * @return
     */
    public ResponseEntity<Object> searchByStartDate(Date date) throws ParseException {
        try {
            LOG.info("Checking Weather data is present or not");
            List<EducationalForm> existingForms = educationalRepository.findByStartDate(date);
            if (existingForms.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Data not found for the entered date in database",null);
            } else {
                LOG.info("List of educational application: Sorted by date ");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"Searched by start date: ",existingForms);
            }
        } catch (Exception e) {
            LOG.info("Exception :"+e.getMessage() + e.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage()+"   cause: "+e.getCause(),null);
        }
    }

    /**
     * this method returns a list w.r.t start and end date
     * @param startDate
     * @param endDate
     * @return
     */
    public ResponseEntity<Object> searchByStartDateAndEndDate(Date startDate,Date endDate) throws ParseException {
        try {
            LOG.info("Checking Weather data is present or not between two dates");
            List<EducationalForm> existingForms = educationalRepository.findByCreatedDateBetweenOrderByUpdatedDateAsc(startDate,endDate);
            if (existingForms.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Data not found for the entered date in database",null);
            } else {
                LOG.info("List of educational application: Sorted by date ");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of educational application: Sorted by start to end date",existingForms);
            }
        } catch (Exception e) {
            LOG.info("error in searchbydate in class educationalService :"+e.getMessage() + e.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage()+"   cause:"+e.getCause(),null);
        }
    }

    /**
     * display list of all active Educational applications
     * @return
     */
    public ResponseEntity<Object> listAllActive() throws ParseException {
        try {
            List<EducationalForm> existingForms = educationalRepository.findAllByActive(true);
            if (existingForms.isEmpty()) {
                LOG.info("Application does not exists in the database");
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no Educational application in the database",null);
            } else {
                LOG.info("Application exist in the database");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of active forms",existingForms);
            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage()+"   Cause;"+e.getCause(),null);
        }
    }

    /**
     * display list of all inactive Educational applications
     * @return
     */
    public ResponseEntity<Object> listAllInactive() throws ParseException {
        try {
            List<EducationalForm> existingForms = educationalRepository.findAllByActive(false);
            if (existingForms.isEmpty()) {
                LOG.info("Application does not exist in the database");
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no Educational applications in the database",null);
            } else {
                LOG.info("Application exists in the database. Returning it to the controller");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of inactive forms",existingForms);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception throws by listAllInactive Educational applications at EducationalService  "+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage()+"  cause: "+e.getCause(),null);
        }
    }

    /**
     * Returns a list of Forms of specific user
     * @param id
     * @return
     */
    public ResponseEntity<Object> ListOfUserEducationalForms(Long id) throws ParseException {
        try {
            List<EducationalForm> existingForm = educationalRepository.findEducationalFormsByUserId(id);
            if (existingForm.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no application forms for the entered user ID",null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"list of user forms",existingForm);
            }
        }
        catch (Exception e)
        { LOG.info("Exception: "+e.getMessage()+"   cause: "+e.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage()+"  cause: "+e.getCause(),null);
        }
    }

    /**
     * returns a List of Forms by specific application Status
     * @param applicationStatus
     * @return
     */
    public ResponseEntity<Object> ListOfEducationalFormsByApplicationStatus(String applicationStatus) throws ParseException {
        try{
            List<EducationalForm> existingForms= educationalRepository.findEducationalFormsByApplicationStatus(applicationStatus);
            if (existingForms.isEmpty())
            {
                LOG.info("There is no In review forms exists",HttpStatus.NO_CONTENT);
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There is no In review forms exists",null);
            }
            else
            {
                LOG.info("Status: In review Forms exists");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of forms by status",existingForms);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception throws by method(ListOfMedicalFormsByApplicationStatus) "+e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage()+"    Cause: "+e.getCause(),null);
        }
    }


}