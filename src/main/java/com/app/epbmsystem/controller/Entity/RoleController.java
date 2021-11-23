package com.app.epbmsystem.controller.Entity;

import com.app.epbmsystem.model.Entity.Role;
import com.app.epbmsystem.service.RoleService;
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
@RequestMapping("/role")
public class RoleController {
    final RoleService roleService;
    private static final Logger LOG =  LogManager.getLogger(RoleController.class);
    private static String token="awais1234";


    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    public boolean authorization(String token) {
        LOG.info("Authorizing the user ");
        return RoleController.token.equals(token);
    }

    public ResponseEntity<Object> UnAuthorizeUser() {
        LOG.info("Unauthorized user is trying to get access");
        return new ResponseEntity<>("Kindly do the authorization first", HttpStatus.UNAUTHORIZED);
    }

    /**
     *returns a list of Roles
     * @param token
     * @return
     * @throws ParseException
     */
    @GetMapping("/list")
    public ResponseEntity<Object> listOfRoles(@RequestHeader("Authorization") String token) throws ParseException {
        if (authorization(token)) {
            return roleService.listAllRoles();
        } else {
            LOG.info("Unauthorized user trying to access the database");
            return UnAuthorizeUser();
        }
    }

    /**
     * This method can add a role
     * @param token
     * @param role
     * @return
     * @throws ParseException
     */
    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestHeader("Authorization") String token, @RequestBody Role role) throws ParseException {
        try{
            if (authorization(token)) {
                return roleService.saveRole(role);
            } else {
                LOG.info("Unauthorized user trying to access the database");
                return UnAuthorizeUser();
            }
        }catch (Exception e){
            LOG.info(e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage()+"  Cause"+e.getCause(),null);
        }
    }

    /**
     * Returns a role w.r.t to ID
     * @param token
     * @param id
     * @return
     * @throws ParseException
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getRoleByID(@RequestHeader("Authorization") String token, @PathVariable Long id) throws ParseException {
        if (authorization(token)) {
            return roleService.getRole(id); //It will return the Response
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser(); //If the user is not authorized
        }
    }

    /**
     * Update an existing role
     * @param token
     * @param role
     * @return
     * @throws ParseException
     */
    @PutMapping("/update")
    public ResponseEntity<Object> UpdateRole(@RequestHeader("Authorization") String token, @RequestBody Role role) throws ParseException {
        if (authorization(token)) {
            return roleService.updateRole(role);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser() ;
        }
    }

    /**
     * Delete a role by id given in param
     * @param id
     * @param token
     * @return
     * @throws ParseException
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> DeleteRole(@PathVariable Long id, @RequestHeader("Authorization") String token) throws ParseException {

        if (authorization(token)) {
            try{
                return roleService.deleteRole(id);
            }catch (Exception exception){
                LOG.info("UnAuthorized User was trying to access the database");
                return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+exception.getMessage()+"  Cause"+exception.getCause(),null);
            }
        }
        else{
            return UnAuthorizeUser();
        }
    }

    /**
     * Delete an existing role by id given by url
     * @param token
     * @param id
     * @return
     * @throws ParseException
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Object> DeleteRole(@RequestHeader("Authorization") String token, @RequestParam("delete") Long id) throws ParseException {
        if (authorization(token)) {
            try{
                return roleService.deleteRole(id);
            }
            catch (Exception e){
                LOG.info("Exception: "+e.getMessage()+e.getCause());
                return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage()+"  Cause"+e.getCause(),null);
            }
        }
        else{
            return UnAuthorizeUser();
        }
    }

    /**
     * Returns a list of Inactive Roles
     * @param token
     * @return
     * @throws ParseException
     */
    @GetMapping("/inactive")
    public ResponseEntity<Object> ListOfInactiveRoles(@RequestHeader("Authorization")String token) throws ParseException {
        if(authorization(token)){
             try{
                return roleService.listAllInactiveRoles();
                }
                catch (ParseException e)
                {
                    return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage()+"  Cause"+e.getCause(),null);
                }
        }
        else
        {
          return UnAuthorizeUser();
        }
    }

    /**
     * Returns a list of Active Roles
     * @param token
     * @return
     * @throws ParseException
     */
    @GetMapping("/active")
    public ResponseEntity<Object> ListOfActiveRoles(@RequestHeader("Authorization")String token) throws ParseException {
        if(authorization(token)){
            try{
                return roleService.listOfActiveRoles();
            }
            catch (ParseException e)
            {
                return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage()+"  Cause"+e.getCause(),null);
            }
        }
        else
        {
            return UnAuthorizeUser();
        }
    }
}
