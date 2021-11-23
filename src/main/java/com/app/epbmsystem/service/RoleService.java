package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Entity.Category;
import com.app.epbmsystem.model.Entity.Permission;
import com.app.epbmsystem.model.Entity.Role;
import com.app.epbmsystem.repository.RoleRepository;
import com.app.epbmsystem.util.DateTime;
import com.app.epbmsystem.util.ResponseHandler;
import com.app.epbmsystem.util.SqlDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.ws.Response;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    final RoleRepository roleRepository;
    Role role;
    private static final Logger LOG = LogManager.getLogger(RoleService.class);
    /**
     * Autowiring through Constructor
     * @param roleRepository
     */
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    /**
     * Add Role into the table
     * @param role
     * @return
     */
    public ResponseEntity<Object> saveRole(Role role) throws ParseException {
        try
        {
            Optional<Permission> existingRole= roleRepository.findByName(role.getName());
             if(existingRole.isPresent())
             {
                 if(existingRole.get().isActive())
                 {

                     return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,true,"Role Already exists ",null);
                 }
                 else
                 {

                     return ResponseHandler.generateResponse(HttpStatus.OK,false,"please Update existing roles ",null);
                 }
             }
            else
            {
                role.setCreatedDate(SqlDate.getDateInSqlFormat());
                role.setUpdatedDate(SqlDate.getDateInSqlFormat());
                role.setActive(true);
                roleRepository.save(role);
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"Role Added",role);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception"+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true," Exception: "+e.getMessage(),null);
        }
    }

    /**
     * List of All active and inactive roles
     * @return
     */
    public ResponseEntity<Object> listAllRoles() throws ParseException {
        try {
            List<Role> RoleList= roleRepository.findAll();
            if (RoleList.isEmpty())
            {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"No Role exists in the database",null);
            }
            else
            {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of Roles",RoleList);
            }
        }
        catch (Exception e){

            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,false,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * this method is to update Role into the DB
     * @param role
     * @return
     */
    public ResponseEntity<Object> updateRole(Role role) throws ParseException {
        try{
            Long id = role.getId();
            Optional<Role> existingRole= roleRepository.findById(id);
            if (existingRole.isPresent())
            {
                if (existingRole.get().isActive())
                {
                    existingRole.get().setUpdatedDate(SqlDate.getDateInSqlFormat());
                    roleRepository.save(existingRole.get());
                    return ResponseHandler.generateResponse(HttpStatus.OK,false,"Role updated thank you",null);
                }
                else
                {
                    return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,true,"",null);
                }
            }
            else
            {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Role not exist",null);
            }
        }
        catch (Exception e)
        {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * This method can delete/Inactive role into the DB
     * @param id
     * @return
     */
    public ResponseEntity<Object> deleteRole(Long id) throws ParseException {
        try {
            Optional<Role> existingRole= roleRepository.findById(id);
            if (existingRole.isPresent())
            {
                if (existingRole.get().isActive())
                {
                    existingRole.get().setUpdatedDate(SqlDate.getDateInSqlFormat());
                    existingRole.get().setActive(false);
                    roleRepository.save(existingRole.get());
                    return ResponseHandler.generateResponse(HttpStatus.OK,false,"Role Added Successfully",null);
                }
                else
                {
                    return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,true,"Role already deleted",null);
                }
            }
            else
            {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"",null);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception: " + e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,true,"Role deleted"+e.getMessage(),null);
        }
    }

    /**
     * Method will extract role by its given id, if exists
     * @param id
     * @return
     */
    public ResponseEntity<Object> getRole(Long id) throws ParseException {
        try{
            Optional<Role> role = roleRepository.findById(id);
            if (role.isPresent())
            {
                return ResponseHandler.generateResponse(HttpStatus.FOUND,false,"",role);
            }
            else
            {

                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Role Not exists Please enter Valid ID",null);
            }
        }
        catch (Exception exception) {

            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, true, "Exception "+exception.getMessage(), null);
        }
        }

    /**
     * this methods displays list of all active roles
     * @return
     */
    public ResponseEntity<Object> listOfActiveRoles() throws ParseException {
        try
        {
            List<Role> existingRoles = roleRepository.findAllByActive(true);
            if (existingRoles.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no active Roles in the database",null);
            }
            else
            {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of Active Roles",existingRoles);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception"+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * this method displays list of all inactive roles
     * @return
     */
    public ResponseEntity<Object> listAllInactiveRoles() throws ParseException {
        try {
            List<Role> existingRoles = roleRepository.findAllByActive(false);
            if (existingRoles.isEmpty())
            {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no Inactive Roles in the database",null);
            }
            else
            {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of inactive Roles",existingRoles);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception"+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception"+e.getMessage(),null);
        }
    }


}
