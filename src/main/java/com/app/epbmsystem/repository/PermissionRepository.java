package com.app.epbmsystem.repository;

import com.app.epbmsystem.model.Entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {

    Optional<Permission> findPermissionByName(String name);
    List<Permission> findAllByActive(boolean active);

}
