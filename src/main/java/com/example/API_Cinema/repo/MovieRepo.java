package com.example.API_Cinema.repo;

import com.example.API_Cinema.model.Branch;
import com.example.API_Cinema.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Integer> {
    @Query("SELECT movie FROM Movie movie WHERE movie.name LIKE %:name%")
    List<Movie> findByName(@Param("name") String name);

    @Query("SELECT movie FROM Movie movie WHERE movie.director LIKE %:director%")
    List<Movie> findByDirector(@Param("director") String director);

    @Query("SELECT movie FROM Movie movie WHERE movie.actors LIKE %:actors%")
    List<Movie> findByActor(@Param("actors") String actor);

    @Query("SELECT movie FROM Movie movie WHERE movie.categories LIKE %:categories%")
    List<Movie> findByCategory(@Param("categories") String categories);
}
