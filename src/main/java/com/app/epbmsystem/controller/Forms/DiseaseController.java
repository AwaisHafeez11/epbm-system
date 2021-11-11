package com.app.epbmsystem.controller.Forms;

import com.app.epbmsystem.model.Forms.Disease;
import com.app.epbmsystem.service.DiseaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@RestController
@RequestMapping("/disease")
public class DiseaseController {

    final DiseaseService diseaseService;

    public DiseaseController(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
    }

    private static final Logger LOG =  LogManager.getLogger(DiseaseController.class);
    private static String token="awais1234";

    public boolean authorization(String token) {
        LOG.info("Authorizing the user ");
        return DiseaseController.token.equals(token);
    }

    public ResponseEntity<Object> UnAuthorizeUser() {
        LOG.info("Unauthorized user is trying to get access");
        return new ResponseEntity<>("Kindly do the authorization first", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> listOfDisease(@RequestHeader("Authorization") String token) {
        if (authorization(token)) {
            return diseaseService.listAllDisease();
        } else {
            LOG.info("Unauthorized user trying to access the database");
            return UnAuthorizeUser();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addDisease(@RequestHeader("Authorization") String token, @RequestBody Disease disease) {
        try{
            if (authorization(token)) {
                return diseaseService.saveDisease(disease);
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
    public ResponseEntity<Object> getDiseaseByID(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (authorization(token)) {
            return diseaseService.getDisease(id);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser(); //If the user is not authorized
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> UpdateDisease(@RequestHeader("Authorization") String token, @RequestBody Disease disease) {
        if (authorization(token)) {
            return diseaseService.updateDisease(disease);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser() ;
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> DeleteDisease(@PathVariable Long id, @RequestHeader("Authorization") String token) {

        if (authorization(token)) {
            try{
                return diseaseService.deleteDisease(id);
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
    public ResponseEntity<Object> DeleteDisease(@RequestHeader("Authorization") String token, @RequestParam("delete") Long id) {
        if (authorization(token)) {
            try{
                return diseaseService.deleteDisease(id);
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
