package com.gaetanl.smwygapi.service;

import com.gaetanl.smwygapi.dto.SmwygSearchParametersDto;
import com.gaetanl.smwygapi.model.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

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
     * @param  genres              the set of genres to look for
     * @return                     a list of titles
     * @throws IOException         during Jackson deserialization
     * @throws URISyntaxException  during API call URI building
     */
    @NonNull List<Title> readAllByGenres(@Nullable final Title.TitleIndex index, @Nullable final Integer page, @NonNull final Set<Genre> genres) throws URISyntaxException, IOException;

    /**
     * Returns a genre.
     *
     * @param  id                  the id of the genre to return
     * @return                     the genre corresponding to the id
     * @throws IOException         during Jackson deserialization
     * @throws URISyntaxException  during API call URI building
     */
    @NonNull Genre readGenre(final int id) throws URISyntaxException, IOException;

    /**
     * Returns titles similar to the one specified.
     *
     * @param   id                  the id of title of reference
     * @param   index               the index used to order the results
     * @param   page                the page of result to return, defaults to 1
     * @return                      a list of similar titles
     * @throws  IOException         during Jackson deserialization
     * @throws  URISyntaxException  during API call URI building
     */
    @NonNull List<Title> readSimilar(@NonNull final String id, @Nullable final Title.TitleIndex index, @Nullable final Integer page) throws URISyntaxException, IOException;

    /**
     * Advanced search function using a similarity profile.
     * {@link com.gaetanl.smwygapi.model.SimilarityProfile}
     *
     * @param  searchParametersDto the parameters of the search
     * @param  index               the index used to order the results
     * @param  page                the page of result to return, defaults to 1
     * @return                     a list of titles corresponding to the
     *                             parameters
     * @throws IOException         during Jackson deserialization
     * @throws URISyntaxException  during API call URI building
     */
    @NonNull List<Title> search(
            @NonNull SmwygSearchParametersDto searchParametersDto,
            @Nullable final Title.TitleIndex index,
            @Nullable final Integer page) throws URISyntaxException, IOException;

    /**
     * Simple search function by name.
     *
     * @param  query               the text query for the search
     * @param  index               the index used to order the results
     * @param  page                the page of result to return, defaults to 1
     * @return                     a list of titles corresponding to the
     *                             parameters
     * @throws IOException         during Jackson deserialization
     * @throws URISyntaxException  during API call URI building
     */
    @NonNull List<Title> searchByName(
            @NonNull String query,
            @Nullable final Title.TitleIndex index,
            @Nullable final Integer page) throws URISyntaxException, IOException;
}
