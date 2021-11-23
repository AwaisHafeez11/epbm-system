package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Forms.EducationalForm;
import com.app.epbmsystem.repository.UserRepository;
import com.app.epbmsystem.model.Entity.User;
import com.app.epbmsystem.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.constraints.Null;

@Service
public class UserService {

    private static final Logger LOG = LogManager.getLogger(UserService.class);
    private Date tokenExpireTime = null;

    final UserRepository userRepository;
    final EmailNotification emailNotification;
    final SmsNotification smsNotification;


    /**
     * Autowiring through constructor
     * @param userRepository
     * @param emailNotification
     * @param smsNotification
     */
    public UserService(UserRepository userRepository, EmailNotification emailNotification, SmsNotification smsNotification) {
        this.userRepository = userRepository;
        this.emailNotification = emailNotification;
        this.smsNotification = smsNotification;
    }

    /**
     * This method let user login to the system
     * @param email
     * @param password
     * @return
     */
    public ResponseEntity<Object> loginUser(String email, String password) throws ParseException {
        try {
            Optional<User> user = userRepository.findUserByEmailAndPassword(email, password);
            if (user.isPresent()) {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"user Logged in",null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"You are entering wrong credentials",user);
            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception ",null);

        }
    }

    /**
     * This method verify user by sending otp through email and sms
     * @param id
     * @param emailToken
     * @param smsToken
     * @return
     */
    public ResponseEntity<Object> AccountVerification(long id, String emailToken, String smsToken) throws ParseException {
        try {
            Optional<User> user = userRepository.findUserByIdAndEmailTokenAndSmsToken(id,emailToken,smsToken);
            Date verificationTime = DateTime.getDateTime();
            System.out.println(tokenExpireTime);

            if(verificationTime.after(tokenExpireTime)){
                return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,true,"The token is expired",null);
            }
            else{
                if (user.isPresent()) {
                    user.get().setActive(true);
                    userRepository.save(user.get());
                    return ResponseHandler.generateResponse(HttpStatus.OK,false,"User account has been verified, now you can login",null);
                } else {
                    return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,true,"Your are entering wrong values for tokens",user);
                }
            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Your are entering wrong values for tokens"+ e.getMessage(),null);


        }
    }

    /**
     * The method can let user signup into the system
     * @param user
     * @return
     */
    public ResponseEntity<Object> addUser(User user) throws ParseException {
        try {
            Optional<User> existingUser = userRepository.findUserByEmail(user.getEmail());
            if (existingUser.isPresent()) {
                if (existingUser.get().isActive()) {
                    return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,true,"User already present",user);
                } else {
                    existingUser.get().setActive(true);
                    userRepository.save(existingUser.get());
                    return ResponseHandler.generateResponse(HttpStatus.OK,false,"Inactive User already exists. User activated successfully. Enjoy Bait ul Mall Services",existingUser);
                }
            } else {
                Random rnd = new Random(); //Generating a random number
                int emailToken = rnd.nextInt(99999) + 10000; //Generating a random number of 6 digits
                emailNotification.sendMail(user.getEmail(), "Hello "+user.getFirstName()+ "!  This message is from Pakistan Bait Ul Maal." + emailToken + " is your otp To get registered over here.");
                user.setEmailToken(emailToken + "");

                int smsToken = rnd.nextInt(99999) + 10000;
                smsNotification.Notification(user.getPhoneNumber(), "Hello "+user.getFirstName()+ "!  This message is from Pakistan Bait Ul Maal." + smsToken + " is your otp To get registered over here.");
                user.setSmsToken(smsToken + "");
                tokenExpireTime = DateTime.getExpireTime();
                user.setActive(false); //the user is active in the start
                user.setCreatedDate(DateTime.getDateTime());
                userRepository.save(user);
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"User is successfully added",user);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception" + e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,""+e.getMessage(),null);
        }
    }

    /**
     * update existing user into the database
     * @param user
     * @return
     */
    public ResponseEntity<Object> updateUser(User user) throws ParseException {
        try {
            Optional<User> existingUser = userRepository.findById(user.getId());
            if (existingUser.isPresent()) {

                user.setUpdatedDate(SqlDate.getDateInSqlFormat());
                userRepository.save(user);
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"User has been successfully Updated",null);
            }
            else
            {
                LOG.info("The user you are trying to update doesn't exist. ");
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"User not exists, Please update existing user",null);
            }
        }
        catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,true,"User not Updated",null);

        }
    }

    /**
     * this method delete user by setting it active(false)
     * @param id
     * @return
     */
    public ResponseEntity<Object> deleteUser(Long id) throws ParseException {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent())
            {
                user.get().setUpdatedDate(DateTime.getDateTime());
                user.get().setActive(false);
                userRepository.save(user.get());
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"User is successfully deleted",null);
            }
            else
            {
                return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,true,"There is no user against this id",user);
            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"This user doesn't exist in the database",null);
        }
    }

    /**
     * this method display user by its existing id
     * @param id
     * @return
     */
    public ResponseEntity<Object> getUser(Long id) throws ParseException {
           try {
               Optional<User> user = userRepository.findById(id);
               if (user.isPresent())
                    {return ResponseHandler.generateResponse(HttpStatus.OK,false,"User found",user);}
               else
                    {return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"User not found", null);}
           }
           catch (Exception exception)
                    {return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception",null);}
       }

    /**
     *List of all active and Inactive users
     * @return
     */
    public ResponseEntity<Object> listAllUsers() throws ParseException {
        try {

            List<User> userList= userRepository.findAll();
            if (userList.isEmpty())
            {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"No User exists in the database",null);
            }
            else
            {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of All users",userList);
            }
        }
        catch (Exception e){
            LOG.info("Exception throws "+ e.getMessage() );
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception"+e.getMessage(),null);

        }
    }

    /**
     * List of all active users will display
     * @return
     */
    public ResponseEntity<Object> listOfActiveUsers() throws ParseException {
        try {
            List<User> userList = userRepository.findAllByActive(true);
            if (userList.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no users in the database",null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of active users",userList);            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
    }
    }

    /**
     *List of all inactive user will display
     * @return
     */
    public ResponseEntity<Object> listOfInActiveUsers() {
        try{
            List<User> userList = userRepository.findAllByActive(false);
            if (userList.isEmpty()) {
                return new ResponseEntity<>("There are no users in the database", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(userList, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * User can receive verification code on its mail and contact no in this method
     * @param email
     * @return
     */
    public ResponseEntity<Object> resendVerificationToken(String email) throws ParseException {
        try{
            Optional<User> user = userRepository.findUserByEmail(email);
            if(user.isPresent()){
                Random rnd = new Random(); //Generating a random number
                int emailToken = rnd.nextInt(999999) + 100000; //Generating a random number of 6 digits
                emailNotification.sendMail(user.get().getEmail(), "Your verification code is: " + emailToken);
                user.get().setEmailToken(emailToken+"");

                int smsToken = rnd.nextInt(999999) + 100000;
                smsNotification.Notification(user.get().getPhoneNumber(), "Your verification code: " + smsToken);
                user.get().setSmsToken(smsToken + "");
                tokenExpireTime = DateTime.getExpireTime();
                userRepository.save(user.get());
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"Tokens are successfully resent to your email and phone number",null);
        }
            else
                {
                    return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,true,"User doesn't exist against this email",null);
                }
        }catch (Exception e) {
            LOG.info("Exception: "+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception" + e.getMessage(),null);

        }
    }

    public ResponseEntity<Object> ListOFUserEducationalForms(long uid,String status) throws ParseException {
        Optional<User> users= userRepository.findById(uid);
                if(users.isPresent()){
            List<EducationalForm> educationalForms = users.get().getEducationalForms();

            List<EducationalForm> educationalFormStream = educationalForms.stream().filter(educationalForm -> educationalForm.getApplicationStatus().equals(status)).collect(Collectors.toList());
            return ResponseHandler.generateResponse(HttpStatus.OK,false,"Listing of educational form of user ID"+uid,educationalFormStream);
        }
        else
        {
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"No applications found for the user Id: "+uid,null);
        }
    }




//    @Override
//    public UserDetails loadUserByUsername(String firstName) throws UsernameNotFoundException {
//        Optional<User> user = userRepository.findUserByFirstName(firstName);
//        if (user.isPresent()) {
//            return new org.springframework.security.core.userdetails.User(user.get().getFirstName(), user.get().getPassword(),new ArrayList<>());
//        } else {
//            throw new UsernameNotFoundException("User not found with username: " + firstName);
//        }
//    }
}