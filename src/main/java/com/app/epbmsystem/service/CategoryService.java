package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Entity.Category;
import com.app.epbmsystem.repository.CategoryRepository;
import com.app.epbmsystem.util.DateTime;
import com.app.epbmsystem.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.ws.Response;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    Category category;

    /**
     * Constructor
     * @param categoryRepository
     */
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private static final Logger LOG = LogManager.getLogger(CategoryService.class);

    /**
     * List of all active and inactive categories
     * @return
     */
    public ResponseEntity<Object> listAllCategories() throws ParseException {
        try {

            List<Category> categoryList= categoryRepository.findAll();
            if (categoryList.isEmpty())
            {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"No Category exists in the databas",null);
            }
            else
            {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of all categories",categoryList);
            }
        }
        catch (Exception e){
            LOG.info("Exception: "+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * This Method save category into DB
     * @param category
     * @return
     */
    public ResponseEntity<Object> saveCategory(Category category) throws ParseException {
        try {
            Optional<Category> existingCategory = categoryRepository.findCategoryByName(category.getName());
            if (existingCategory.isPresent()) {
                if (existingCategory.get().isActive()) {
                    return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, "Category Already exists", null);
                } else {
                    category.setActive(true);
                    category.setUpdatedDate(DateTime.getDateTime());
                    categoryRepository.save(category);
                    return ResponseHandler.generateResponse(HttpStatus.OK, false, "Category activated", null);
                }
            } else {
                category.setCreatedDate(DateTime.getDateTime());
                category.setUpdatedDate(DateTime.getDateTime());
                categoryRepository.save(category);
                return ResponseHandler.generateResponse(HttpStatus.OK, false, "Category added", null);
            }

        } catch (Exception e) {
            LOG.info("Exception: " + e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, true, "Exception: " + e.getMessage(), null);
        }
    }

    /**
     * This Method update category into DB
     * @param category
     * @return
     */
    public ResponseEntity<Object> updateCategory(Category category) throws ParseException {                  // Update user
        try{
            Long id = category.getId();
            if (categoryRepository.existsById(id)) {

                category.setUpdatedDate(DateTime.getDateTime());
                categoryRepository.save(category);
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"Category updated thank you",null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,true,"Category not exist",null);
            }
        }
        catch (Exception e)
        {
            LOG.info("Exception: "+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * This method will inactive already existing category
     * @param id
     * @return
     */
    public ResponseEntity<Object> deleteCategory(Long id) throws ParseException {                   //Delete user
        try {
            Optional<Category> category= categoryRepository.findById(id);
            if (category.isPresent()) {
                category.get().setActive(false);
                category.get().setUpdatedDate(DateTime.getDateTime());
                categoryRepository.save(category.get());
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"Category has been Deleted",null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"Category Not exists Please enter Valid ID",null);
            }
        }
        catch (Exception e){
        LOG.info("Exception: " + e.getMessage());
        return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);}
    }

    /**
     * This method display category by getting its id from DB
     * @param id
     * @return
     */
    public ResponseEntity<Object> getCategory(Long id) throws ParseException {
        try{
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isPresent())
            {
            return ResponseHandler.generateResponse(HttpStatus.FOUND,false,"Category exists by id: "+id,category);}
            else
            {
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,false,"Category not found for the entered id: "+id,category);}
        }
        catch (Exception exception)
        {
        return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+exception.getMessage(),null);
        }
    }

    /**
     * List of all Active categories will display if exists
     * @return
     */
    public ResponseEntity<Object> listAllActiveCategories() throws ParseException {
        try {
            List<Category> categoryList = categoryRepository.findAllByActive(true);
            if (categoryList.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no active Category in the database",null);
            } else {
                return  ResponseHandler.generateResponse(HttpStatus.FOUND,false,"List of active categories",categoryList);
            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }

    /**
     * List of all inactive categories will display if exists
     */
    public ResponseEntity<Object> listAllInactiveCategories() throws ParseException {
        try {
            List<Category> categoryList = categoryRepository.findAllByActive(false);
            if (categoryList.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,"There are no Inactive Category in the database",null);
            } else {
                return ResponseHandler.generateResponse(HttpStatus.OK,false,"List of in active users",categoryList);
            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,true,"Exception: "+e.getMessage(),null);
        }
    }



}
