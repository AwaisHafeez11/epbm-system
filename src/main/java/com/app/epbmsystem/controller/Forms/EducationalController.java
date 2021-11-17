package com.app.epbmsystem.controller.Forms;

import com.app.epbmsystem.model.Entity.Category;
import com.app.epbmsystem.model.Forms.EducationalForm;
import com.app.epbmsystem.service.EducationalService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Date;
import java.util.List;


@EnableSwagger2
@RestController
@RequestMapping("/educationalForm")
public class EducationalController {

    private static final Logger LOG =  LogManager.getLogger(FinancialController.class);
    private static final String token="awais1234";

    final EducationalService educationalService;

    public EducationalController(EducationalService educationalService) {
        this.educationalService = educationalService;
    }

    /**
     * this method authorize the request from its token
     * @param token
     * @return
     */
    public boolean authorization(String token) {
        LOG.info("Authorizing the user ");
        return EducationalController.token.equals(token);
    }

    /**
     * this method return a response for unauthorized user
     * @return
     */
    public ResponseEntity<Object> UnAuthorizeUser() {
        LOG.info("Unauthorized user is trying to get access");
        return new ResponseEntity<>("Kindly do the authorization first", HttpStatus.UNAUTHORIZED);
    }

    /**
     * this method returns a list of educational forms
     * @param token
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<Object> listOfEducationalForms(@RequestHeader("Authorization") String token) {
        if (authorization(token)) {
            return educationalService.listAllEducationalFroms();
        } else {
            LOG.info("Unauthorized user trying to access the database");
            return UnAuthorizeUser();
        }
    }

    /**
     * this method add a application into the database
     * @param token
     * @param educationalForm
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestHeader("Authorization") String token, @RequestBody EducationalForm educationalForm) {
        try{
            if (authorization(token)) {
                return educationalService.saveEducationalForm(educationalForm);
            } else {
                LOG.info("Unauthorized user trying to access the database");
                return UnAuthorizeUser();
            }
        }catch (Exception e){
            LOG.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * this method gets a application form by its ID
     * @param token
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getEducationalFormByID(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (authorization(token)) {
            return educationalService.getEducationalForm(id); //It will return the Response
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser(); //If the user is not authorized
        }
    }

    /**
     * this method updates existing application
     * @param token
     * @param educationalForm
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<Object> UpdateEducationalForm(@RequestHeader("Authorization") String token, @RequestBody EducationalForm educationalForm) {
        if (authorization(token)) {
            return educationalService.updateEducationalForm(educationalForm);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser() ;
        }
    }

    /**
     * this method delete application from the database
     * @param id
     * @param token
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> DeleteEducationalForm(@PathVariable Long id, @RequestHeader("Authorization") String token) {

        if (authorization(token)) {
            try{
                return educationalService.deleteEducationalForm(id);
            }catch (Exception exception){
                LOG.info("UnAuthorized User was trying to access the database");
                return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else{
            return UnAuthorizeUser();
        }
    }

    /**
     * this method delete application from the database
     * @param token
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Object> DeleteEducationalForm(@RequestHeader("Authorization") String token, @RequestParam("delete") Long id) {
        if (authorization(token)) {
            try{
                return educationalService.deleteEducationalForm(id);
            }catch (Exception exception){
                LOG.info("Exception: "+exception.getMessage());
                return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else{
            return UnAuthorizeUser();
        }
    }

    /**
     * this method return list of application w.r.t Date
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
                return educationalService.searchByDate(date);
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
                return educationalService.searchByStartDate(date);
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
                return educationalService.searchByStartDateAndEndDate(startDate,endDate);
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
     * Returns a list of active applications
     * @param token
     * @return
     */
    @GetMapping("/active")
    public ResponseEntity<Object> ListOfActiveForms(@RequestHeader("Authorization")String token) {
        try
        {
            if (authorization(token))
            {
                return educationalService.listAllActive();
            } else {
                return UnAuthorizeUser();
            }
        }
        catch(Exception e)
        {
            LOG.info("Exception:At List of Active Educational forms  "+e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    /**
     * Returns a list of Inactive applications
      * @param token
     * @return
     */
    @GetMapping("/inactive")
    public ResponseEntity<Object> ListOfInactiveForms(@RequestHeader("Authorization")String token) {
         try
        {
            if (authorization(token))
            {
                return educationalService.listAllInactive();
            } else {
                return UnAuthorizeUser();
            }
        }
        catch(Exception e)
        {
            LOG.info("Exception:At List of Active Educational forms  "+e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Return list of forms by specific user
     * @param token
     * @param userId
     * @return
     */
    @GetMapping("/searchEducationalFormByUser")
    public ResponseEntity<Object> ListAllUserEducationalForms(@RequestHeader("Authorization")String token,@RequestParam("UserId")Long userId){
        try{
            if (authorization(token))
            {
                LOG.info("Token authorized");
                return educationalService.ListOfUserEducationalForms(userId);
            }
            else
            {
                LOG.info("Token not authorized");
                return new ResponseEntity<>("Please enter valid token first",HttpStatus.UNAUTHORIZED);
            }

        }
        catch (Exception e)
        {
            return new ResponseEntity<>("Exception: "+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Return a list of forms for specific user
     * @param token
     * @param status
     * @return
     */
    @GetMapping("/searchFormsByStatus")
    public ResponseEntity<Object> ListAllFormsByStatus(@RequestHeader("Authorization")String token,@RequestParam("UserId")String status){
        try{
            if (authorization(token))
            {
                LOG.info("Token authorized");
                return educationalService.ListOfEducationalFormsByApplicationStatus(status);
            }
            else
            {
                LOG.info("Token not authorized");
                return new ResponseEntity<>("Please enter valid token first",HttpStatus.UNAUTHORIZED);
            }

        }
        catch (Exception e)
        {
            LOG.info("Exception throws by inReviewForms API ");
            return new ResponseEntity<>("Exception: "+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


}
