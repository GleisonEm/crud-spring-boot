package com.example.crudWithSolid.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.crudWithSolid.entity.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findByEmail(String email);

    List<User> findByName(String name);
}
