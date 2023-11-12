package com.gaetanl.smwygapi.util;

import com.gaetanl.smwygapi.model.ModelIndex;
import com.gaetanl.smwygapi.model.ModelObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ModelObjectIndexer<T extends ModelObject> {
    private static final Logger logger = LoggerFactory.getLogger(ModelObjectIndexer.class);

    public @NonNull List<T> orderListByIndex(final List<T> unorderedList, final ModelIndex<T> index) {
        logger.info("Unordered list: " + unorderedList.stream().map(index::getIndexedValue).toList());

        final SortedMap<String, T> sortedMap = unorderedList.stream()
                .collect(Collectors.toMap(index::getIndexedValue, Function.identity(), (o, n) -> n, TreeMap::new));

        final List<T> orderedList = new ArrayList<>(sortedMap.values());
        logger.info("Ordered list: " + orderedList.stream().map(index::getIndexedValue).toList());
        return orderedList;
    }
}
