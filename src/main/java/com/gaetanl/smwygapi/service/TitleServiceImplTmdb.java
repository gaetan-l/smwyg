package com.gaetanl.smwygapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gaetanl.smwygapi.dto.TmdbGenreDto;
import com.gaetanl.smwygapi.dto.TmdbMovieDto;
import com.gaetanl.smwygapi.model.Title;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
@Primary
public class TitleServiceImplTmdb implements TitleService {
    private static final Logger logger = LoggerFactory.getLogger(TitleServiceImplTmdb.class);

    private final Map<Integer, String> inMemoryGenres = new HashMap<>();

    @Override
    public @NonNull String getApiKey() {
        return "91b96d8d3dc851fb01aa9a36e8c81880";
    }

    @Override
    public @NonNull String getRootUri() {
        return "https://api.themoviedb.org/3";
    }

    @Override
    public @NonNull List<Title> getTitles(@Nullable Integer page) throws URISyntaxException, IOException {
        String path = "/movie/popular";
        String uriString = String.format("%s%s?api_key=%s%s",
                getRootUri(),
                path,
                getApiKey(),
                page == null ? "" : "&page=" + page);

        WebClient client = WebClient.create();
        URI uri = new URI(uriString);

        logger.info(uri.toString());
        String body = client.get()
                    .uri(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResult = objectMapper.readTree(body).get("results");
        ObjectReader objectReader = objectMapper.readerFor(new TypeReference<ArrayList<TmdbMovieDto>>() {});

        List<TmdbMovieDto> dtoTitles = objectReader.readValue(jsonResult);

        List<Title> pojoTitles = new ArrayList<>();
        for (TmdbMovieDto dtoTitle: dtoTitles) pojoTitles.add(dtoToPojoTitle(dtoTitle)); // NOTE: Can't use a lambda here because of exception cascading

        return pojoTitles;
    }

    /**
     * Transforms a TMDB title into a proper Title with the information that
     * interest us.
     *
     * @param dtoTitle the TmdbMovieDto to transform
     * @return the newly built Title
     * @throws IOException during Jackson deserialization
     * @throws URISyntaxException during API call URI building
     */
    private Title dtoToPojoTitle(TmdbMovieDto dtoTitle) throws IOException, URISyntaxException {
        Set<String> genres = new HashSet<>();
        for (Integer genreId: dtoTitle.genreIds) genres.add(getGenres().get(genreId)); // NOTE: Can't use a lambda here because of exception cascading

        return new Title(Integer.toString(dtoTitle.id), dtoTitle.title, genres);
    }

    /**
     * Returns the list of genres used by the TMDB API. Saves it locally and
     * uses it instead after the first call (we'll assume the genres won't
     * change during execution).
     *
     * @return a map of genre id and name used by the TMDB API
     * @throws IOException during Jackson deserialization
     * @throws URISyntaxException during API call URI building
     */
    private Map<Integer, String> getGenres() throws IOException, URISyntaxException {
        String path = "/genre/movie/list";
        String uriString = String.format("%s%s?api_key=%s",
                getRootUri(),
                path,
                getApiKey());

        /*
         * This list is not supposed to change, so the API call will only be
         * done once and populate the local map.
         */
        if (inMemoryGenres.isEmpty()) {
            WebClient client = WebClient.create();
            URI uri = new URI(uriString);

            logger.info(uri.toString());
            String body = client.get()
                    .uri(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonGenresList = objectMapper.readTree(body).get("genres");
            ObjectReader objectReader = objectMapper.readerFor(new TypeReference<ArrayList<TmdbGenreDto>>() {});

            List<TmdbGenreDto> genreDtoList = objectReader.readValue(jsonGenresList);
            for (TmdbGenreDto genreDto: genreDtoList) inMemoryGenres.put(genreDto.id, genreDto.name); // NOTE: Can't use a lambda here because of exception cascading
        }

        return inMemoryGenres;
    }
}
