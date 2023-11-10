package com.gaetanl.smwygapi.service;

import com.gaetanl.smwygapi.model.Title;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface TitleService {
    /**
     * Returns a list of titles.
     *
     * @param  page                the page of result to return, defaults to 1
     *                             if empty
     * @return                     a list of titles
     * @throws IOException         during Jackson deserialization
     * @throws URISyntaxException  during API call URI building
     */
    @NonNull List<Title> readAll(@Nullable Integer page) throws URISyntaxException, IOException;
}
