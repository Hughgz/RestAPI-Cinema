package com.example.API_Cinema.service.impl;


import com.example.API_Cinema.dto.MovieDTO;
import com.example.API_Cinema.model.Movie;
import com.example.API_Cinema.repo.MovieRepo;
import com.example.API_Cinema.service.IMovieService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService implements IMovieService {
    @Autowired
    MovieRepo repository;


    @Override
    public void insert(MovieDTO dto) {
        Movie movie = new ModelMapper().map(dto, Movie.class);
        repository.save(movie);
    }

    @Override
    @Transactional
    public MovieDTO update(MovieDTO dto) {
        Movie currentMovie = repository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Movie does not exits"));
        if(currentMovie != null){
            currentMovie.setName(dto.getName());
            currentMovie.setSmallImgMovie(dto.getSmallImgMovie());
            currentMovie.setLargeImgMovie(dto.getLargeImgMovie());
            currentMovie.setShortDescription(dto.getShortDescription());
            currentMovie.setLongDescription(dto.getLongDescription());
            currentMovie.setDirector(dto.getDirector());
            currentMovie.setActors(dto.getActors());
            currentMovie.setCategories(dto.getCategories());
            currentMovie.setReleaseDate(dto.getReleaseDate());
            currentMovie.setDuration(dto.getDuration());
            currentMovie.setTrailerURL(dto.getTrailerURL());
            currentMovie.setCountry(dto.getCountry());
            currentMovie.setRated(dto.getRated());
            currentMovie.setShowing(dto.getShowing());
            repository.save(currentMovie);
            return convert(currentMovie);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }

    @Override
    public List<MovieDTO> getAll() {
        List<Movie> movies = repository.findAll();
        return movies.stream().map(movie -> convert(movie)).collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> findByName(String name) {
        List<Movie> movies = repository.findByName(name);
        return movies.stream().map(movie -> convert(movie)).collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> findByDirector(String director) {
        List<Movie> movies = repository.findByDirector(director);
        return movies.stream().map(movie -> convert(movie)).collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> findByActor(String actor) {
        List<Movie> movies = repository.findByActor(actor);
        return movies.stream().map(movie -> convert(movie)).collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> findByCategory(String category) {
        List<Movie> movies = repository.findByCategory(category);
        return movies.stream().map(movie -> convert(movie)).collect(Collectors.toList());
    }

    @Override
    public MovieDTO convert(Movie movie) {
        return new ModelMapper().map(movie, MovieDTO.class);
    }

    @Override
    public MovieDTO findById(int id) {
        Movie movies = repository.findById(id).orElseThrow(() -> new RuntimeException("Movie does not exits"));
        return convert(movies);
    }
}
