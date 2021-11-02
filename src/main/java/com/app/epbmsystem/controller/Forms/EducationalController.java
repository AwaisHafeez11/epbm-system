package com.app.epbmsystem.controller.Forms;

import com.app.epbmsystem.model.Forms.EducationalForm;
import com.app.epbmsystem.service.EducationalService;
import io.swagger.annotations.Api;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@RestController
@RequestMapping("/educationalForm")
@Api(value="Educationalform Operations - CRUD REST API's for the educationalform")
public class EducationalController {

    private static final Logger LOG =  LogManager.getLogger(FinancialController.class);
    private static String token="awais1234";

    final EducationalService educationalService;

    public EducationalController(EducationalService educationalService) {
        this.educationalService = educationalService;
    }

    public boolean authorization(String token) {
        LOG.info("Authorizing the user ");
        return EducationalController.token.equals(token);
    }

    public ResponseEntity<Object> UnAuthorizeUser() {
        LOG.info("Unauthorized user is trying to get access");
        return new ResponseEntity<>("Kindly do the authorization first", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> listOfEducationalForms(@RequestHeader("Authorization") String token) {
        if (authorization(token)) {
            return educationalService.listAllEducationalFroms();
        } else {
            LOG.info("Unauthorized user trying to access the database");
            return UnAuthorizeUser();
        }
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEducationalFormByID(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (authorization(token)) {
            return educationalService.getEducationalForm(id); //It will return the Response
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser(); //If the user is not authorized
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> UpdateEducationalForm(@RequestHeader("Authorization") String token, @RequestBody EducationalForm educationalForm) {
        if (authorization(token)) {
            return educationalService.updateEducationalForm(educationalForm);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser() ;
        }
    }

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
}
