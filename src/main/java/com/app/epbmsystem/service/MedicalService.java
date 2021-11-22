package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Forms.Disease;
import com.app.epbmsystem.model.Forms.EducationalForm;
import com.app.epbmsystem.model.Forms.FinancialForm;
import com.app.epbmsystem.model.Forms.MedicalForm;
import com.app.epbmsystem.repository.MedicalRepository;
import com.app.epbmsystem.util.ResponseHandler;
import com.app.epbmsystem.util.SqlDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
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

    /**
     *this method list of all medical forms
     * @return
     */
    public ResponseEntity<Object> listAllMedicalFroms() throws ParseException {   // List of all Medical applications
        try {
            List<MedicalForm> medicalForms= medicalRepository.findAll();
            if (medicalForms.isEmpty())
            {
                LOG.info("there is no list of medical forms exists till now");
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"No MedicalForm exists in the database",null);
            }
            else
            {
                LOG.info("Returning list of medicalForms to the controller from service");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of medical forms",medicalForms);
            }
        }
        catch (Exception e){
            LOG.info("listAllMedicalFroms Throws exception Internal server Error");
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);

        }
    }

    /**
     * this method will add application into the medicalforms
     * @param medicalForm
     * @return
     */
    public ResponseEntity<Object> saveMedicalForm(MedicalForm medicalForm) throws ParseException {
        try
        {
            medicalForm.setCreatedDate(SqlDate.getDateInSqlFormat());
            medicalForm.setUpdatedDate(SqlDate.getDateInSqlFormat());
            medicalForm.setActive(true);
            medicalForm.setApplicationStatus("In review");
                        medicalRepository.save(medicalForm);
            return ResponseHandler.generateResponse(HttpStatus.OK,false,"Medical Application Added",null);
        }
        catch (ParseException e) {
            LOG.info("An exception throws by saveMedicalForm in the service method  ");
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * this method will update medical application
     * @param medicalForm
     * @return
     */
    public ResponseEntity<Object> updateMedicalForm(MedicalForm medicalForm) throws ParseException {                  // Update user
        try{
            Long id = medicalForm.getId();


            if (medicalRepository.existsById(id)) {
                medicalForm.setUpdatedDate(SqlDate.getDateInSqlFormat());
                medicalRepository.save(medicalForm);
                LOG.info("Medical Application updated");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"medicalForm updated thank you",null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Data you are trying to update doesn't exists",null);
            }
        }
        catch (Exception e)
        {
            LOG.info("An exception throws by the method(updateMedicalForm) in Medical service ");
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception"+e.getMessage(),null);
        }
    }

    /**
     * this method will delete application
     * @param id
     * @return
     */
    public ResponseEntity<Object> deleteMedicalForm(Long id) throws ParseException {
        try{
            Optional<MedicalForm> existingForm= medicalRepository.findById(id);
            if (existingForm.isPresent()) {
                existingForm.get().setActive(false);
                medicalRepository.save(existingForm.get());
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"Inactivated form for the id"+id,null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Not Found any data for the id: "+id,null);
            }
        }
        catch (Exception e){
            LOG.info("Exception: " + e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
           }
    }

    /**
     * this method returns a object by its id
     * @param id
     * @return
     */
    public ResponseEntity<Object> getMedicalForm(Long id) throws ParseException {
        try{
            Optional<MedicalForm> medicalForm = medicalRepository.findById(id);
            if (medicalForm.isPresent())
            {
                LOG.info("medical form exist for this: "+ id);
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"medical form exist for this:",medicalForm);
            }
            else
            {
                LOG.info("there is no medical form exist for this: "+ id);
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Data not exists by id: "+id,null);
            }
        }
        catch (Exception exception)
            {
                LOG.info("An exception throws by getMedicalForm in MedicalService class ");
                return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+exception.getMessage(),null);}
    }

    /**
     * This method returns a list of forms w.r.t to specific date
     * @param date
     * @return
     */
    public ResponseEntity<Object> searchByDate(Date date) throws ParseException {
        try {
            LOG.info("Checking Weather data is present or not");
            List<MedicalForm> existingForms = medicalRepository.findByCreatedDate(date);
            if (existingForms.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Data not found for the entered date in database",null);
            } else {
                LOG.info("List of educational application: Sorted by date ");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of forms: Sorted by date",existingForms);
            }
        } catch (Exception e) {
            LOG.info("error in searchbydate in class MedicalService :"+e.getMessage() + e.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
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
            List<MedicalForm> existingForms = medicalRepository.findByStartDate(date);
            if (existingForms.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Data not found for the entered date in database",null);
            } else {
                LOG.info("List of Medical application: Sorted start by date ");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of Medical application: Sorted by start date",existingForms);
            }
        } catch (Exception e) {
            LOG.info("error in searchbydate in class eMedicallService :"+e.getMessage() + e.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
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
            List<MedicalForm> existingForms =medicalRepository.findByCreatedDateBetweenOrderByUpdatedDateAsc(startDate,endDate);
            if (existingForms.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Data not found for the entered date in database",null);
            } else {
                LOG.info("List of Medical application: Sorted by date ");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of forms: Sorted by start to end date",existingForms);
            }
        } catch (Exception e) {
            LOG.info("error in searchbydate in class MedicallService :"+e.getMessage() + e.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Excepption: "+e.getMessage(),null);
        }
    }

    /**
     * display list of all active Medical applications
     * @return
     */
    public ResponseEntity<Object> listAllActive() throws ParseException {
        try {
            List<MedicalForm> existingForms = medicalRepository.findAllByActive(true);
            if (existingForms.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no Medical application in the database",null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of active forms",existingForms);
            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * display list of all inactive Medical applications
     * @return
     */
    public ResponseEntity<Object> listAllInactive() throws ParseException {
        try {
            List<MedicalForm> existingForms = medicalRepository.findAllByActive(false);
            if (existingForms.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no medical applications in the database",null);

            } else {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of Inactive forms",existingForms);
            }
        } catch (Exception e) {
            LOG.info("Exception throws by listAllInactive medical applications at medicalService  "+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }



    }

    /**
     * Returns a list of Forms of specific user
     * @param id
     * @return
     */
    public ResponseEntity<Object> ListOfUserMedicalForms(Long id) throws ParseException {
        try {
            List<MedicalForm> existingForm = medicalRepository.findMedicalFormsByUserId(id);

            if (existingForm.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no application forms for the entered user ID",null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of forms ",existingForm);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception: "+e.getMessage()+"   Cause"+e.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * returns a List of Forms by specific application Status
     * @param applicationStatus
     * @return
     */
    public ResponseEntity<Object> ListOfMedicalFormsByApplicationStatus(String applicationStatus) throws ParseException {
        try{
            List<MedicalForm> existingForms= medicalRepository.findMedicalFormsByApplicationStatus(applicationStatus);
            if (existingForms.isEmpty())
            {
                LOG.info("There is no In review forms exists",HttpStatus.NO_CONTENT);
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There is no In review forms exists",null);
            }
            else
            {
                LOG.info("Status: In review Forms exists");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"Status: In review forms exists",existingForms);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception throws by method(ListOfMedicalFormsByApplicationStatus) "+e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+ e.getCause(),null);
        }
    }
}
