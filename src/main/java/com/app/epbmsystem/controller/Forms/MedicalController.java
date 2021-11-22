package com.app.epbmsystem.controller.Forms;

import com.app.epbmsystem.controller.Entity.PermissionController;
import com.app.epbmsystem.model.Forms.MedicalForm;
import com.app.epbmsystem.service.MedicalService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Date;
import java.text.ParseException;

@EnableSwagger2
@RestController
@RequestMapping("/medical")
public class MedicalController {
    final MedicalService medicalService;
    private static final Logger LOG = LogManager.getLogger(PermissionController.class);
    private static String token = "awais1234";

    public MedicalController(MedicalService medicalService) {
        this.medicalService = medicalService;
    }

    /**
     * this method returns boolean type data w.r.t user exists or not
     * @param token
     * @return
     */
    public boolean authorization(String token) {
        LOG.info("Authorizing the user ");
        return MedicalController.token.equals(token);
    }

    /**
     * Response for unauthorize users
     * @return
     */
    public ResponseEntity<Object> UnAuthorizeUser() {
        LOG.info("Unauthorized user is trying to get access");
        return new ResponseEntity<>("Kindly do the authorization first", HttpStatus.UNAUTHORIZED);
    }

    /**
     * This method will list all medical forms
     * @param token
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<Object> listOfMedicalForms(@RequestHeader("Authorization") String token) throws ParseException {
        if (authorization(token)) {
            return medicalService.listAllMedicalFroms();
        } else {
            LOG.info("Unauthorized user trying to access the database");
            return UnAuthorizeUser();
        }
    }

    /**
     * this message Add medical application form by this controller
     * @param token
     * @param medicalForm
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestHeader("Authorization") String token, @RequestBody MedicalForm medicalForm) {
        try {
            if (authorization(token)) {
                return medicalService.saveMedicalForm(medicalForm);
            } else {
                LOG.info("Unauthorized user trying to access the database");
                return UnAuthorizeUser();
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This api returns a medical by given id if exists
     * @param token
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getMedicalFormByID(@RequestHeader("Authorization") String token, @PathVariable Long id) throws ParseException {
        if (authorization(token)) {
            return medicalService.getMedicalForm(id);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser();
        }
    }

    /**
     * this api updates existing medical foprms
     * @param token
     * @param medicalForm
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<Object> UpdatePermission(@RequestHeader("Authorization") String token, @RequestBody MedicalForm medicalForm) throws ParseException {
        if (authorization(token)) {
            return medicalService.updateMedicalForm(medicalForm);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser();
        }
    }

    /**
     * delete application by entering id of application in the url
     * @param id
     * @param token
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> DeletePermission(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        if (authorization(token)) {
            try {
                return medicalService.deleteMedicalForm(id);
            } catch (Exception exception) {
                LOG.info("UnAuthorized User was trying to access the database");
                return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return UnAuthorizeUser();
        }
    }

    /**
     * delete application by entering id of application in the param
     * @param token
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Object> DeleteMedicalForm(@RequestHeader("Authorization") String token, @RequestParam("delete") Long id) {
        if (authorization(token)) {
            try {
                return medicalService.deleteMedicalForm(id);
            } catch (Exception exception) {
                LOG.info("Exception: " + exception.getMessage());
                return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return UnAuthorizeUser();
        }
    }

    /**
     * this method get list of application w.r.t from start date to the end of the existing record
     * @param token
     * @param date
     * @return
     */
    @GetMapping("/searchByStartDate")
    public ResponseEntity<Object> SearchFormByStartDate(@RequestHeader("Authorization")String token, @RequestParam("date") Date date) {
        try
        {
            if (authorization(token))
            {
                return medicalService.searchByStartDate(date);
            } else {
                return UnAuthorizeUser();
            }
        }
        catch(Exception e){
            LOG.info("Exception: search by date "+e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    /**
     * this method get list of application between Start date and End date
     * @param token
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/searchByStartDateToEndDate")
    public ResponseEntity<Object> SearchFormByStartDateToEndDate(@RequestHeader("Authorization")String token, @RequestParam("Startdate") Date startDate,@RequestParam("EndDate") Date endDate) {
        try
        {
            if (authorization(token))
            {
                return medicalService.searchByStartDateAndEndDate(startDate,endDate);
            } else {
                return UnAuthorizeUser();
            }
        }
        catch(Exception e)
        {
            LOG.info("Exception: search by date "+e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * return list searched by specific date
     * @param token
     * @param date
     * @return
     */
    @GetMapping("/searchByDate")
    public ResponseEntity<Object> SearchFormByDate(@RequestHeader("Authorization")String token, @RequestParam("date") Date date) {
        try
        {
            if (authorization(token))
            {
                return medicalService.searchByDate(date);
            } else {
                return UnAuthorizeUser();
            }
        }
        catch(Exception e){
            LOG.info("Exception: search by date "+e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    /**
     * List of all Inactive disease display
     * @param token
     * @return
     */
    @GetMapping("/list/Inactive")
    public ResponseEntity<Object> listOfInActiveMedicalForms(@RequestHeader("Authorization") String token) throws ParseException {
        if (authorization(token)) {
            LOG.info("Listing all the Medical forms that are Inactive");
            return medicalService.listAllInactive();
        } else {
            return UnAuthorizeUser();
        }
    }

    /**
     * List of all Inactive disease display
     * @param token
     * @return
     */
    @GetMapping("/list/active")
    public ResponseEntity<Object> listOfActiveMedicalForms(@RequestHeader("Authorization") String token) throws ParseException {
        if (authorization(token)) {
            LOG.info("Listing all the disease that are active");
            return medicalService.listAllActive();
        } else {
            LOG.info("Unautorize user tried to access system");
            return UnAuthorizeUser();
        }
    }

}
