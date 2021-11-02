package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Entity.Category;
import com.app.epbmsystem.model.Entity.User;
import com.app.epbmsystem.repository.CategoryRepository;
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
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    Category category;


    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private static final Logger LOG = LogManager.getLogger(CategoryService.class);

    public ResponseEntity<Object> listAllCategories(){
        try {

            List<Category> categoryList= categoryRepository.findAll();
            if (categoryList.isEmpty())
            {
                return new ResponseEntity<>("No Category exists in the database", HttpStatus.NOT_FOUND);
            }
            else
            {
                return new ResponseEntity<>(categoryList, HttpStatus.OK);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> saveCategory(Category category) {
        try{ categoryRepository.save(category);
            return new ResponseEntity<>("User Added /n Thank you for adding   ",HttpStatus.OK);}
        catch (Exception e){return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
    }

    public ResponseEntity<Object> updateCategory(Category category){                  // Update user
        try{
            Long id = category.getId();
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            if (categoryRepository.existsById(id)) {
                categoryRepository.save(category);
                return new ResponseEntity<>("Category updated thank you", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Category not exist", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteCategory(Long id){                   //Delete user
        try {

            if (categoryRepository.existsById(id)) {
                categoryRepository.delete(category);
                return new ResponseEntity<>(" Category has been Deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Category Not exists Please enter Valid ID", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            LOG.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("Category deleted", HttpStatus.BAD_REQUEST);}
    }

    public ResponseEntity<Object> getCategory(Long id){
        try{
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isPresent())
            {return new ResponseEntity<>(category, HttpStatus.FOUND); }
            else
            {return new ResponseEntity<>(category, HttpStatus.NOT_FOUND); }
        }
        catch (Exception exception)
        {return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
    }



}
