package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Forms.FinancialForm;
import com.app.epbmsystem.repository.FinancialRepository;
import com.app.epbmsystem.util.ResponseHandler;
import com.app.epbmsystem.util.SqlDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FinancialService {
    @Autowired
    final FinancialRepository financialRepository;
    FinancialForm financialForm;

    public FinancialService(FinancialRepository financialRepository) {
        this.financialRepository = financialRepository;
    }
    private static final Logger LOG = LogManager.getLogger(FinancialService.class);

    /**
     * List of all financialForms
     * @return
     * @throws ParseException
     */
    public ResponseEntity<Object> listAllFinancialFroms() throws ParseException {   // List of all financial applications
        try {
                List<FinancialForm> financialFormList= financialRepository.findAll();
                if (financialFormList.isEmpty())
                {
                    return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"No FinancialForm exists in the database",null);
                }
                else
                {
                    return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of forms",financialFormList);
                }
        }
        catch (Exception e){
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * Save a form into the database
     * @param financialForm
     * @return
     * @throws ParseException
     */
    public ResponseEntity<Object> saveFinancialForm(FinancialForm financialForm) throws ParseException {
            financialForm.setCreatedDate(SqlDate.getDateInSqlFormat());
            financialForm.setUpdatedDate(SqlDate.getDateInSqlFormat());
            financialForm.setApplicationStatus("Inreview");
            financialForm.setActive(true);
            financialRepository.save(financialForm);
            return ResponseHandler.generateResponse(HttpStatus.OK,false,"Form Application Added",null);
    }

    /**
     * updetes a form by object
     * @param financialForm
     * @return
     * @throws ParseException
     */
    public ResponseEntity<Object> updateFinancialForm(FinancialForm financialForm) throws ParseException {                  // Update user
        try{
                Long id = financialForm.getId();
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                if (financialRepository.existsById(id)) {
                    financialRepository.save(financialForm);
                    return ResponseHandler.generateResponse(HttpStatus.OK,false,"financialForm updated thank you",null);
                } else {
                    return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"financialForm not exist",null);
                }
        }
        catch (Exception e)
            {
                LOG.info("Exception: "+e.getMessage()+ e.getCause());
                return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getCause()+"  Cause"+e.getMessage(),null);

            }
    }

    /**
     * Inactive a form by id
     * @param id
     * @return
     * @throws ParseException
     */
    public ResponseEntity<Object> deleteFinancialForm(Long id) throws ParseException {                   //Financialform deleted
        try{
            Optional<FinancialForm> existingForm= financialRepository.findById(id);
            if (existingForm.isPresent()) {
                existingForm.get().setUpdatedDate(SqlDate.getDateInSqlFormat());
                existingForm.get().setActive(false);
                financialRepository.save(financialForm);
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"financialForm has been deactivated",null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Data not exists Please enter Valid ID",null);
            }
        }
        catch (Exception e){
            LOG.info("Exception: " + e.getMessage()+ "  Cause"+e.getCause());
        return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage()+ e.getCause(),null);}
    }

    /**
     * Returns a form by id
     * @param id
     * @return
     * @throws ParseException
     */
    public ResponseEntity<Object> getFinancialForm(Long id) throws ParseException {
        try{
            Optional<FinancialForm> financialForm = financialRepository.findById(id);
            if (financialForm.isPresent())
            {
            return ResponseHandler.generateResponse(HttpStatus.OK,false,"form by id: "+id,financialForm);}
            else
            {
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"form not found",null);}
        }
        catch (Exception exception)
        {
            LOG.info("Exception: "+exception.getMessage()+" cause: "+exception.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+exception.getMessage(),null);
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
            List<FinancialForm> existingForms = financialRepository.findByCreatedDate(date);
            if (existingForms.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Data not found for the entered date in database",null);
            } else {
                LOG.info("List of financial application: Sorted by date ");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of financial application: Sorted by date ",existingForms);
            }
        } catch (Exception e) {
            LOG.info("error in searchbydate in class financialService :"+e.getMessage() + e.getCause());
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
            List<FinancialForm> existingForms = financialRepository.findByStartDate(date);
            if (existingForms.isEmpty()) {

                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Data not found for the entered date in database",null);
            } else {
                LOG.info("List of financial application: Sorted by date ");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of financial application: Sorted by start date",null);
            }
        } catch (Exception e) {
            LOG.info("error in searchbydate in class financialService :"+e.getMessage() + e.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage()+"  Cause: "+e.getCause(),null);
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
            List<FinancialForm> existingForms =financialRepository.findByCreatedDateBetweenOrderByUpdatedDateAsc(startDate,endDate);
            if (existingForms.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Data not found for the entered date in database",null);
            } else {
                LOG.info("List of financial application: Sorted by date ");
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"Sorted by start to end date",existingForms);
            }
        } catch (Exception e) {
            LOG.info("error in searchbydate in class financialService :"+e.getMessage() + e.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception",null);
        }
    }

    /**
     * display list of all active financial applications
     * @return
     */
    public ResponseEntity<Object> listAllActive() throws ParseException {
        try {
            List<FinancialForm> existingForms = financialRepository.findByActive(true);
            if (existingForms.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no financial application in the database",null);
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
            List<FinancialForm> existingForms = financialRepository.findByActive(false);
            if (existingForms.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no financial applications in the database",null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of Inactive forms",existingForms);
            }
        } catch (Exception e) {
            LOG.info("Exception throws by listAllInactive financial applications at financialService  "+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }

    }

    /**
     * Returns a list of Specific user financial forms
     * @param userid
     * @return
     */
    public ResponseEntity<Object> findUserFinancialForms(Long userid) throws ParseException {
        try {
            List<FinancialForm> existingForm = financialRepository.findFinancialFormByUserId(userid);
            if (existingForm.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no application forms for the entered user ID",null);
            } else {

                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of users forms",existingForm);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception: "+e.getMessage()+"  Cause"+e.getCause());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * returns a List of Forms by specific application Status
     * @param applicationStatus
     * @return
     */
    public ResponseEntity<Object> ListOfFinancialFormsByApplicationStatus(String applicationStatus) {
        try{
            List<FinancialForm> existingForms= financialRepository.findEducationalFormsByApplicationStatus(applicationStatus);
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
            LOG.info("Exception throws by method(ListOfFinancialFormsByApplicationStatus) "+e.getMessage());
            return new ResponseEntity<>("Exception"+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }



}
