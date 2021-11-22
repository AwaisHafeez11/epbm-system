package com.app.epbmsystem.controller.Forms;

import com.app.epbmsystem.model.Forms.Disease;
import com.app.epbmsystem.service.DiseaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.text.ParseException;

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

    /**
     * Returns boolean after authorization
     * @param token
     * @return
     */
    public boolean authorization(String token) {
        LOG.info("Authorizing the user ");
        return DiseaseController.token.equals(token);
    }

    /**
     * Returns a response to the unauthorize user
     * @return
     */
    public ResponseEntity<Object> UnAuthorizeUser() {
        LOG.info("Unauthorized user is trying to get access");
        return new ResponseEntity<>("Kindly do the authorization first", HttpStatus.UNAUTHORIZED);
    }

    /**
     * Returns List of all diseases that exists
     * @param token
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<Object> listOfDisease(@RequestHeader("Authorization") String token) throws ParseException {
        if (authorization(token)) {
            return diseaseService.listAllDisease();
        } else {
            LOG.info("Unauthorized user trying to access the database");
            return UnAuthorizeUser();
        }
    }

    /**
     * this method add disease into the data
     * @param token
     * @param disease
     * @return
     */
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

    /**
     * returns a Object of disease by entering its ID
     * @param token
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getDiseaseByID(@RequestHeader("Authorization") String token, @PathVariable Long id) throws ParseException {
        if (authorization(token)) {
            return diseaseService.getDisease(id);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser(); //If the user is not authorized
        }
    }

    /**
     * Returns a updated disease into the table
     * @param token
     * @param disease
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<Object> UpdateDisease(@RequestHeader("Authorization") String token, @RequestBody Disease disease) throws ParseException {
        if (authorization(token)) {
            return diseaseService.updateDisease(disease);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser() ;
        }
    }

    /**
     * Inactive/delete the existing object by given id in the url
     * @param id
     * @param token
     * @return
     */
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

    /**
     * Inactive/delete the existing object by given id in param
     * @param token
     * @param id
     * @return
     */
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

    /**
     * List of all Inactive disease display
     * @param token
     * @return
     */
    @GetMapping("/list/Inactive")
    public ResponseEntity<Object> listOfInActiveUsers(@RequestHeader("Authorization") String token) throws ParseException {
        if (authorization(token)) {
            LOG.info("Listing all the disease that are Inactive");
            return diseaseService.listAllInactiveDiseases();
        } else {
            LOG.info("Listing all the disease that are Inactive");
            return UnAuthorizeUser();
        }
    }

    /**
     * List of all Inactive disease display
     * @param token
     * @return
     */
    @GetMapping("/list/active")
    public ResponseEntity<Object> listOfActiveUsers(@RequestHeader("Authorization") String token) throws ParseException {
        if (authorization(token)) {
            LOG.info("Listing all the disease that are active");
            return diseaseService.listAllActivediseases();
        } else {
            LOG.info("Listing all the disease that are active");
            return UnAuthorizeUser();
        }
    }


}
