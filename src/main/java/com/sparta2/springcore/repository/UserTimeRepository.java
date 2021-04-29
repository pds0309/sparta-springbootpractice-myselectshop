package com.sparta2.springcore.repository;


import com.sparta2.springcore.model.User;
import com.sparta2.springcore.model.UserTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTimeRepository extends JpaRepository<UserTime, Long> {
    UserTime findByUser(User user);
}

