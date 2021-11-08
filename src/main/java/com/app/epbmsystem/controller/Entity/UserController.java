package com.app.epbmsystem.controller.Entity;

import com.app.epbmsystem.model.Entity.User;
import com.app.epbmsystem.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@RestController
@RequestMapping("/users")
@Api(value="User Operations - CRUD REST API's for the User")
public class UserController {
    private static final Logger LOG =  LogManager.getLogger(UserController.class);
    private static String token="awais1234";
    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public boolean authorization(String token) {
        LOG.info("Authorizing the user ");
        return UserController.token.equals(token);
    }
    public ResponseEntity<Object> UnAuthorizeUser() {
        LOG.info("Unauthorized user is trying to get access");
        return new ResponseEntity<>("Kindly do the authorization first", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/list/inactive")
    public ResponseEntity<Object> listOfInActiveUsers(@RequestHeader("Authorization") String token) {
        if (authorization(token)) {
            LOG.info("Listing all the users that are not active");
            return userService.listOfInActiveUsers();
        } else {
            return UnAuthorizeUser();
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> addUser(@RequestBody User user) {
        LOG.info("Adding the user");
        return userService.addUser(user);
    }

    @GetMapping("/login")
    public ResponseEntity<Object> login(@RequestHeader("Authorization") String token, @RequestParam String email, @RequestParam String password) {
        if (authorization(token)) {
            LOG.info("User is trying to login the system");
            return userService.loginUser(email, password);
        } else {
            return UnAuthorizeUser();
        }
    }


    @GetMapping("/verification")
    public ResponseEntity<Object> AccountVerification(@RequestHeader("Authorization") String token, @RequestHeader Long id, @RequestHeader String emailToken, @RequestHeader String smsToken) {
        if (authorization(token)) {
            LOG.info("Doing the account verification through tokens");
            return userService.AccountVerification(id, emailToken, smsToken);
        } else {
            return UnAuthorizeUser();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Object> listOfUsers(@RequestHeader("Authorization") String token) {
        if (!authorization(token)) {
            LOG.info("Unauthorized user trying to access the database");
            return UnAuthorizeUser();
        } else {
            return userService.listAllUsers();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestHeader("Authorization") String token, @RequestBody User user) {
        try{
            if (authorization(token)) {
                return userService.addUser(user);
            } else {
                LOG.info("Unauthorized user trying to access the database");
                return UnAuthorizeUser();
            }
        }catch (Exception e){
            LOG.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserByID(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (authorization(token)) {
            return userService.getUser(id); //It will return the Response
        } else {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser(); //If the user is not authorized
        }
    }

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





//    @PostMapping("/sms")
//    public ResponseEntity<Object> SendSms(@RequestHeader("Authorization") String token,@RequestHeader long id, @RequestBody String message) {
//
//        if(authorization(token)){
//            return userService.SendSms(id, message);
//        }
//        else{
//            LOG.info("UnAuthorized User was trying to access the database");
//            return UnAuthorizeUser();
//        }
//    }


}
