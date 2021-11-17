package com.app.epbmsystem.controller.Entity;

import com.app.epbmsystem.model.Entity.User;
import com.app.epbmsystem.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger LOG =  LogManager.getLogger(UserController.class);
    private static String token="awais1234";
    final UserService userService;
    User user;

    /**
     * Autowiring through constructor
     * @param userService
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     *authorized Response for user
     * @param token
     * @return
     */
    public boolean authorization(String token) {
        LOG.info("Authorizing the user ");
        return UserController.token.equals(token);
    }

    /**
     * Unauthorized Response for user
     * @return
     */
    public ResponseEntity<Object> UnAuthorizeUser() {
        LOG.info("Unauthorized user is trying to get access");
        return new ResponseEntity<>("Kindly do the authorization first", HttpStatus.UNAUTHORIZED);
    }

    /**
     * List of all Inactive users display
     * @param token
     * @return
     */
    @GetMapping("/list/inactive")
    public ResponseEntity<Object> listOfInActiveUsers(@RequestHeader("Authorization") String token) {
        if (authorization(token)) {
            LOG.info("Listing all the users that are not active");
            return userService.listOfInActiveUsers();
        } else {
            return UnAuthorizeUser();
        }
    }

    /**
     * List of all Inactive users will
     * @param token
     * @return
     */
    @GetMapping("/list/active")
    public ResponseEntity<Object> listOfActiveUsers(@RequestHeader("Authorization") String token) {
        if (authorization(token)) {
            LOG.info("Listing all the users that are active");
            return userService.listOfActiveUsers();
        } else {
            return UnAuthorizeUser();
        }
    }

    /**
     * User Signup by this Controler
     * @param user
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<Object> addUser(@RequestBody User user,@RequestHeader("Authorization") String token) {
        if(authorization(token))
        {LOG.info("Adding the user");
        return userService.addUser(user);}
        else
        {return new ResponseEntity<>("Please Authorize first",HttpStatus.BAD_REQUEST);}
    }

    /**
     * User can Login by his token, email and password
     * @param token
     * @param email
     * @param password
     * @return
     */
    @GetMapping("/login")
    public ResponseEntity<Object> login(@RequestHeader("Authorization") String token, @RequestParam String email, @RequestParam String password) {
        if (authorization(token)) {
            LOG.info("User is trying to login the system");
            return userService.loginUser(email, password);
        } else {
            return UnAuthorizeUser();
        }
    }

    /**
     * User can verify his account by his email and phone number otp
     * @param token
     * @param id
     * @param emailToken
     * @param smsToken
     * @return
     */
    @GetMapping("/verification")
    public ResponseEntity<Object> AccountVerification(@RequestHeader("Authorization") String token, @RequestHeader Long id, @RequestHeader String emailToken, @RequestHeader String smsToken) {
        if (authorization(token)) {
            LOG.info("Verifying account by token");
            return userService.AccountVerification(id, emailToken, smsToken);
        } else {
            return UnAuthorizeUser();
        }
    }

    /**
     * User can resend OTP after expired previous received token
     * @param token
     * @param email
     * @return
     */
    @PutMapping ("/resendVerificationToken")
    public ResponseEntity<Object> resendVerificationToken(@RequestHeader("Authorization") String token,@RequestHeader  String email ){
        if (authorization(token)) {
            LOG.info("Resending the verification tokens");
            return userService.resendVerificationToken(email);
        } else {
            return UnAuthorizeUser();
        }
    }

    /**
     * List of all active and inactive users will display
     * @param token
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<Object> listOfUsers(@RequestHeader("Authorization") String token) {
        if (!authorization(token)) {
            LOG.info("Unauthorized user trying to access the database");
            return UnAuthorizeUser();
        } else {
            return userService.listAllUsers();
        }
    }

    /**
     * User can by its id
     * @param token
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserByID(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (authorization(token)) {
            return userService.getUser(id); //It will return the Response
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser(); //If the user is not authorized
        }
    }

    /**
     * user can be updated by this method
     * @param token
     * @param user
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<Object> UpdateUser(@RequestHeader("Authorization") String token, @RequestBody User user) {
        if (authorization(token)) {
            LOG.info("Updating the user");
            return userService.updateUser(user);
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser() ;
        }
    }

    /**
     * Set user as Inactive/Delete from the DB
     * @param id
     * @param token
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> DeleteUser(@PathVariable Long id, @RequestHeader("Authorization") String token) {

        if (authorization(token)) {
            try{
                return userService.deleteUser(id);
            }catch (Exception exception){
                LOG.info("UnAuthorized User was trying to access the database");
                return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else{
            return UnAuthorizeUser();
        }
    }


}
