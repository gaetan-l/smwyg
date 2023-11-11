package com.gaetanl.smwygapi.service;

import com.gaetanl.smwygapi.model.Title;
import com.gaetanl.smwygapi.model.User;
import com.gaetanl.smwygapi.util.ApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteService.class);

    @Autowired
    UserService userService;

    /**
     * Adds the specified title to the specified user's favorites list. User
     * and title existence are assumed, no checks are performed here.
     *
     * @param  user   the owner of the favorites list
     * @param  title  the title to add to the favorites list
     * @return        the user with the updated favorites list persisted
     */
    public @NonNull User create(@NonNull final User user, @NonNull final Title title) {
        ApiUtil.logMethodExecution(logger, "create", String.format("{Title with id=%s} to {User with id=%s}", user.getIdAsString(), title.getIdAsString()));
        user.getFavorites().add(title.getIdAsString());
        return userService.update(user);
    }

    /**
     * Removes the specified title from the specified user's favorites list.
     * User and title existence are assumed, no checks are performed here.
     *
     * @param  user   the owner of the favorites list
     * @param  title  the title to remove from the favorites list
     * @return        the user with the updated favorites list persisted
     */
    public @NonNull User delete(@NonNull final User user, @NonNull final Title title) {
        ApiUtil.logMethodExecution(logger, "delete", String.format("{Title with id=%s} to {User with id=%s}", user.getIdAsString(), title.getIdAsString()));
        user.getFavorites().remove(title.getIdAsString());
        return userService.update(user);
    }
}
