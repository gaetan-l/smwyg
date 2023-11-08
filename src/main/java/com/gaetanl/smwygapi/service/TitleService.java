package com.gaetanl.smwygapi.service;

import com.gaetanl.smwygapi.model.Title;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface TitleService {
    @NonNull String getApiKey();
    @NonNull String getRootUri();
    @NonNull List<Title> getTitles(@Nullable Integer page) throws URISyntaxException, IOException;
}
