package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Forms.EducationalForm;
import com.app.epbmsystem.model.Forms.MedicalForm;
import com.app.epbmsystem.model.Forms.ResidentialForm;
import com.app.epbmsystem.repository.ResidentialRepository;
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
    public ResponseEntity<Object> listAllResidentialFroms() throws ParseException {
        try {
            List<ResidentialForm> residentialFormList= residentialRepository.findAll();
            if (residentialFormList.isEmpty())
            {
                LOG.info("no record of residential applications");
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"No residentialForm exists in the database",null);
            }
            else
            {
                LOG.info("");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"Returning a list of existing residential applications",null);
            }
        }
        catch (Exception e){
            LOG.info("Exception:  " +e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * this method adds ResidentialApplication into database
     * @param residentialForm
     * @return
     */
    public ResponseEntity<Object> saveResidentialForm(ResidentialForm residentialForm) throws ParseException {
       try {
           residentialForm.setActive(true);
           residentialForm.setCreatedDate(SqlDate.getDateInSqlFormat());
           residentialForm.setUpdatedDate(SqlDate.getDateInSqlFormat());
           residentialForm.setApplicationStatus("Submitted");
           residentialForm.setRemarks("No remarks");
           residentialRepository.save(residentialForm);
           return ResponseHandler.generateResponse(HttpStatus.OK,false,"added thank you",null);
       }
       catch (Exception e)
       {
           LOG.info("Exception throws by method saveResidentialForm in service class ");
           return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
       }
    }

    /**
     * This method updates existing residential from
     * @param residentialForm
     * @return
     */
    public ResponseEntity<Object> updateResidentialForm(ResidentialForm residentialForm) throws ParseException {                  // Update user
        try{
            Long id = residentialForm.getId();
            Optional<ResidentialForm> existingForm= residentialRepository.findById(id);
            if (existingForm.isPresent()) {
                LOG.info("residentialform is present by its id ");
                existingForm.get().setUpdatedDate(SqlDate.getDateInSqlFormat());
                residentialRepository.save(residentialForm);
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"residentialForm updated ",null);
            } else {
                LOG.info("No ResidentialFrom found for the id: "+id);
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"residentialForm not exist",null);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception: "+e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * this method set application status false for inactive existing application
     * @param id
     * @return
     */
    public ResponseEntity<Object> deleteResidentialForm(Long id) throws ParseException {
        try{
            Optional<ResidentialForm> existingForm= residentialRepository.findById(id);
            if (existingForm.isPresent()) {
                LOG.info("Inactivating residential applications ");
                existingForm.get().setActive(false);
                residentialRepository.save(existingForm.get());
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"Form has been Deleted",null);
            } else {
                LOG.info("Application do not exists, Please Enter valid ID");
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Form Not exists Please enter Valid ID",null);
            }
        }
        catch (Exception e){
            LOG.info("Exception: " + e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null); }
    }

    /**
     * This method returns a application by entering its id
     * @param id
     * @return
     */
    public ResponseEntity<Object> getResidentialForm(Long id) throws ParseException {
        try{
            Optional<ResidentialForm> residentialForm = residentialRepository.findById(id);
            if (residentialForm.isPresent())
            {
                LOG.info("Residential application returns");
            return ResponseHandler.generateResponse(HttpStatus.FOUND,false,"Form exists by id: "+id,null);}
            else
            {
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"forms do not exists by id: "+id,null);}
        }
        catch (Exception exception)
        {
            LOG.info("Exception throws by method getResidentialForm  ");
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+exception.getMessage(),null);
            }
    }

    /**
     * display list of all active Residential applications
     * @return
     */
    public ResponseEntity<Object> listAllActive() throws ParseException {
        try {
            List<ResidentialForm> existingForms = residentialRepository.findAllByActive(true);
            if (existingForms.isEmpty()) {
                LOG.info("Application exists in the database");
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"forms not found. ",null);
            } else {
                LOG.info("Application does not exist in the database");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"Exception",null);            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * display list of all inactive Residential applications
     * @return
     */
    public ResponseEntity<Object> listAllInactive() throws ParseException {
        try {
            List<ResidentialForm> existingForms = residentialRepository.findAllByActive(false);
            if (existingForms.isEmpty()) {
                LOG.info("Application does not exist in the database");
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Forms not exists",null);
            } else {
                LOG.info("Application exists in the database. Returning it to the controller");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of Inactive forms",existingForms);
            }
        } catch (Exception e) {
            LOG.info("Exception throws by listAllInactive Residential applications at medicalService  "+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exceptions: "+e.getMessage(),null);

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
            List<ResidentialForm> existingForms = residentialRepository.findByCreatedDate(date);
            if (existingForms.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Data not found for the entered date in database",null);
            } else {
                LOG.info("List of Residential application: Sorted by date ");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of Residential application: Sorted by date",existingForms);
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
            List<ResidentialForm> existingForms = residentialRepository.findByStartDate(date);
            if (existingForms.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Data not found for the entered date in database",null);
            } else {
                LOG.info("List of residential application: Sorted by date ");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of residential application: Sorted by start date",existingForms);
            }
        } catch (Exception e) {
            LOG.info("error in search by Start date in class residentialService :"+e.getMessage() + e.getCause());


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
            List<ResidentialForm> existingForms =residentialRepository.findByCreatedDateBetweenOrderByUpdatedDateAsc(startDate,endDate);
            if (existingForms.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Data not found for the entered date in database",null);
            } else {
                LOG.info("List of Residential application: Sorted by date ");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of Residential application: Sorted by start to end date",existingForms);
            }
        } catch (Exception e) {
            LOG.info("Method throws exception :"+e.getMessage() + e.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);

        }
    }

    /**
     * Returns a list of Forms of specific user
     * @param id
     * @return
     */
    public ResponseEntity<Object> ListOfUserResidentialForms(Long id) throws ParseException {
        try {
            List<ResidentialForm> existingForm = residentialRepository.findResidentialFormsByUserId(id);
            if (existingForm.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no application forms for the entered user ID",null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of Residential Forms",existingForm);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception: "+e.getMessage()+ "Cause"+e.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * returns a List of Forms by specific application Status
     * @param applicationStatus
     * @return
     */
    public ResponseEntity<Object> ListOfResidentialFormsByApplicationStatus(String applicationStatus) throws ParseException {
        try{
            List<ResidentialForm> existingForms= residentialRepository.findResidentialFormsByApplicationStatus(applicationStatus);
            if (existingForms.isEmpty())
            {
                LOG.info("There is no "+applicationStatus+" forms exists",HttpStatus.NO_CONTENT);
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Data not found",null);
            }
            else
            {
                LOG.info("Status: In review Forms exists");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of forms by status",existingForms);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception throws by method(ResidentialFormsByApplicationStatus) "+e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception"+e.getMessage(),null);
        }
    }


}
