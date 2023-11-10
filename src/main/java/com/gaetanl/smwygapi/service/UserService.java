package com.gaetanl.smwygapi.service;

import com.gaetanl.smwygapi.model.User;
import com.gaetanl.smwygapi.repository.UserRepository;
import com.gaetanl.smwygapi.util.ApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public @NonNull User create(final User user) {
        ApiUtil.logMethodExecution(logger, "create", user);
        return userRepository.save(user);
    }

    public Optional<User> read(final int id) {
        ApiUtil.logMethodExecution(logger, "read", "id=" + id);
        return userRepository.findById(id);
    }

    public @NonNull List<User> readAll() {
        ApiUtil.logMethodExecution(logger, "readAll");
        final List<User> userList = new ArrayList<>();
        userRepository.findAll().forEach(userList::add);
        return userList;
    }

    public @NonNull User update(final User user) {
        ApiUtil.logMethodExecution(logger, "update", user);
        return userRepository.save(user);
    }

    public void delete(final User user) {
        ApiUtil.logMethodExecution(logger, "delete", user);
        userRepository.delete(user);
    }
}
