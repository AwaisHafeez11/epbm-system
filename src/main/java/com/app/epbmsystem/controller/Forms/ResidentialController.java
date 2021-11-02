package com.app.epbmsystem.controller.Forms;

import com.app.epbmsystem.model.Forms.EducationalForm;
import com.app.epbmsystem.model.Forms.ResidentialForm;
import com.app.epbmsystem.service.ResidentialService;
import io.swagger.annotations.Api;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@RestController
@RequestMapping("/residential")
@Api(value="residentialForm Operations - CRUD REST API's for the residentialForm")
public class ResidentialController {
    private static final Logger LOG =  LogManager.getLogger(ResidentialController.class);
    private static String token="awais1234";

    final ResidentialService residentialService;

    public ResidentialController(ResidentialService residentialService) {
        this.residentialService = residentialService;
    }

    public boolean authorization(String token) {
        LOG.info("Authorizing the user ");
        return ResidentialController.token.equals(token);
    }

    public ResponseEntity<Object> UnAuthorizeUser() {
        LOG.info("Unauthorized user is trying to get access");
        return new ResponseEntity<>("Kindly do the authorization first", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> listOfResidentialForms(@RequestHeader("Authorization") String token) {
        if (authorization(token)) {
            return residentialService.listAllResidentialFroms();
        } else {
            LOG.info("Unauthorized user trying to access the database");
            return UnAuthorizeUser();
        }
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<Object> getResidentialFormByID(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (authorization(token)) {
            return residentialService.getResidentialForm(id);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser(); //If the user is not authorized
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> UpdateResidentialForm(@RequestHeader("Authorization") String token, @RequestBody ResidentialForm residentialForm) {
        if (authorization(token)) {
            return residentialService.updateResidentialForm(residentialForm);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser() ;
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> DeleteResidentialForm(@PathVariable Long id, @RequestHeader("Authorization") String token) {

        if (authorization(token)) {
            try{
                return residentialService.deleteResidentialForm(id);
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

}
