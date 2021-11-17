package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Forms.MedicalForm;
import com.app.epbmsystem.model.Forms.ResidentialForm;
import com.app.epbmsystem.repository.ResidentialRepository;
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
public class ResidentialService {
    final ResidentialRepository residentialRepository;
    ResidentialForm residentialForm;

    public ResidentialService(ResidentialRepository residentialRepository) {
        this.residentialRepository = residentialRepository;
    }

    private static final Logger LOG = LogManager.getLogger(ResidentialService.class);

    /**
     * this method returns List of all existing residential application
     * @return
     */
    public ResponseEntity<Object> listAllResidentialFroms(){
        try {
            List<ResidentialForm> residentialFormList= residentialRepository.findAll();
            if (residentialFormList.isEmpty())
            {
                LOG.info("there is no record in the database of residential applications");
                return new ResponseEntity<>("No residentialForm exists in the database", HttpStatus.NOT_FOUND);
            }
            else
            {
                LOG.info("Returning a list of existing residential applications");
                return new ResponseEntity<>(residentialFormList, HttpStatus.OK);
            }
        }
        catch (Exception e){
            LOG.info("Exception throws by method listAllResidentialFroms in service class  ");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * this method adds ResidentialApplication into database
     * @param residentialForm
     * @return
     */
    public ResponseEntity<Object> saveResidentialForm(ResidentialForm residentialForm) {
       try {
           residentialForm.setActive(true);
           residentialForm.setCreatedDate(SqlDate.getDateInSqlFormat());
           residentialForm.setUpdatedDate(SqlDate.getDateInSqlFormat());
           residentialForm.setApplicationStatus("Submitted");
           residentialForm.setRemarks("No remarks");
           residentialRepository.save(residentialForm);
           return new ResponseEntity<>("residential Application Added /n Thank you for adding   ", HttpStatus.OK);
       }
       catch (Exception e)
       {
           LOG.info("Exception throws by method saveResidentialForm in service class ");
           return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
       }
    }

    /**
     * This method updates existing residential from
     * @param residentialForm
     * @return
     */
    public ResponseEntity<Object> updateResidentialForm(ResidentialForm residentialForm){                  // Update user
        try{
            Long id = residentialForm.getId();
            Optional<ResidentialForm> existingForm= residentialRepository.findById(id);
            if (existingForm.isPresent()) {
                LOG.info("residentialform is present by its id ");
                existingForm.get().setUpdatedDate(SqlDate.getDateInSqlFormat());
                residentialRepository.save(residentialForm);
                return new ResponseEntity<>("residentialForm updated thank you", HttpStatus.OK);
            } else {
                LOG.info("No ResidentialFrom found for the id: "+id);
                return new ResponseEntity<>("residentialForm not exist", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e)
        {
            LOG.info("");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * this method set application status false for inactive existing application
     * @param id
     * @return
     */
    public ResponseEntity<Object> deleteResidentialForm(Long id){
        try{
            Optional<ResidentialForm> existingForm= residentialRepository.findById(id);
            if (existingForm.isPresent()) {
                LOG.info("Inactivating residential applications ");
                existingForm.get().setActive(false);
                residentialRepository.save(existingForm.get());
                return new ResponseEntity<>(" residentialForm has been Deleted", HttpStatus.OK);
            } else {
                LOG.info("Application do not exists, Please Enter valid ID");
                return new ResponseEntity<>("residentialForm Not exists Please enter Valid ID", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            LOG.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("Exception", HttpStatus.BAD_REQUEST);}
    }

    /**
     * This method returns a application by entering its id
     * @param id
     * @return
     */
    public ResponseEntity<Object> getResidentialForm(Long id){
        try{
            Optional<ResidentialForm> residentialForm = residentialRepository.findById(id);
            if (residentialForm.isPresent())
            {
                LOG.info("Residential application returns");
                return new ResponseEntity<>(residentialForm, HttpStatus.FOUND); }
            else
            {return new ResponseEntity<>(residentialForm, HttpStatus.NOT_FOUND); }
        }
        catch (Exception exception)
        {
            LOG.info("Exception throws by method getResidentialForm  ");
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
    }

    /**
     * display list of all active Residential applications
     * @return
     */
    public ResponseEntity<Object> listAllActive() {
        try {
            List<ResidentialForm> existingForms = residentialRepository.findAllByActive(true);
            if (existingForms.isEmpty()) {
                LOG.info("Application exists in the database");
                return new ResponseEntity<>("There are no Residential application in the database", HttpStatus.NOT_FOUND);
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
     * display list of all inactive Residential applications
     * @return
     */
    public ResponseEntity<Object> listAllInactive(){
        try {
            List<ResidentialForm> existingForms = residentialRepository.findAllByActive(false);
            if (existingForms.isEmpty()) {
                LOG.info("Application does not exist in the database");
                return new ResponseEntity<>("There are no Residential applications in the database", HttpStatus.NOT_FOUND);
            } else {
                LOG.info("Application exists in the database. Returning it to the controller");
                return new ResponseEntity<>(existingForms, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("Exception throws by listAllInactive Residential applications at medicalService  "+ e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
            List<ResidentialForm> existingForms = residentialRepository.findByCreatedDate(date);
            if (existingForms.isEmpty()) {
                return new ResponseEntity<>("Data not found for the entered date in database.", HttpStatus.NOT_FOUND);
            } else {
                LOG.info("List of Residential application: Sorted by date ");
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
            List<ResidentialForm> existingForms = residentialRepository.findByStartDate(date);
            if (existingForms.isEmpty()) {
                return new ResponseEntity<>("Data not found for the entered date in database.", HttpStatus.NOT_FOUND);
            } else {
                LOG.info("List of residential application: Sorted by date ");
                return new ResponseEntity<>(existingForms, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("error in search by Start date in class residentialService :"+e.getMessage() + e.getCause());

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
            List<ResidentialForm> existingForms =residentialRepository.findByCreatedDateBetweenOrderByUpdatedDateAsc(startDate,endDate);
            if (existingForms.isEmpty()) {
                return new ResponseEntity<>("Data not found for the entered date in database.", HttpStatus.NOT_FOUND);
            } else {
                LOG.info("List of Residential application: Sorted by date ");
                return new ResponseEntity<>(existingForms, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("Method throws exception :"+e.getMessage() + e.getCause());
            return new ResponseEntity<Object>("An error occured ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
