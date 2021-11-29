package com.app.epbmsystem.repository;

import com.app.epbmsystem.model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findAllByActive(boolean active);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByIdAndEmailTokenAndSmsToken(long id ,String emailToken, String smsToken);
    Optional<User> findUserByEmailAndPassword(String email, String password);

    Optional<User> findUserByUsername(String username);
}
