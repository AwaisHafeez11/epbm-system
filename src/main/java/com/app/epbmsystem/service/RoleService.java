package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Entity.Category;
import com.app.epbmsystem.model.Entity.Permission;
import com.app.epbmsystem.model.Entity.Role;
import com.app.epbmsystem.repository.RoleRepository;
import com.app.epbmsystem.util.DateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
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
    public ResponseEntity<Object> saveRole(Role role){
        try
        {
            Optional<Permission> existingRole= roleRepository.findByName(role.getName());
             if(existingRole.isPresent())
             {
                 if(existingRole.get().isActive())
                 {
                     return new ResponseEntity<>("Role Already exists ",HttpStatus.BAD_REQUEST);
                 }
                 else
                 {
                     return new ResponseEntity<>("please Update existing roles ",HttpStatus.OK);
                 }
             }
            else
            {
                role.setCreatedDate(DateTime.getDateTime());
                role.setUpdatedDate(DateTime.getDateTime());
                role.setActive(true);
                roleRepository.save(role);
                return new ResponseEntity<>("Role Added",HttpStatus.OK);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception"+ e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * List of All active and inactive roles
     * @return
     */
    public ResponseEntity<Object> listAllRoles(){
        try {
            List<Role> RoleList= roleRepository.findAll();
            if (RoleList.isEmpty())
            {
                return new ResponseEntity<>("No Role exists in the database", HttpStatus.NOT_FOUND);
            }
            else
            {
                return new ResponseEntity<>(RoleList, HttpStatus.OK);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * this method is to update Role into the DB
     * @param role
     * @return
     */
    public ResponseEntity<Object> updateRole(Role role){
        try{
            Long id = role.getId();
            Optional<Role> existingRole= roleRepository.findById(id);
            if (existingRole.isPresent())
            {
                if (existingRole.get().isActive())
                {
                    existingRole.get().setUpdatedDate(DateTime.getDateTime());
                    roleRepository.save(existingRole.get());
                    return new ResponseEntity<>("Role updated thank you", HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>("Please Add role before update it. Thank you",HttpStatus.BAD_REQUEST);
                }
            }
            else
            {
                return new ResponseEntity<>("Role not exist", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This method can delete/Inactive role into the DB
     * @param id
     * @return
     */
    public ResponseEntity<Object> deleteRole(Long id){                   //Delete Role
        try {
            Optional<Role> existingRole= roleRepository.findById(id);
            if (existingRole.isPresent())
            {
                if (existingRole.get().isActive())
                {
                    existingRole.get().setUpdatedDate(DateTime.getDateTime());
                    existingRole.get().setActive(false);
                    roleRepository.save(existingRole.get());
                    return new ResponseEntity<>("Role Inactive/Deleted into the DB",HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(" Role already deleted", HttpStatus.BAD_REQUEST);
                }
            }
            else
            {
                return new ResponseEntity<>("Role Not exists Please enter Valid ID", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("Role deleted", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Method will extract role by its given id, if exists
     * @param id
     * @return
     */
    public ResponseEntity<Object> getRole(Long id){
        try{
            Optional<Role> role = roleRepository.findById(id);
            if (role.isPresent())
            {
                return new ResponseEntity<>(role, HttpStatus.FOUND);
            }
            else
            {
                return new ResponseEntity<>("Role Not exists Please enter Valid ID", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception exception)
        {return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
    }

    /**
     * this methods displays list of all active roles
     * @return
     */
    public ResponseEntity<Object> listOfActiveRoles() {
        try
        {
            List<Role> existingRoles = roleRepository.findAllByActive(true);
            if (existingRoles.isEmpty()) {
                return new ResponseEntity<>("There are no active Category in the database", HttpStatus.NOT_FOUND);
            }
            else
            {
                return new ResponseEntity<>(existingRoles, HttpStatus.OK);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception"+ e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * this method displays list of all inactive roles
     * @return
     */
    public ResponseEntity<Object> listAllInactiveRoles() {
        try {
            List<Role> existingRoles = roleRepository.findAllByActive(false);
            if (existingRoles.isEmpty())
            {
                return new ResponseEntity<>("There are no Inactive Roles in the database", HttpStatus.NOT_FOUND);
            }
            else
            {
                return new ResponseEntity<>(existingRoles, HttpStatus.OK);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception"+ e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
