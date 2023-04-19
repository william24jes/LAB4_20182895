package com.example.lab4_20182895.Repository;

import com.example.lab4_20182895.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
