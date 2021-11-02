package com.app.epbmsystem.repository;

import com.app.epbmsystem.model.Entity.Category;
import com.app.epbmsystem.model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>{

}
