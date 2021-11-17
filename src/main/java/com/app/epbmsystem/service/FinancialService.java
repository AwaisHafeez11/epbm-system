package com.app.epbmsystem.service;


import com.app.epbmsystem.model.Forms.EducationalForm;
import com.app.epbmsystem.model.Forms.FinancialForm;
import com.app.epbmsystem.model.Forms.MedicalForm;
import com.app.epbmsystem.repository.FinancialRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.DateFormat;
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

    public ResponseEntity<Object> listAllFinancialFroms(){   // List of all financial applications
        try {
                List<FinancialForm> financialFormList= financialRepository.findAll();
                if (financialFormList.isEmpty())
                {
                    return new ResponseEntity<>("No FinancialForm exists in the database", HttpStatus.NOT_FOUND);
                }
                else
                {
                    return new ResponseEntity<>(financialFormList, HttpStatus.OK);
                }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> saveFinancialForm(FinancialForm financialForm) {
            financialRepository.save(financialForm);
            return new ResponseEntity<>("Financial Application Added /n Thank you for adding   ",HttpStatus.OK);
    }

    public ResponseEntity<Object> updateFinancialForm(FinancialForm financialForm){                  // Update user
        try{
                Long id = financialForm.getId();
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                if (financialRepository.existsById(id)) {
                    financialRepository.save(financialForm);
                    return new ResponseEntity<>("financialForm updated thank you", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("financialForm not exist", HttpStatus.NOT_FOUND);
                }
        }
        catch (Exception e)
            {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    public ResponseEntity<Object> deleteFinancialForm(Long id){                   //Financialform deleted
        try{
            if (financialRepository.existsById(id)) {
                financialRepository.delete(financialForm);
                return new ResponseEntity<>(" financialForm has been Deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("financialForm Not exists Please enter Valid ID", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            LOG.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("financialForm deleted", HttpStatus.BAD_REQUEST);}
    }

    public ResponseEntity<Object> getFinancialForm(Long id){
        try{
            Optional<FinancialForm> financialForm = financialRepository.findById(id);
            if (financialForm.isPresent())
            {return new ResponseEntity<>(financialForm, HttpStatus.FOUND); }
            else
            {return new ResponseEntity<>(financialForm, HttpStatus.NOT_FOUND); }
        }
        catch (Exception exception)
        {return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
    }

    /**
     * This method returns a list of forms w.r.t to specific date
     * @param date
     * @return
     */
    public ResponseEntity<Object> searchByDate(Date date) {
        try {
            LOG.info("Checking Weather data is present or not");
            List<FinancialForm> existingForms = financialRepository.findByCreatedDate(date);
            if (existingForms.isEmpty()) {
                return new ResponseEntity<>("Data not found for the entered date in database.", HttpStatus.NOT_FOUND);
            } else {
                LOG.info("List of financial application: Sorted by date ");
                return new ResponseEntity<>(existingForms, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("error in searchbydate in class financialService :"+e.getMessage() + e.getCause());

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
            List<FinancialForm> existingForms = financialRepository.findByStartDate(date);
            if (existingForms.isEmpty()) {
                return new ResponseEntity<>("Data not found for the entered date in database.", HttpStatus.NOT_FOUND);
            } else {
                LOG.info("List of financial application: Sorted by date ");
                return new ResponseEntity<>(existingForms, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("error in searchbydate in class financialService :"+e.getMessage() + e.getCause());

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
            List<FinancialForm> existingForms =financialRepository.findByCreatedDateBetweenOrderByUpdatedDateAsc(startDate,endDate);
            if (existingForms.isEmpty()) {
                return new ResponseEntity<>("Data not found for the entered date in database.", HttpStatus.NOT_FOUND);
            } else {
                LOG.info("List of financial application: Sorted by date ");
                return new ResponseEntity<>(existingForms, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("error in searchbydate in class financialService :"+e.getMessage() + e.getCause());
            return new ResponseEntity<Object>("An error occured ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * display list of all active financial applications
     * @return
     */
    public ResponseEntity<Object> listAllActive() {
        try {
            List<FinancialForm> existingForms = financialRepository.findByActive(true);
            if (existingForms.isEmpty()) {
                return new ResponseEntity<>("There are no financial application in the database", HttpStatus.NOT_FOUND);
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
            List<FinancialForm> existingForms = financialRepository.findByActive(false);
            if (existingForms.isEmpty()) {
                return new ResponseEntity<>("There are no financial applications in the database", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(existingForms, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("Exception throws by listAllInactive financial applications at financialService  "+ e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }

    /**
     * Returns a list of Specific user financial forms
     * @param userid
     * @return
     */
    public ResponseEntity<Object> findUserFinancialForms(Long userid) {
        try {
            List<FinancialForm> existingForm = financialRepository.findFinancialFormByUserId(userid);
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
