package com.gaetanl.smwygapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gaetanl.smwygapi.dto.*;
import com.gaetanl.smwygapi.model.Genre;
import com.gaetanl.smwygapi.model.SimilarityProfile;
import com.gaetanl.smwygapi.model.Title;
import com.gaetanl.smwygapi.model.User;
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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

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

        final List<Title> pojoTitles = blockToTitleList(body);

        return index == null ? pojoTitles : titleIndexer.orderListByIndex(pojoTitles, index);
    }

    @Override
    public @NonNull List<Title> readAllByGenres(@Nullable final Title.TitleIndex index, @Nullable final Integer page, @NonNull final Set<Genre> genres) throws URISyntaxException, IOException {
        final String path = "/discover/movie";
        final String uriString = String.format("%s%s?api_key=%s%s%s",
                rootUri,
                path,
                apiKey,
                "&with_genres=" + String.join(",", genres.stream().map(genre -> String.valueOf(genre.getId())).collect(Collectors.toSet())),
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

        final List<Title> pojoTitles = blockToTitleList(body);

        return index == null ? pojoTitles : titleIndexer.orderListByIndex(pojoTitles, index);
    }

    @Override
    public @NonNull Genre readGenre(final int id) throws IOException, URISyntaxException {
        return getGenres().get(id);
    }

    @Override
    public @NonNull List<Title> readSimilar(@NonNull final String id, @Nullable final Title.TitleIndex index, @Nullable final Integer page) throws URISyntaxException, IOException {
        final String path = String.format("/movie/%s/similar", id);
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

        final List<Title> pojoTitles = blockToTitleList(body);

        return index == null ? pojoTitles : titleIndexer.orderListByIndex(pojoTitles, index);
    }

    @Override
    public @NonNull List<Title> search(
            @NonNull final SmwygSearchParametersDto sp,
            @Nullable final Title.TitleIndex index,
            @Nullable final Integer page) throws URISyntaxException, IOException {
        final String path = "/discover/movie";

        final List<String> queryParams = new ArrayList<>();
        queryParams.add(String.format("include_adult=%b", sp.include_adult));
        if (sp.primary_release_year != null) queryParams.add(String.format("primary_release_year=%d", sp.primary_release_year));
        if ((sp.with_genres != null) && (!sp.with_genres.isEmpty())) {
            final List<String> requestedGenreIds = new ArrayList<>();
            for (final String requestedGenreName: sp.with_genres) {
                for (final Genre genre: getGenres().values()) {
                    if (Objects.equals(genre.getName(), requestedGenreName)) {
                        requestedGenreIds.add(String.valueOf(genre.getId()));
                        break;
                    }
                }
            }
            queryParams.add(String.format("with_genres=%s",
                    String.join(",", String.join("%7C", requestedGenreIds))));
        }
        if ((sp.with_keywords != null) && (!sp.with_keywords.isEmpty())) queryParams.add(String.format("with_keywords=%s", String.join(",", sp.with_keywords)));
        if (sp.with_origin_country != null) queryParams.add(String.format("with_origin_country=%s", sp.with_origin_country));
        if (sp.with_original_language != null) queryParams.add(String.format("with_original_language=%s", sp.with_original_language));

        final String uriString = String.format("%s%s?api_key=%s&%s%s",
                rootUri,
                path,
                apiKey,
                String.join("&", queryParams),
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

        final List<Title> pojoTitles = blockToTitleList(body);

        return index == null ? pojoTitles : titleIndexer.orderListByIndex(pojoTitles, index);
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
        final Title title = new Title(Integer.toString(dtoTitle.id));
        title.setName(dtoTitle.title);
        try {title.setReleaseDate(LocalDate.parse(dtoTitle.release_date));} catch (final DateTimeParseException e) {title.setReleaseDate(null);}
        title.setLanguage(dtoTitle.originalLanguage);
        title.setAdult(dtoTitle.adult);
        title.setGenres(genres);
        title.setPictureUri(dtoTitle.posterPath);
        return title;
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

    /**
     * Returns the result of a Mono<String> WebClient.get() as a list of titles.
     *
     * @param  block               the Mono<String> block returned by the
     *                             WebClient.get()
     * @return                     the list of titles
     * @throws IOException         during Jackson deserialization
     * @throws URISyntaxException  during API call URI building
     */
    private @NonNull List<Title> blockToTitleList(final String block) throws IOException, URISyntaxException {
        final ObjectMapper objectMapper = ApiUtil.getObjectMapper();
        final JsonNode jsonResult = objectMapper.readTree(block).get("results");
        final ObjectReader objectReader = objectMapper.readerFor(new TypeReference<ArrayList<TmdbMovieReducedDto>>() {});

        final List<TmdbMovieReducedDto> dtoTitles = objectReader.readValue(jsonResult);

        final List<Title> pojoTitles = new ArrayList<>();
        for (final TmdbMovieReducedDto dtoTitle: dtoTitles) pojoTitles.add(dtoToModelTitle(dtoTitle)); // NOTE: Can't use a lambda here because of exception cascading

        return pojoTitles;
    }

    /**
     * Builds and returns an object listing parameters shared by the favorite
     * titles of a user. Used to find titles that may appeal to that user.
     *
     * @param  user  the user whose titles to explore
     * @return       the similarity profile of the user
     */
    private @NonNull SimilarityProfile getSimilarityProfile(@NonNull final User user) throws URISyntaxException, IOException, NoSuchElementException {
        final SimilarityProfile sp = new SimilarityProfile();

        final Set<String> favoriteIds = user.getFavorites();
        for (final String favoriteId: favoriteIds) {
            final Optional<Title> optionalTitle = read(favoriteId);
            if (optionalTitle.isEmpty()) throw new NoSuchElementException("No value present");
            final Title favorite = optionalTitle.get();

            final int releaseDecade = Math.round((float) favorite.getReleaseDate().getYear() / 10) * 10;
            sp.getReleaseDecade().increase(releaseDecade);

            sp.getLanguage().increase(favorite.getLanguage());
            sp.getAdult().increase(favorite.getAdult());

            for (final Genre genre: favorite.getGenres()) sp.getGenres().increase(genre.getName());
        }

        return sp;
    }
}
