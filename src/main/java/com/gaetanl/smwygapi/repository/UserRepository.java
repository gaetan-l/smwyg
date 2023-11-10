package com.gaetanl.smwygapi.repository;

import com.gaetanl.smwygapi.model.User;
import org.springframework.data.repository.CrudRepository;

@SuppressWarnings("unused")
public interface UserRepository extends CrudRepository<User, Integer> {
}
