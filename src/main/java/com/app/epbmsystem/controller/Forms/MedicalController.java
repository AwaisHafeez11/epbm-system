package com.app.epbmsystem.controller.Forms;

import com.app.epbmsystem.controller.Entity.PermissionController;
import com.app.epbmsystem.model.Entity.Permission;
import com.app.epbmsystem.model.Forms.MedicalForm;
import com.app.epbmsystem.service.MedicalService;
import io.swagger.annotations.Api;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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

    public boolean authorization(String token) {
        LOG.info("Authorizing the user ");
        return MedicalController.token.equals(token);
    }

    public ResponseEntity<Object> UnAuthorizeUser() {
        LOG.info("Unauthorized user is trying to get access");
        return new ResponseEntity<>("Kindly do the authorization first", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> listOfMedicalForms(@RequestHeader("Authorization") String token) {
        if (authorization(token)) {
            return medicalService.listAllMedicalFroms();
        } else {
            LOG.info("Unauthorized user trying to access the database");
            return UnAuthorizeUser();
        }
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMedicalFormByID(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (authorization(token)) {
            return medicalService.getFinancialForm(id); //It will return the Response
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser(); //If the user is not authorized
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> UpdatePermission(@RequestHeader("Authorization") String token, @RequestBody MedicalForm medicalForm) {
        if (authorization(token)) {
            return medicalService.updateMedicalForm(medicalForm);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser();
        }
    }

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

    @DeleteMapping("/delete")
    public ResponseEntity<Object> DeleteCategory(@RequestHeader("Authorization") String token, @RequestParam("delete") Long id) {
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

}
