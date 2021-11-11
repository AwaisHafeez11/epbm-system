package com.app.epbmsystem.service;



import com.app.epbmsystem.model.Entity.Category;
import com.app.epbmsystem.model.Entity.Permission;
import com.app.epbmsystem.repository.PermissionRepository;
import com.app.epbmsystem.util.DateTime;
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

    final PermissionRepository permissionRepository;
    Permission permission;
    final Logger LOG = LogManager.getLogger(PermissionService.class);

    /**
     * Autowiring through constructor
     * @param permissionRepository
     */
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    /**
     * List of all active and inactive permissions
     * @return
     */
    public ResponseEntity<Object> listAllPermissions(){
        try{
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

    /**
     * permission added to the database by this method
     * @param permission
     * @return
     */
    public ResponseEntity<Object> savePermission(Permission permission) {
        try{
            Optional<Permission> existingPermission= permissionRepository.findPermissionByName(permission.getName());
            if(existingPermission.isPresent())
            {
                if(existingPermission.get().isActive())
                {
                    return new ResponseEntity<>("Permission already exists",HttpStatus.BAD_REQUEST);
                }
                else
                {
                    permission.setActive(true);
                    permission.setUpdatedDate(DateTime.getDateTime());
                    permissionRepository.save(permission);
                    return new ResponseEntity<>(" Activated deleted permission successfully ",HttpStatus.OK);
                }
            }
            else
            {
                permission.setCreatedDate(DateTime.getDateTime());
                permission.setUpdatedDate(DateTime.getDateTime());
                permissionRepository.save(permission);
                return new ResponseEntity<>("permission Added: Thank you for adding   ",HttpStatus.OK);
            }

        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    /**
     * this method is use to update permission
     * @param permission
     * @return
     */
    public ResponseEntity<Object> updatePermission(Permission permission){
        try{
            Long id = permission.getId();
            if (permissionRepository.existsById(id)) {
                permission.setUpdatedDate(DateTime.getDateTime());
                permissionRepository.save(permission);
                return new ResponseEntity<>("permission updated thank you", HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>("permission not exist", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This method validate existence of permission and set inactive
     * @param id
     * @return
     */
    public ResponseEntity<Object> deletePermission(Long id){
        try {
            Optional<Permission> existingPermission = permissionRepository.findById(id);
            if (existingPermission.isPresent())
            {
                if(existingPermission.get().isActive())
                {
                    existingPermission.get().setActive(false);
                    permissionRepository.save(existingPermission.get());
                    return new ResponseEntity<>("Permisssion Inactive/Deleted for DB",HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>("Permission already Deleted/Inactive at DB",HttpStatus.BAD_REQUEST);
                }
            }
            else
            {
                return new ResponseEntity<>("permission Not exists Please enter Valid permission ID", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("permission deleted", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * This method get permission by its given id
     * @param id
     * @return
     */
    public ResponseEntity<Object> getPermission(Long id){
        try
        {
            Optional<Permission> permission = permissionRepository.findById(id);
            if(permission.isPresent())
            {
                return new ResponseEntity<>(permission, HttpStatus.FOUND);
            }
            else
            {
                return new ResponseEntity<>("Permission not exists", HttpStatus.NOT_FOUND);
            }
        }
        catch(Exception exception)
        {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * List of all inactive Permissions display by this method
     * @return
     */
    public ResponseEntity<Object> listActivePermisssions(){
        try
        {
            List<Permission> existingPermissions = permissionRepository.findAllByActive(true);

            if (existingPermissions.isEmpty())
            {
                return new ResponseEntity<>("There are no active permissions in the DB ", HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(existingPermissions, HttpStatus.OK);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception"+ e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * List of All inactive Permissions will display by this method
     * @return
     */
    public ResponseEntity<Object> listInactivePermisssions(){
        try
        {
            List<Permission> existingPermissions = permissionRepository.findAllByActive(false);

            if (existingPermissions.isEmpty())
            {
                return new ResponseEntity<>("There are no active permissions in the DB ", HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(existingPermissions, HttpStatus.OK);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception"+ e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
