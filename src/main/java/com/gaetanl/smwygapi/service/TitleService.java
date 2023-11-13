package com.gaetanl.smwygapi.service;

import com.gaetanl.smwygapi.model.Genre;
import com.gaetanl.smwygapi.model.Title;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface TitleService {
    /**
     * Returns a title.
     *
     * @return                     a list of titles
     * @throws IOException         during Jackson deserialization
     * @throws URISyntaxException  during API call URI building
     */
    @NonNull
    Optional<Title> read(@NonNull final String id) throws URISyntaxException, IOException;

    /**
     * Returns a list of titles.
     *
     * @param  index               the index used to order the results
     * @param  page                the page of result to return, defaults to 1
     *                             if empty
     * @return                     a list of titles
     * @throws IOException         during Jackson deserialization
     * @throws URISyntaxException  during API call URI building
     */
    @NonNull List<Title> readAll(@Nullable final Title.TitleIndex index, @Nullable final Integer page) throws URISyntaxException, IOException;

    /**
     * Returns a list of titles.
     *
     * @param  index               the index used to order the results
     * @param  page                the page of result to return, defaults to 1
     *                             if empty
     * @param genres               the set of genres to look for
     * @return                     a list of titles
     * @throws IOException         during Jackson deserialization
     * @throws URISyntaxException  during API call URI building
     */
    @NonNull List<Title> readAllByGenres(@Nullable final Title.TitleIndex index, @Nullable final Integer page, @NonNull final Set<Genre> genres) throws URISyntaxException, IOException;

    @NonNull Genre getGenre(final int id) throws URISyntaxException, IOException;
}
