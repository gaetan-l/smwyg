package com.gaetanl.smwygapi.service;

import com.gaetanl.smwygapi.model.Genre;
import com.gaetanl.smwygapi.model.Title;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Just an example to show how Spring chooses between multiple implementations
 * of a component using @Autowired. In this case, TitleServiceImplTmdb has
 * a @Primary annotation, which is why it is chosen instead of this class.
 */
@Service
@SuppressWarnings("all") // This is an example class not meant to be used, see doc above
public class TitleServiceImplOther implements TitleService {

    @Override
    public @NonNull Optional<Title> read(@NonNull final String id) throws URISyntaxException, IOException {
        return Optional.of(null);
    }

    @Override
    public @NonNull List<Title> readAll(@Nullable final Title.TitleIndex index, @Nullable final Integer page) throws URISyntaxException, IOException {
        return new ArrayList<>();
    }

    @Override
    public @NonNull List<Title> readAllByGenres(Title.TitleIndex index, Integer page, Set<String> genres) throws URISyntaxException, IOException {
        return null;
    }

    public @NonNull Map<Integer, Genre> getGenres() throws URISyntaxException, IOException {
        return null;
    }
}
