package com.app.epbmsystem.service;

import com.app.epbmsystem.repository.UserRepository;
import com.app.epbmsystem.model.Entity.User;
import com.app.epbmsystem.util.DateTime;
import com.app.epbmsystem.util.EmailNotification;
import com.app.epbmsystem.util.SmsNotification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class UserService {

    private static final Logger LOG = LogManager.getLogger(UserService.class);
    private Date tokenExpireTime = null;

    final UserRepository userRepository;
    final EmailNotification emailNotification;
    final SmsNotification smsNotification;
    private User user;

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
    public ResponseEntity<Object> loginUser(String email, String password) {
        try {
            Optional<User> user = userRepository.findUserByEmailAndPassword(email, password);
            if (user.isPresent()) {
                return new ResponseEntity<>("You are successfully logged in", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("You are entering wrong credentials", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This method verify user by sending otp through email and sms
     * @param id
     * @param emailToken
     * @param smsToken
     * @return
     */
    public ResponseEntity<Object> AccountVerification(long id, String emailToken, String smsToken) {
        try {
            Optional<User> user = userRepository.findUserByIdAndEmailTokenAndSmsToken(id,emailToken,smsToken);
            Date verificationTime = DateTime.getDateTime();
            System.out.println(tokenExpireTime);

            if(verificationTime.after(tokenExpireTime)){
                return new ResponseEntity<>("The token is expired", HttpStatus.BAD_REQUEST);
            }
            else{
                if (user.isPresent()) {
                    user.get().setActive(true);
                    userRepository.save(user.get());
                    return new ResponseEntity<>("User account has been verified, now you can login", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Your are entering wrong values for tokens", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * The method can let user signup into the system
     * @param user
     * @return
     */
    public ResponseEntity<Object> addUser(User user) {
        try {
            Optional<User> existingUser = userRepository.findUserByEmail(user.getEmail());
            if (existingUser.isPresent()) {
                if (existingUser.get().isActive()) {
                    return new ResponseEntity<>("User already present", HttpStatus.BAD_REQUEST);
                } else {
                    existingUser.get().setActive(true);
                    userRepository.save(existingUser.get());
                    return new ResponseEntity<>("User is successfully added", HttpStatus.OK);
                }
            } else {
                Random rnd = new Random(); //Generating a random number
                int emailToken = rnd.nextInt(99999) + 10000; //Generating a random number of 6 digits
                emailNotification.sendMail(user.getEmail(), "Your verification code is: " + emailToken);
                user.setEmailToken(emailToken + "");

                int smsToken = rnd.nextInt(99999) + 10000;
                smsNotification.Notification(user.getPhoneNumber(), "Your  cverificationode: " + smsToken);
                user.setSmsToken(smsToken + "");
                tokenExpireTime = DateTime.getExpireTime();
                user.setActive(false); //the user is active in the start
                user.setCreatedDate(DateTime.getDateTime());
                userRepository.save(user);
                return new ResponseEntity<>("User is successfully added", HttpStatus.OK);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception" + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * update existing user into the database
     * @param user
     * @return
     */
    public ResponseEntity<Object> updateUser(User user) {
        try {
            user.setUpdatedDate(DateTime.getDateTime());
            userRepository.save(user);
            return new ResponseEntity<>("User has been successfully Updated", HttpStatus.OK);
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return new ResponseEntity<>("User is not Updated", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * this method delete user by setting it active(false)
     * @param id
     * @return
     */
    public ResponseEntity<Object> deleteUser(Long id) {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent())
            {
                user.get().setUpdatedDate(DateTime.getDateTime());
                user.get().setActive(false);
                userRepository.save(user.get());
                return new ResponseEntity<>("User is successfully deleted", HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>("There is no user against this id", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return new ResponseEntity<>("This user doesn't exist in the database", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * this method display user by its existing id
     * @param id
     * @return
     */
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

    /**
     *List of all active and Inactive users
     * @return
     */
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

    /**
     * List of all active users will display
     * @return
     */
    public ResponseEntity<Object> listOfActiveUsers() {
        try {
            List<User> userList = userRepository.findAllByActive(true);
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
    public ResponseEntity<Object> resendVerificationToken(String email){
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
                return new ResponseEntity<>("Tokens are successfully resent to your email and phone number", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("User doesn't exist against this email", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e) {
            LOG.info("Exception: "+ e.getMessage());
            return new ResponseEntity<>("There is no user against this email", HttpStatus.NOT_FOUND);
        }
    }
}