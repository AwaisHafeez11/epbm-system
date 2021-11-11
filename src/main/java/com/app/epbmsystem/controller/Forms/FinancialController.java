package com.app.epbmsystem.controller.Forms;


import com.app.epbmsystem.model.Forms.FinancialForm;
import com.app.epbmsystem.service.FinancialService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
    public ResponseEntity<Object> listOfFinancialForms(@RequestHeader("Authorization") String token) {
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
    public ResponseEntity<Object> getFinancialFormByID(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (authorization(token)) {
            return financialService.getFinancialForm(id); //It will return the Response
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser(); //If the user is not authorized
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> UpdateFinancialForm(@RequestHeader("Authorization") String token, @RequestBody FinancialForm financialForm) {
        if (authorization(token)) {
            return financialService.updateFinancialForm(financialForm);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser() ;
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> DeleteFinancialForm(@PathVariable Long id, @RequestHeader("Authorization") String token) {

        if (authorization(token)) {
            try{
                return financialService.deleteFinancialForm(id);
            }catch (Exception exception){
                LOG.info("UnAuthorized User was trying to access the database");
                return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else{
            return UnAuthorizeUser();
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



}
