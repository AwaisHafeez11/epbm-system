package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Forms.Disease;
import com.app.epbmsystem.model.Forms.EducationalForm;
import com.app.epbmsystem.model.Forms.FinancialForm;
import com.app.epbmsystem.model.Forms.MedicalForm;
import com.app.epbmsystem.repository.MedicalRepository;
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
    public ResponseEntity<Object> listAllMedicalFroms(){   // List of all Medical applications
        try {
            List<MedicalForm> medicalForms= medicalRepository.findAll();
            if (medicalForms.isEmpty())
            {
                LOG.info("there is no list of medical forms exists till now");
                return new ResponseEntity<>("No MedicalForm exists in the database", HttpStatus.NOT_FOUND);
            }
            else
            {
                LOG.info("Returning list of medicalForms to the controller from service");
                return new ResponseEntity<>(medicalForms, HttpStatus.OK);
            }
        }
        catch (Exception e){
            LOG.info("listAllMedicalFroms Throws exception Internal server Error");
             return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * this method will add application into the medicalforms
     * @param medicalForm
     * @return
     */
    public ResponseEntity<Object> saveMedicalForm(MedicalForm medicalForm) {
        try
        {
            medicalForm.setCreatedDate(SqlDate.getDateInSqlFormat());
            medicalForm.setUpdatedDate(SqlDate.getDateInSqlFormat());
            medicalForm.setActive(true);
            medicalForm.setApplicationStatus("In review");
                        medicalRepository.save(medicalForm);
            return new ResponseEntity<>("Medical Application Added, Thank you for adding   ",HttpStatus.OK);
        } catch (ParseException e) {
            LOG.info("An exception throws by saveMedicalForm in the service method  ");
            e.printStackTrace();
        }
       return new ResponseEntity<>("",HttpStatus.OK);
    }

    /**
     * this method will update medical application
     * @param medicalForm
     * @return
     */
    public ResponseEntity<Object> updateMedicalForm(MedicalForm medicalForm){                  // Update user
        try{
            Long id = medicalForm.getId();


            if (medicalRepository.existsById(id)) {
                medicalForm.setUpdatedDate(SqlDate.getDateInSqlFormat());
                medicalRepository.save(medicalForm);
                LOG.info("Medical Application updated");
                return new ResponseEntity<>("medicalForm updated thank you", HttpStatus.OK);
            } else {

                return new ResponseEntity<>("medicalForm not exist", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e)
        {
            LOG.info("An exception throws by the method(updateMedicalForm) in Medical service ");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * this method will delete application
     * @param id
     * @return
     */
    public ResponseEntity<Object> deleteMedicalForm(Long id){
        try{
            Optional<MedicalForm> existingForm= medicalRepository.findById(id);
            if (existingForm.isPresent()) {
                existingForm.get().setActive(false);
                medicalRepository.save(existingForm.get());
                return new ResponseEntity<>(" MedicalForm has been Deleted/Inactive", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("MedicalForm Not exists Please enter Valid ID", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            LOG.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("Exception ", HttpStatus.BAD_REQUEST);}
    }

    /**
     * this method returns a object by its id
     * @param id
     * @return
     */
    public ResponseEntity<Object> getMedicalForm(Long id){
        try{
            Optional<MedicalForm> medicalForm = medicalRepository.findById(id);
            if (medicalForm.isPresent())
            {
                LOG.info("medical form exist for this: "+ id);
                return new ResponseEntity<>(medicalForm, HttpStatus.FOUND);
            }
            else
            {
                LOG.info("there is no medical form exist for this: "+ id);
                return new ResponseEntity<>(medicalForm, HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception exception)
            {
                LOG.info("An exception throws by getMedicalForm in MedicalService class ");
                return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
    }

    /**
     * This method returns a list of forms w.r.t to specific date
     * @param date
     * @return
     */
    public ResponseEntity<Object> searchByDate(Date date) {
        try {
            LOG.info("Checking Weather data is present or not");
            List<MedicalForm> existingForms = medicalRepository.findByCreatedDate(date);
            if (existingForms.isEmpty()) {
                return new ResponseEntity<>("Data not found for the entered date in database.", HttpStatus.NOT_FOUND);
            } else {
                LOG.info("List of educational application: Sorted by date ");
                return new ResponseEntity<>(existingForms, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("error in searchbydate in class MedicalService :"+e.getMessage() + e.getCause());

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
            List<MedicalForm> existingForms = medicalRepository.findByStartDate(date);
            if (existingForms.isEmpty()) {
                return new ResponseEntity<>("Data not found for the entered date in database.", HttpStatus.NOT_FOUND);
            } else {
                LOG.info("List of Medical application: Sorted by date ");
                return new ResponseEntity<>(existingForms, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("error in searchbydate in class eMedicallService :"+e.getMessage() + e.getCause());

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
            List<MedicalForm> existingForms =medicalRepository.findByCreatedDateBetweenOrderByUpdatedDateAsc(startDate,endDate);
            if (existingForms.isEmpty()) {
                return new ResponseEntity<>("Data not found for the entered date in database.", HttpStatus.NOT_FOUND);
            } else {
                LOG.info("List of Medical application: Sorted by date ");
                return new ResponseEntity<>(existingForms, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("error in searchbydate in class MedicallService :"+e.getMessage() + e.getCause());
            return new ResponseEntity<Object>("An error occured ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * display list of all active Medical applications
     * @return
     */
    public ResponseEntity<Object> listAllActive() {
        try {
            List<MedicalForm> existingForms = medicalRepository.findAllByActive(true);
            if (existingForms.isEmpty()) {
                return new ResponseEntity<>("There are no Medical application in the database", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(existingForms, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * display list of all inactive Medical applications
     * @return
     */
    public ResponseEntity<Object> listAllInactive(){
        try {
            List<MedicalForm> existingForms = medicalRepository.findAllByActive(false);
            if (existingForms.isEmpty()) {
                return new ResponseEntity<>("There are no medical applications in the database", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(existingForms, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("Exception throws by listAllInactive medical applications at medicalService  "+ e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }

    /**
     * Returns a list of Forms of specific user
     * @param id
     * @return
     */
    public ResponseEntity<Object> ListOfUserMedicalForms(Long id){
        try {
            List<MedicalForm> existingForm = medicalRepository.findMedicalFormsByUserId(id);

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
    public ResponseEntity<Object> ListOfMedicalFormsByApplicationStatus(String applicationStatus)
    {
        try{
            List<MedicalForm> existingForms= medicalRepository.findMedicalFormsByApplicationStatus(applicationStatus);
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
