package com.chat.user.repositories;

import com.chat.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,String> {

    Optional<User> findByMobile(String mobile);

    @Override
    void deleteById(String id);
}
