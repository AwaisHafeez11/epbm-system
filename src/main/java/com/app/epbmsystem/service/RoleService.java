package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Entity.Category;
import com.app.epbmsystem.model.Entity.Role;
import com.app.epbmsystem.repository.RoleRepository;
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

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    private static final Logger LOG = LogManager.getLogger(RoleService.class);

    public ResponseEntity<Object> saveRole(Role role){
        roleRepository.save(role);
        return new ResponseEntity<>("Role Added /n Thank you for adding   ", HttpStatus.OK);
    }

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



    public ResponseEntity<Object> updateRole(Role role){                  // Update Role
        try{
            Long id = role.getId();
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            if (roleRepository.existsById(id)) {
                roleRepository.save(role);
                return new ResponseEntity<>("Role updated thank you", HttpStatus.OK);
            } else
            {
                return new ResponseEntity<>("Role not exist", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteRole(Long id){                   //Delete Role
        try {

            if (roleRepository.existsById(id)) {
                roleRepository.delete(role);
                return new ResponseEntity<>(" Role has been Deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Role Not exists Please enter Valid ID", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            LOG.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("Role deleted", HttpStatus.BAD_REQUEST);}
    }

    public ResponseEntity<Object> getRole(Long id){         // Get Role By id
        try{
            Optional<Role> role = roleRepository.findById(id);
            if (role.isPresent())
            {return new ResponseEntity<>(role, HttpStatus.FOUND); }
            else
            {return new ResponseEntity<>(role, HttpStatus.NOT_FOUND); }
        }
        catch (Exception exception)
        {return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
    }


}
