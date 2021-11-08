package com.app.epbmsystem.controller.Forms;

import com.app.epbmsystem.model.Forms.Hospital;
import com.app.epbmsystem.service.HospitalService;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@RestController
@Api(value = "Hospital crud Operations")
public class HospitalController {
    private static final Logger LOG = LogManager.getLogger(HospitalController.class);
    final HospitalService hospitalService;
    private static String token="awais1234";

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    public boolean authorization(String token) {
        LOG.info("Authorizing the user ");
        return HospitalController.token.equals(token);
    }

    public ResponseEntity<Object> UnAuthorizeUser() {
        LOG.info("Unauthorized user is trying to get access");
        return new ResponseEntity<>("Kindly do the authorization first", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> listOfHospitals(@RequestHeader("Authorization") String token) {
        if (authorization(token)) {
            return hospitalService.listAllHospitals();
        } else {
            LOG.info("Unauthorized user trying to access the database");
            return UnAuthorizeUser();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestHeader("Authorization") String token, @RequestBody Hospital hospital) {
        try{
            if (authorization(token)) {
                return hospitalService.saveHospital(hospital);
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
    public ResponseEntity<Object> getHospitalByID(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (authorization(token)) {
            return hospitalService.getHospital(id);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser(); //If the user is not authorized
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> UpdateHospital(@RequestHeader("Authorization") String token, @RequestBody Hospital hospital) {
        if (authorization(token)) {
            return hospitalService.updateHospital(hospital);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser() ;
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> DeleteHospital(@PathVariable Long id, @RequestHeader("Authorization") String token) {

        if (authorization(token)) {
            try{
                return hospitalService.deleteHospital(id);
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
    public ResponseEntity<Object> DeleteHospital(@RequestHeader("Authorization") String token, @RequestParam("delete") Long id) {
        if (authorization(token)) {
            try{
                return hospitalService.deleteHospital(id);
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
