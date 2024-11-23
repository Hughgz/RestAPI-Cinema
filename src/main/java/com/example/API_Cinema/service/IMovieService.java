package com.example.API_Cinema.service;

import com.example.API_Cinema.dto.MovieDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.model.Movie;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IMovieService {
    void insert(MovieDTO dto);
    MovieDTO update(MovieDTO dto);
    void delete(int id);
    List<MovieDTO> getAll();
    List<MovieDTO> findByName(String name);
    List<MovieDTO> findByDirector(String director);
    List<MovieDTO> findByActor(String actor);
    List<MovieDTO> findByCategory(String category);
    void uploadSmallImage(final Integer id, final MultipartFile file) throws DataNotFoundException;
    void uploadLargeImage(final Integer id, final MultipartFile file) throws DataNotFoundException;
    MovieDTO convert(Movie movie);
    MovieDTO findById(int id);

}
