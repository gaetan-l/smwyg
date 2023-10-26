package com.gaetanl.smwygapi.repository;

import com.gaetanl.smwygapi.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
