package com.example.API_Cinema.service.impl;


import com.example.API_Cinema.dto.MovieDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.model.Movie;
import com.example.API_Cinema.repository.MovieRepo;
import com.example.API_Cinema.response.CloudinaryResponse;
import com.example.API_Cinema.service.IMovieService;
import com.example.API_Cinema.utils.FileUploadUtils;
import com.example.API_Cinema.utils.MovieAliasUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService implements IMovieService {
    private final MovieRepo repository;
    private final CloudinaryService cloudinaryService;
    public MovieService(MovieRepo repository, CloudinaryService cloudinaryService) {
        this.repository = repository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public void insert(MovieDTO dto) {
        Movie movie = new ModelMapper().map(dto, Movie.class);
        String movieNameAlias = MovieAliasUtils.generateAlias(dto.getName());
        movie.setMovieUrl(movieNameAlias);
        repository.save(movie);
    }

    @Override
    @Transactional
    public MovieDTO update(MovieDTO dto) {
        Movie currentMovie = repository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Movie does not exits"));
        if(currentMovie != null){
            currentMovie.setName(dto.getName());
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
    @Transactional
    public void uploadSmallImage(final Integer id, final MultipartFile file) throws DataNotFoundException {
        final Movie movie = this.repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Movie not found"));
        FileUploadUtils.assertAllowed(file, FileUploadUtils.IMAGE_PATTERN);
        final String fileName = FileUploadUtils.getFileName(file.getOriginalFilename());
        final CloudinaryResponse response = this.cloudinaryService.uploadFile(file, fileName);
        movie.setSmallImgMovie(response.getUrl());
        movie.setCloudinaryImageSmallId(response.getPublicId());
        this.repository.save(movie);
    }
    @Transactional
    public void uploadLargeImage(final Integer id, final MultipartFile file) throws DataNotFoundException {
        final Movie movie = this.repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));
        FileUploadUtils.assertAllowed(file, FileUploadUtils.IMAGE_PATTERN);
        final String fileName = FileUploadUtils.getFileName(file.getOriginalFilename());
        final CloudinaryResponse response = this.cloudinaryService.uploadFile(file, fileName);
        movie.setLargeImgMovie(response.getUrl());
        movie.setCloudinaryImgLargeId(response.getPublicId());
        this.repository.save(movie);
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

    @Override
    public MovieDTO getMovieByMovieUrl(String movieUrl) throws DataNotFoundException {
        if(repository.findByMovieUrl(movieUrl) == null){
            throw new DataNotFoundException("Movie url: " + movieUrl + " not found");
        }
        Movie movie = repository.findByMovieUrl(movieUrl);
        return convert(movie);
    }
}
