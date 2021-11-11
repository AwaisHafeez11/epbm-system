package com.app.epbmsystem.service;

import com.app.epbmsystem.model.Entity.Category;
import com.app.epbmsystem.repository.CategoryRepository;
import com.app.epbmsystem.util.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    /**
     * This Method save category into DB
     * @param category
     * @return
     */
    public ResponseEntity<Object> saveCategory(Category category) {
        try{
            Optional<Category> existingCategory= categoryRepository.findCategoryByName(category.getName());
                if(existingCategory.isPresent())
                {
                    if(existingCategory.get().isActive())
                    {
                        return new ResponseEntity<>("Category Already exists", HttpStatus.BAD_REQUEST);
                    }
                    else
                    {
                        category.setActive(true);
                        category.setUpdatedDate(DateTime.getDateTime());
                        categoryRepository.save(category);
                        return new ResponseEntity<>("Category added", HttpStatus.OK);
                    }
                }
                else
                {
                category.setCreatedDate(DateTime.getDateTime());
                category.setUpdatedDate(DateTime.getDateTime());
                categoryRepository.save(category);
                return new ResponseEntity<>("Category added",HttpStatus.OK);
                }

            }
        catch (Exception e){return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);}
    }

    /**
     * This Method update category into DB
     * @param category
     * @return
     */
    public ResponseEntity<Object> updateCategory(Category category){                  // Update user
        try{
            Long id = category.getId();
            if (categoryRepository.existsById(id)) {

                category.setUpdatedDate(DateTime.getDateTime());
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

    /**
     * This method will inactive already existing category
     * @param id
     * @return
     */
    public ResponseEntity<Object> deleteCategory(Long id){                   //Delete user
        try {
            Optional<Category> category= categoryRepository.findById(id);
            if (category.isPresent()) {
                category.get().setActive(false);
                category.get().setUpdatedDate(DateTime.getDateTime());
                categoryRepository.save(category.get());
                return new ResponseEntity<>(" Category has been Deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Category Not exists Please enter Valid ID", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            LOG.info("Exception: " + e.getMessage());
            return new ResponseEntity<>("Category deleted", HttpStatus.BAD_REQUEST);}
    }

    /**
     * This method display category by getting its id from DB
     * @param id
     * @return
     */
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

    /**
     * List of all Active categories will display if exists
     * @return
     */
    public ResponseEntity<Object> listAllActiveCategories() {
        try {
            List<Category> categoryList = categoryRepository.findAllByActive(true);
            if (categoryList.isEmpty()) {
                return new ResponseEntity<>("There are no active Category in the database", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(categoryList, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * List of all inactive categories will display if exists
     */
    public ResponseEntity<Object> listAllInactiveCategories() {
        try {
            List<Category> categoryList = categoryRepository.findAllByActive(false);
            if (categoryList.isEmpty()) {
                return new ResponseEntity<>("There are no Inactive Category in the database", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(categoryList, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.info("Exception"+ e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
