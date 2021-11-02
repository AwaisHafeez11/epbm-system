package com.app.epbmsystem.service;



import com.app.epbmsystem.model.Entity.Category;
import com.app.epbmsystem.model.Entity.Permission;
import com.app.epbmsystem.repository.PermissionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
        @Autowired
        final PermissionRepository permissionRepository;
        Permission permission;
    final Logger LOG = LogManager.getLogger(PermissionService.class);

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;

    }

    public ResponseEntity<Object> listAllPermissions(){
        try {
           List<Permission> permissionList = permissionRepository.findAll();
            if (permissionList.isEmpty())
            {
                return new ResponseEntity<>("No Permissions exists in the database", HttpStatus.NOT_FOUND);
            }
            else
            {
                return new ResponseEntity<>(permissionList, HttpStatus.OK);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> savePermission(Permission permission) {
        permissionRepository.save(permission);
        return new ResponseEntity<>("permission Added /n Thank you for adding   ",HttpStatus.OK);
    }

    public ResponseEntity<Object> updatePermission(Permission permission){                  // Update user
        try{
            Long id = permission.getId();
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            if (permissionRepository.existsById(id)) {
                permissionRepository.save(permission);
                return new ResponseEntity<>("permission updated thank you", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("permission not exist", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deletePermission(Long id){                   //Delete user
        try {

            if (permissionRepository.existsById(id)) {
                permissionRepository.delete(permission);
                return new ResponseEntity<>(" permission has been Deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("permission Not exists Please enter Valid ID", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            LOG.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("permission deleted", HttpStatus.BAD_REQUEST);}
    }

    public ResponseEntity<Object> getPermission(Long id){
        try{
            Optional<Permission> permission = permissionRepository.findById(id);
            if (permission.isPresent())
            {return new ResponseEntity<>(permission, HttpStatus.FOUND); }
            else
            {return new ResponseEntity<>(permission, HttpStatus.NOT_FOUND); }
        }
        catch (Exception exception)
        {return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
    }


}
