package com.gaetanl.smwygapi.model;

import lombok.Getter;

/**
 * Class containing parameters shared by the favorite titles of a user. Used to
 * find titles that may appeal to that user.
 */
@Getter
public class SimilarityProfile {
    final private SimilarityParameter<Integer> releaseDecade = new SimilarityParameter<>();
    final private SimilarityParameter<String> language = new SimilarityParameter<>();
    final private SimilarityParameter<Boolean> adult = new SimilarityParameter<>();
    final private SimilarityParameter<String> genres = new SimilarityParameter<>();
}
