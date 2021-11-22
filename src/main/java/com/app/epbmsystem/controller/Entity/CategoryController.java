package com.app.epbmsystem.controller.Entity;

import com.app.epbmsystem.model.Entity.Category;

import com.app.epbmsystem.service.CategoryService;
import com.app.epbmsystem.util.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;

@EnableSwagger2
@RestController
@RequestMapping("/category")
public class CategoryController {
    private static final Logger LOG =  LogManager.getLogger(CategoryController.class);
    private static String token="awais1234";

    final CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public boolean authorization(String token) {
        LOG.info("Authorizing the user ");
        return CategoryController.token.equals(token);
    }

    public ResponseEntity<Object> UnAuthorizeUser() {
        LOG.info("Unauthorized user is trying to get access");
        return new ResponseEntity<>("Kindly do the authorization first", HttpStatus.UNAUTHORIZED);
    }

    /**
     * returns list of all Categories
     * @param token
     * @return
     * @throws ParseException
     */
    @GetMapping("/list")
    public ResponseEntity<Object> listOfCategories(@RequestHeader("Authorization") String token) throws ParseException {
        if (authorization(token)) {
            return categoryService.listAllCategories();
        } else {
            LOG.info("Unauthorized user trying to access the database");
            return UnAuthorizeUser();
        }
    }

    /**
     * Thid method will add category
     * @param token
     * @param category
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestHeader("Authorization") String token, @RequestBody Category category) throws ParseException {
        try{
            if (authorization(token))
            {
                return categoryService.saveCategory(category);
            }
            else
            {
                LOG.info("Unauthorized user trying to access the database");
                return UnAuthorizeUser();
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception: "+e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * returns a category by given id
     * @param token
     * @param id
     * @return
     * @throws ParseException
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCategoryByID(@RequestHeader("Authorization") String token, @PathVariable Long id) throws ParseException {
        if (authorization(token))
        {
            return categoryService.getCategory(id); //It will return the Response
        }
        else
        {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser(); //If the user is not authorized
        }
    }

    /**
     * Updates an existing category
     * @param token
     * @param category
     * @return
     * @throws ParseException
     */
    @PutMapping("/update")
    public ResponseEntity<Object> UpdateCategory(@RequestHeader("Authorization") String token, @RequestBody Category category) throws ParseException {
        if (authorization(token))
        {
            return categoryService.updateCategory(category);
        }
        else
        {
            LOG.info("UnAuthorized User was trying to access the database");
            return UnAuthorizeUser() ;
        }
    }

    /**
     * this method delete inactive category by its id
     * @param id
     * @param token
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> DeleteCategory(@PathVariable Long id, @RequestHeader("Authorization") String token) throws ParseException {

        if (authorization(token)) {
            try{
                return categoryService.deleteCategory(id);
            }catch (Exception exception){
                LOG.info("UnAuthorized User was trying to access the database");
                return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: ",null);
            }
        }
        else
        {
            return UnAuthorizeUser();
        }
    }

    /**
     * this method delete inactive category by its id
     * @param token
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Object> DeleteCategory(@RequestHeader("Authorization") String token, @RequestParam("delete") Long id) throws ParseException {
        if (authorization(token))
        {
            try
            {
                return categoryService.deleteCategory(id);
            }
            catch (Exception exception)
            {
                LOG.info("Exception: "+exception.getMessage());
                return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception:"+exception.getMessage(),null);
            }
        }
        else
        {
            return UnAuthorizeUser();
        }
    }

    /**
     * Returns a list of active categories
     * @param token
     * @return
     * @throws ParseException
     */
    @GetMapping("/active")
    public ResponseEntity<Object> ListOfActiveCategories(@RequestHeader("Authorizationn")String token) throws ParseException {
        if (authorization(token))
        {

                return categoryService.listAllActiveCategories();
        }
        else
        {
            return UnAuthorizeUser();
        }
    }

    /**
     * Returns a list of inactive categories
     * @param token
     * @return
     * @throws ParseException
     */
    @GetMapping("/inactive")
    public ResponseEntity<Object> ListOfInactiveCategories(@RequestHeader("Authorizationn")String token) throws ParseException {
        if (authorization(token))
        {

            return categoryService.listAllInactiveCategories();
        }
        else
        {
            return UnAuthorizeUser();
        }
    }
}
