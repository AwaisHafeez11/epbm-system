package com.app.epbmsystem.controller.Entity;

import com.app.epbmsystem.Config.JwtTokenUtil;
import com.app.epbmsystem.model.Entity.JwtRequest;
import com.app.epbmsystem.model.Entity.JwtResponse;
import com.app.epbmsystem.model.Entity.User;
import com.app.epbmsystem.service.UserService;
import com.app.epbmsystem.util.ResponseHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.text.ParseException;
import java.util.Optional;

@CrossOrigin
@EnableSwagger2
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger LOG =  LogManager.getLogger(UserController.class);
    private static String token="awais1234";

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


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

            LOG.info("Listing all the users that are not active");
            return userService.listOfInActiveUsers();

    }

    /**
     * List of all Inactive users will
     * @param token
     * @return
     */
    @GetMapping("/list/active")
    public ResponseEntity<Object> listOfActiveUsers(@RequestHeader("Authorization") String token) throws ParseException {

            LOG.info("Listing all the users that are active");
            return userService.listOfActiveUsers();
    }

    /**
     * User Signup by this Controler
     * @param user
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<Object> addUser(@RequestBody User user,@RequestHeader("Authorization") String token) throws ParseException {

        LOG.info("Adding the user");
        return userService.addUser(user);

    }

//    /**
//     * User can Login by his token, email and password
//     * @param token
//     * @param email
//     * @param password
//     * @return
//     */
//    @GetMapping("/login")
//    public ResponseEntity<Object> login(@RequestHeader("Authorization") String token, @RequestParam String email, @RequestParam String password) throws ParseException {
//        if (authorization(token)) {
//            LOG.info("User is trying to login the system");
//            return userService.loginUser(email, password);
//        } else {
//            return UnAuthorizeUser();
//        }
//    }

    /**
     * User can verify his account by his email and phone number otp
     * @param token
     * @param id
     * @param emailToken
     * @param smsToken
     * @return
     */
    @GetMapping("/verification")
    public ResponseEntity<Object> AccountVerification(@RequestHeader("Authorization") String token, @RequestHeader Long id, @RequestHeader String emailToken, @RequestHeader String smsToken) throws ParseException {

            LOG.info("Verifying account by token");
            return userService.AccountVerification(id, emailToken, smsToken);


    }

    /**
     * User can resend OTP after expired previous received token
     * @param token
     * @param email
     * @return
     */
    @PutMapping ("/resendVerificationToken")
    public ResponseEntity<Object> resendVerificationToken(@RequestHeader("Authorization") String token,@RequestHeader  String email ) throws ParseException {

            LOG.info("Resending the verification tokens");
            return userService.resendVerificationToken(email);

    }

    /**
     * List of all active and inactive users will display
     * @param token
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<Object> listOfUsers(@RequestHeader("Authorization") String token) throws ParseException {
            LOG.info("Unauthorized user trying to access the database");
            return UnAuthorizeUser();

    }

    /**
     * User can by its id
     * @param token
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserByID(@RequestHeader("Authorization") String token, @PathVariable Long id) throws ParseException {

        LOG.info("Calling user service to get User by id");
            return userService.getUser(id); //It will return the Response

    }

    /**
     * user can be updated by this method
     * @param token
     * @param user
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<Object> UpdateUser(@RequestHeader("Authorization") String token, @RequestBody User user) throws ParseException {

            LOG.info("calling Update Method of userService");
            return userService.updateUser(user);

    }

    /**
     * Set user as Inactive/Delete from the DB
     * @param id
     * @param token
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> DeleteUser(@PathVariable Long id, @RequestHeader("Authorization") String token) throws ParseException {

        LOG.info("calling userService's method of deleteUser");
                return userService.deleteUser(id);

    }

    @GetMapping("/UserEducationalForms")
    public ResponseEntity<Object> UserEducationalForms(@RequestHeader("id") Long id,@RequestHeader("status") String status ,@RequestHeader("Authorization")String token) throws ParseException {
       LOG.info("Calling method ListOFUserEducationalForms from user Service ");
          return userService.ListOFUserEducationalForms(id,status);

    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());

        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUserName());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }


    private void authenticate(String userName, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
