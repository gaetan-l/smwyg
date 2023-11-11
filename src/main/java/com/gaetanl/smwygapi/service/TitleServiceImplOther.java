package com.gaetanl.smwygapi.service;

import com.gaetanl.smwygapi.model.Title;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Just an example to show how Spring chooses between multiple implementations
 * of a component using @Autowired. In this case, TitleServiceImplTmdb has
 * a @Primary annotation, which is why it is chosen instead of this class.
 */
@Service
public class TitleServiceImplOther implements TitleService {
    @Override
    public @NonNull List<Title> readAll(@Nullable final Integer page) throws URISyntaxException, IOException {
        return new ArrayList<>();
    }

    @Override
    public @NonNull Optional<Title> read(@NonNull final String id) throws URISyntaxException, IOException {
        return Optional.of(null);
    }
}
