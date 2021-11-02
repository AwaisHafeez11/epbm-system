package com.app.epbmsystem.service;

import com.app.epbmsystem.repository.UserRepository;
import com.app.epbmsystem.model.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class UserService {

    private static final Logger LOG = LogManager.getLogger(UserService.class);
    @Autowired
    UserRepository userRepository;
    User user;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<Object> listAllUsers(){
           try {

               List<User> userList= userRepository.findAll();
               if (userList.isEmpty())
               {
                   return new ResponseEntity<>("No User exists in the database", HttpStatus.NOT_FOUND);
               }
               else
               {
                   return new ResponseEntity<>(userList, HttpStatus.OK);
               }
           }
           catch (Exception e){
                   return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
           }
       }


       public ResponseEntity<Object> saveUser(User user) {
               userRepository.save(user);
               return new ResponseEntity<>("User Added /n Thank you for adding   ",HttpStatus.OK);
       }

       public ResponseEntity<Object> updateUser(User user){                  // Update user
            try {
                    Long id = user.getId();
                    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    if (userRepository.existsById(id)) {
                        userRepository.save(user);
                        return new ResponseEntity<>("User updated thank you", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("User not exist", HttpStatus.NOT_FOUND);
                    }
            }
            catch (Exception e)
            {
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
       }


       public ResponseEntity<Object> deleteUser(Long id){                   //Delete user
           try {

               if (userRepository.existsById(id)) {
                   userRepository.delete(user);
                   return new ResponseEntity<>(" User has been Deleted", HttpStatus.OK);
               } else {
                   return new ResponseEntity<>("user Not exists Please enter Valid ID", HttpStatus.NOT_FOUND);
               }
           }
           catch (Exception e){
               LOG.info("Exception: " + e.getMessage());
               return new ResponseEntity<>("User deleted", HttpStatus.BAD_REQUEST);}
       }


       public ResponseEntity<Object> getUser(Long id){
           try {
               Optional<User> user = userRepository.findById(id);
               if (user.isPresent())
                    {return new ResponseEntity<>(user, HttpStatus.FOUND); }
               else
                    {return new ResponseEntity<>(user, HttpStatus.NOT_FOUND); }
           }
           catch (Exception exception)
                    {return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
       }


       public ResponseEntity<Object> Authentication(String email, String password) {
           try {
               Optional<User> user = userRepository.findUserByEmailAndPassword(email, password);
               if (user.isPresent()) {
                   return new ResponseEntity<>("You are successfully Logged in", HttpStatus.OK);
               } else {
                   return new ResponseEntity<>("You are entering wrong email or Password", HttpStatus.NOT_FOUND);
               }
           } catch (Exception exception) {
               LOG.info("Exception: " + exception.getMessage());
               return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
           }
       }



    }
