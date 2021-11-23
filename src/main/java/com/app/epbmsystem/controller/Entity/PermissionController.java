package com.app.epbmsystem.controller.Entity;

import com.app.epbmsystem.model.Entity.Permission;
import com.app.epbmsystem.service.PermissionService;
import com.app.epbmsystem.util.ResponseHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.text.ParseException;

@EnableSwagger2
@RestController
@RequestMapping("/permission")
public class PermissionController {

    private static final Logger LOG =  LogManager.getLogger(PermissionController.class);
    private static String token="awais1234";

    final PermissionService permissionService;


    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public boolean authorization(String token) {
        LOG.info("Authorizing the user ");
        return PermissionController.token.equals(token);
    }

    public ResponseEntity<Object> UnAuthorizeUser() throws ParseException {
        LOG.info("Unauthorized user is trying to get access");
        return ResponseHandler.generateResponse(HttpStatus.UNAUTHORIZED,true,"Unauthorized, Kindly do the authorization first",null);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> listOfPermissions(@RequestHeader("Authorization") String token) throws ParseException {
        if (authorization(token)) {
            return permissionService.listAllPermissions();
        } else {
            LOG.info("Unauthorized user trying to access the database");
            return UnAuthorizeUser();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestHeader("Authorization") String token, @RequestBody Permission permission) {
        try{
            if (authorization(token)) {
                return permissionService.savePermission(permission);
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
    public ResponseEntity<Object> getPermissionByID(@RequestHeader("Authorization") String token, @PathVariable Long id) throws ParseException {
        if (authorization(token)) {
            return permissionService.getPermission(id); //It will return the Response
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser(); //If the user is not authorized
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> UpdatePermission(@RequestHeader("Authorization") String token, @RequestBody Permission permission) throws ParseException {
        if (authorization(token)) {
            return permissionService.updatePermission(permission);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser() ;
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> DeletePermission(@PathVariable Long id, @RequestHeader("Authorization") String token) throws ParseException {

        if (authorization(token)) {
            try{
                return permissionService.deletePermission(id);
            }catch (Exception e){
                LOG.info("UnAuthorized User was trying to access the database");
                return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage()+"  Cause: "+e.getCause(),null);
            }
        }
        else{
            return UnAuthorizeUser();
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Object> DeleteCategory(@RequestHeader("Authorization") String token, @RequestParam("delete") Long id) throws ParseException {
        if (authorization(token)) {
            try{
                return permissionService.deletePermission(id);
            }catch (Exception exception){
                LOG.info("Exception: "+exception.getMessage());
                return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+exception.getMessage()+"  Cause: "+exception.getCause(),null);

            }
        }
        else{
            return UnAuthorizeUser();
        }
    }



}
