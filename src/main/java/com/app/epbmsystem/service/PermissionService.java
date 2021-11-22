package com.app.epbmsystem.service;



import com.app.epbmsystem.model.Entity.Category;
import com.app.epbmsystem.model.Entity.Permission;
import com.app.epbmsystem.repository.PermissionRepository;
import com.app.epbmsystem.util.DateTime;
import com.app.epbmsystem.util.ResponseHandler;
import com.app.epbmsystem.util.SqlDate;
import org.apache.http.client.protocol.ResponseProcessCookies;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.cert.ocsp.RespID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
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
    public ResponseEntity<Object> listAllPermissions() throws ParseException {
        try{
           List<Permission> permissionList = permissionRepository.findAll();
            if (permissionList.isEmpty())
                {
                    return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"No Permissions exists in the database",null);
                }
            else
                {
                    return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of all permissions",permissionList);
                }
            }
            catch (Exception e){
            LOG.info("Exception: "+ e.getMessage());
                return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);

            }
    }

    /**
     * permission added to the database by this method
     * @param permission
     * @return
     */
    public ResponseEntity<Object> savePermission(Permission permission) throws ParseException {
        try{
            Optional<Permission> existingPermission= permissionRepository.findPermissionByName(permission.getName());
            if(existingPermission.isPresent())
            {
                if(existingPermission.get().isActive())
                {
                    return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,true,"Permission already exists",null);
                }
                else
                {
                    permission.setActive(true);
                    permission.setUpdatedDate(SqlDate.getDateInSqlFormat());
                    permissionRepository.save(permission);
                    return ResponseHandler.generateResponse(HttpStatus.OK,false,"",null);
                }
            }
            else
            {
                permission.setCreatedDate(SqlDate.getDateInSqlFormat());
                permission.setUpdatedDate(SqlDate.getDateInSqlFormat());
                permissionRepository.save(permission);
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"permission added",permission);
            }

        }
        catch (Exception e) {
            LOG.info("Exception: "+e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * this method is use to update permission
     * @param permission
     * @return
     */
    public ResponseEntity<Object> updatePermission(Permission permission) throws ParseException {
        try{
            Long id = permission.getId();
            if (permissionRepository.existsById(id)) {
                permission.setUpdatedDate(SqlDate.getDateInSqlFormat());
                permissionRepository.save(permission);
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"permission updated thank you",null);
            }
            else
            {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"permission not exist",null);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception: "+e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * This method validate existence of permission and set inactive
     * @param id
     * @return
     */
    public ResponseEntity<Object> deletePermission(Long id) throws ParseException {
        try {
            Optional<Permission> existingPermission = permissionRepository.findById(id);
            if (existingPermission.isPresent())
            {
                if(existingPermission.get().isActive())
                {
                    existingPermission.get().setActive(false);
                    permissionRepository.save(existingPermission.get());
                    return ResponseHandler.generateResponse(HttpStatus.OK,false,"Permisssion Inactive/Deleted for DB",null);
                }
                else
                {
                    return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,true,"Permission already Deleted/Inactive at DB",null);
                }
            }
            else
            {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"permission Not exists Please enter Valid permission ID",null);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception: " + e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * This method get permission by its given id
     * @param id
     * @return
     */
    public ResponseEntity<Object> getPermission(Long id) throws ParseException {
        try
        {
            Optional<Permission> permission = permissionRepository.findById(id);
            if(permission.isPresent())
            {
                return ResponseHandler.generateResponse(HttpStatus.FOUND,false,"Permission exist by id: "+id,permission);
            }
            else
            {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Permission not exists",null);
            }
        }
        catch(Exception exception)
        {
            LOG.info("Exception: "+ exception.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+exception.getMessage(),null);
        }
    }

    /**
     * List of all inactive Permissions display by this method
     * @return
     */
    public ResponseEntity<Object> listActivePermisssions() throws ParseException {
        try
        {
            List<Permission> existingPermissions = permissionRepository.findAllByActive(true);

            if (existingPermissions.isEmpty())
            {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no active permissions in the DB",null);
            }
            else
            {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of active permissions ",existingPermissions);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception"+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * List of All inactive Permissions will display by this method
     * @return
     */
    public ResponseEntity<Object> listInactivePermisssions() throws ParseException {
        try
        {
            List<Permission> existingPermissions = permissionRepository.findAllByActive(false);

            if (existingPermissions.isEmpty())
            {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no active permissions in the DB",null);
            }
            else
            {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of inactive Permissions ",existingPermissions);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception"+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }
}
