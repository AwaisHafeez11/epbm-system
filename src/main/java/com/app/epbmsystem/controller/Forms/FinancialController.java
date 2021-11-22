package com.app.epbmsystem.controller.Forms;


import com.app.epbmsystem.model.Forms.FinancialForm;
import com.app.epbmsystem.service.FinancialService;
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
@RequestMapping("/financialForm")
public class FinancialController {
    private static final Logger LOG =  LogManager.getLogger(FinancialController.class);
    private static String token="awais1234";

    final FinancialService financialService;

    public FinancialController(FinancialService financialService) {
        this.financialService = financialService;
    }

    public boolean authorization(String token) {
        LOG.info("Authorizing the user ");
        return FinancialController.token.equals(token);
    }

    public ResponseEntity<Object> UnAuthorizeUser() {
        LOG.info("Unauthorized user is trying to get access");
        return new ResponseEntity<>("Kindly do the authorization first", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> listOfFinancialForms(@RequestHeader("Authorization") String token) throws ParseException {
        if (authorization(token)) {
            return financialService.listAllFinancialFroms();
        } else {
            LOG.info("Unauthorized user trying to access the database");
            return UnAuthorizeUser();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestHeader("Authorization") String token, @RequestBody FinancialForm financialForm) {
        try{
            if (authorization(token)) {
                return financialService.saveFinancialForm(financialForm);
            } else {
                LOG.info("Unauthorized user trying to access the database");
                return UnAuthorizeUser();
            }
        }catch (Exception e){
            LOG.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getFinancialFormByID(@RequestHeader("Authorization") String token, @PathVariable Long id) throws ParseException {
        if (authorization(token)) {
            return financialService.getFinancialForm(id); //It will return the Response
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser(); //If the user is not authorized
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> UpdateFinancialForm(@RequestHeader("Authorization") String token, @RequestBody FinancialForm financialForm) throws ParseException {
        if (authorization(token)) {
            return financialService.updateFinancialForm(financialForm);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser() ;
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> DeleteFinancialForm(@RequestHeader("Authorization") String token, @RequestParam("delete") Long id) {
        if (authorization(token)) {
            try{
                return financialService.deleteFinancialForm(id);
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
                return financialService.searchByStartDate(date);
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
                return financialService.searchByStartDateAndEndDate(startDate,endDate);
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
                return financialService.searchByDate(date);
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
     * List of all Inactive financialforms display
     * @param token
     * @return
     */
    @GetMapping("/list/Inactive")
    public ResponseEntity<Object> listOfInActiveFinancialForms(@RequestHeader("Authorization") String token) throws ParseException {
        if (authorization(token)) {
            LOG.info("Listing all the financial forms that are Inactive");
            return financialService.listAllInactive();
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
    public ResponseEntity<Object> listOfActiveFinancialForms(@RequestHeader("Authorization") String token) throws ParseException {
        if (authorization(token)) {
            LOG.info("Listing all the financial that are active");
            return financialService.listAllActive();
        } else {
            LOG.info("Unautorize user tried to access system");
            return UnAuthorizeUser();
        }
    }

    /**
     * Return list of forms by specific user
     * @param token
     * @param userId
     * @return
     */
    @GetMapping("/searchFinancialFormByUser")
    public ResponseEntity<Object> ListAllUserFinancialForms(@RequestHeader("Authorization")String token,@RequestParam("UserId")Long userId){
        try{
            if (authorization(token))
            {
                LOG.info("Token authorized");
                return financialService.findUserFinancialForms(userId);
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
                return financialService.ListOfFinancialFormsByApplicationStatus(status);
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
