package com.gaetanl.smwygapi.service;

import com.gaetanl.smwygapi.model.Title;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Just an example to show how Spring chooses between multiple implementations
 * of a component using @Autowired. In this case, TitleServiceImplTmdb has
 * a @Primary annotation, which is why it is chosen instead of this class.
 */
@Service
public class TitleServiceImplOther implements TitleService {
    @Override
    public @NonNull List<Title> readAll(@Nullable final Integer page) {
        return new ArrayList<>();
    }
}
