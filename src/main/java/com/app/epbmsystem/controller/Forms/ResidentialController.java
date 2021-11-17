package com.app.epbmsystem.controller.Forms;

import com.app.epbmsystem.model.Forms.ResidentialForm;
import com.app.epbmsystem.service.ResidentialService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Date;

@EnableSwagger2
@RestController
@RequestMapping("/residential")
public class ResidentialController {
    private static final Logger LOG =  LogManager.getLogger(ResidentialController.class);
    private static String token="awais1234";

    final ResidentialService residentialService;

    public ResidentialController(ResidentialService residentialService) {
        this.residentialService = residentialService;
    }

    /**
     * method authorize user user to access apis
     * @param token
     * @return
     */
    public boolean authorization(String token) {
        LOG.info("Authorizing the user ");
        return ResidentialController.token.equals(token);
    }

    /**
     * returns a response for unautorize user
     * @return
     */
    public ResponseEntity<Object> UnAuthorizeUser() {
        LOG.info("Unauthorized user is trying to get access");
        return new ResponseEntity<>("Kindly do the authorization first", HttpStatus.UNAUTHORIZED);
    }

    /**
     * method returns list of all active and inactive residential forms
     * @param token
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<Object> listOfResidentialForms(@RequestHeader("Authorization") String token) {
        if (authorization(token)) {
            return residentialService.listAllResidentialFroms();
        } else {
            LOG.info("Unauthorized user trying to access the database");
            return UnAuthorizeUser();
        }
    }

    /**
     * method add a residential form
     * @param token
     * @param residentialForm
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestHeader("Authorization") String token, @RequestBody ResidentialForm residentialForm) {
        try{
            if (authorization(token)) {
                return residentialService.saveResidentialForm(residentialForm);
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
     * this method returns a residential for required ID
     * @param token
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getResidentialFormByID(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (authorization(token)) {
            return residentialService.getResidentialForm(id);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser(); //If the user is not authorized
        }
    }

    /**
     * This method updates a form
     * @param token
     * @param residentialForm
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<Object> UpdateResidentialForm(@RequestHeader("Authorization") String token, @RequestBody ResidentialForm residentialForm) {
        if (authorization(token)) {
            return residentialService.updateResidentialForm(residentialForm);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser() ;
        }
    }

    /**
     * this method inactivate existing form
     * @param id
     * @param token
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> DeleteResidentialForm(@PathVariable Long id, @RequestHeader("Authorization") String token) {

        if (authorization(token)) {
            try{
                return residentialService.deleteResidentialForm(id);
            }
            catch (Exception exception){
                LOG.info("UnAuthorized User was trying to access the database");
                return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else{
            return UnAuthorizeUser();
        }
    }

    /**
     * this method inactivate existing form
     * @param token
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Object> DeleteResidentialForm(@RequestHeader("Authorization") String token, @RequestParam("delete") Long id) {
        if (authorization(token)) {
            try{
                return residentialService.deleteResidentialForm(id);
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
     * List of all Inactive ResidentialForms display
     * @param token
     * @return
     */
    @GetMapping("/list/Inactive")
    public ResponseEntity<Object> listOfInActiveResidentialForms(@RequestHeader("Authorization") String token) {
        if (authorization(token)) {
            LOG.info("Listing all the ResidentialForms that are Inactive");
            return residentialService.listAllInactive();
        } else {
            LOG.info("No authorized");
            return UnAuthorizeUser();
        }
    }

    /**
     * List of all Inactive ResidentialForms
     * @param token
     * @return
     */
    @GetMapping("/list/active")
    public ResponseEntity<Object> listOfActiveResidentialForms(@RequestHeader("Authorization") String token) {
        if (authorization(token)) {
            LOG.info("Listing all the Residential forms that are active");
            return residentialService.listAllActive();
        } else {
            LOG.info("Unautorize user tried to access system");
            return UnAuthorizeUser();
        }
    }

    /**
     * return List sorted w.r.t to specific date
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
                return residentialService.searchByDate(date);
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
                return residentialService.searchByStartDate(date);
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
                return residentialService.searchByStartDateAndEndDate(startDate,endDate);
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
     * Return list of forms by specific user
     * @param token
     * @param userId
     * @return
     */
    @GetMapping("/searchResidentialFormByUser")
    public ResponseEntity<Object> ListAllUserResidentialForms(@RequestHeader("Authorization")String token,@RequestParam("UserId")Long userId){
        try{
            if (authorization(token))
            {
                LOG.info("Token authorized");
                return residentialService.ListOfUserResidentialForms(userId);
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
                return residentialService.ListOfResidentialFormsByApplicationStatus(status);
            }
            else
            {
                LOG.info("Token not authorized");
                return new ResponseEntity<>("Please enter valid token first",HttpStatus.UNAUTHORIZED);
            }

        }
        catch (Exception e)
        {
            LOG.info("Exception throws by API searchFormsByStatus ");
            return new ResponseEntity<>("Exception: "+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }



}
