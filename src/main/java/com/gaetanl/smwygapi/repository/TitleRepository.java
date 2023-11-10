package com.gaetanl.smwygapi.repository;

import com.gaetanl.smwygapi.model.Title;
import org.springframework.data.repository.CrudRepository;

@SuppressWarnings("unused")
public interface TitleRepository extends CrudRepository<Title, Integer> {
}
