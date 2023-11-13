package com.gaetanl.smwygapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gaetanl.smwygapi.dto.TmdbGenreDto;
import com.gaetanl.smwygapi.dto.TmdbMovieDetailsDto;
import com.gaetanl.smwygapi.dto.TmdbMovieDto;
import com.gaetanl.smwygapi.dto.TmdbMovieReducedDto;
import com.gaetanl.smwygapi.model.Genre;
import com.gaetanl.smwygapi.model.Title;
import com.gaetanl.smwygapi.util.ApiUtil;
import com.gaetanl.smwygapi.util.ModelObjectIndexer;
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

    private final Map<Integer, Genre> inMemoryGenres = new HashMap<>();
    private final ModelObjectIndexer<Title> titleIndexer = new ModelObjectIndexer<>();

    private final String apiKey = "91b96d8d3dc851fb01aa9a36e8c81880"; // TODO: externalize
    private final String rootUri = "https://api.themoviedb.org/3";

    @Override
    public @NonNull Optional<Title> read(@NonNull final String id) throws URISyntaxException, IOException {
        final String path = "/movie";
        final String uriString = String.format("%s%s/%s?api_key=%s",
                rootUri,
                path,
                id,
                apiKey);

        final WebClient client = WebClient.create();
        final URI uri = new URI(uriString);

        logger.info(uri.toString());
        final String body = client.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        final ObjectMapper objectMapper = ApiUtil.getObjectMapper();
        final JsonNode jsonResult = objectMapper.readTree(body);

        final TmdbMovieDetailsDto dtoTitle = objectMapper.treeToValue(jsonResult, TmdbMovieDetailsDto.class);

        return Optional.of(dtoToModelTitle(dtoTitle));
    }

    @Override
    public @NonNull List<Title> readAll(@Nullable final Title.TitleIndex index, @Nullable final Integer page) throws URISyntaxException, IOException {
        final String path = "/movie/popular";
        final String uriString = String.format("%s%s?api_key=%s%s",
                rootUri,
                path,
                apiKey,
                page == null ? "" : "&page=" + page);

        final WebClient client = WebClient.create();
        final URI uri = new URI(uriString);

        logger.info(uri.toString());
        final String body = client.get()
                    .uri(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        final ObjectMapper objectMapper = ApiUtil.getObjectMapper();
        final JsonNode jsonResult = objectMapper.readTree(body).get("results");
        final ObjectReader objectReader = objectMapper.readerFor(new TypeReference<ArrayList<TmdbMovieReducedDto>>() {});

        final List<TmdbMovieReducedDto> dtoTitles = objectReader.readValue(jsonResult);

        final List<Title> pojoTitles = new ArrayList<>();
        for (final TmdbMovieReducedDto dtoTitle: dtoTitles) pojoTitles.add(dtoToModelTitle(dtoTitle)); // NOTE: Can't use a lambda here because of exception cascading

        return index == null ? pojoTitles : titleIndexer.orderListByIndex(pojoTitles, index);
    }

    @Override
    public @NonNull List<Title> readAllByGenres(Title.TitleIndex index, Integer page, Set<String> genres) throws URISyntaxException, IOException {
        return null;
    }

    /**
     * Transforms a TMDB title into a proper Title with the information that
     * interest us.
     *
     * @param  dtoTitle           the TmdbMovieDto to transform
     * @return                    the newly built Title
     * @throws IOException        during Jackson deserialization
     * @throws URISyntaxException during API call URI building
     */
    private @NonNull Title dtoToModelTitle(@NonNull final TmdbMovieDto dtoTitle) throws IOException, URISyntaxException {
        final Set<Genre> genres = new HashSet<>();
        for (final Integer genreId: dtoTitle.getGenreIdsSet()) genres.add(getGenres().get(genreId)); // NOTE: Can't use a lambda here because of exception cascading
        return new Title(Integer.toString(dtoTitle.id), dtoTitle.title, genres, dtoTitle.posterPath);
    }

    /**
     * Returns the list of genres used by the TMDB API. Saves it locally and
     * uses it instead after the first call (we'll assume the genres won't
     * change during execution).
     *
     * @return                     a map of id, genres used by the TMDB API
     * @throws URISyntaxException  during API call URI building
     * @throws IOException         during Jackson deserialization
     */
    private @NonNull Map<Integer, Genre> getGenres() throws URISyntaxException, IOException {
        final String path = "/genre/movie/list";
        final String uriString = String.format("%s%s?api_key=%s",
                rootUri,
                path,
                apiKey);

        if (inMemoryGenres.isEmpty()) {
            final WebClient client = WebClient.create();
            final URI uri = new URI(uriString);

            logger.info(uri.toString());
            final String body = client.get()
                    .uri(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            final ObjectMapper objectMapper = ApiUtil.getObjectMapper();
            final JsonNode jsonGenresList = objectMapper.readTree(body).get("genres");
            final ObjectReader objectReader = objectMapper.readerFor(new TypeReference<ArrayList<TmdbGenreDto>>() {});

            final List<TmdbGenreDto> genreDtoList = objectReader.readValue(jsonGenresList);
            for (final TmdbGenreDto genreDto: genreDtoList) {
                inMemoryGenres.put(genreDto.id, new Genre(genreDto.id, genreDto.name)); // NOTE: Can't use a lambda here because of exception cascading
            }
        }

        return inMemoryGenres;
    }
}
