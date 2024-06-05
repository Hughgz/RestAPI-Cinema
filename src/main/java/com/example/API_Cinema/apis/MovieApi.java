package com.example.API_Cinema.apis;

import com.example.API_Cinema.dto.BranchDTO;
import com.example.API_Cinema.dto.MovieDTO;
import com.example.API_Cinema.model.Movie;
import com.example.API_Cinema.service.impl.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movie")
public class MovieApi {
    @Autowired
    MovieService service;

    @PostMapping("/new")
    public ResponseEntity<?> createMovie(@Valid @RequestBody MovieDTO dto, BindingResult result){
        try{
            if(result.hasErrors()){
                List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorMessage);
            }
            service.insert(dto);
            return ResponseEntity.status(201).body("Create Movie Successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateMovie(@Valid @RequestBody MovieDTO dto, BindingResult result){
        try{
            if(result.hasErrors()){
                List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorMessage);
            }
            MovieDTO movieDTO = service.update(dto);
            return ResponseEntity.status(200).body(movieDTO);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMovie(@RequestParam("movieID") int movieID){
        try {
            service.delete(movieID);
            return ResponseEntity.ok(String.format("Product with id = %d deleted successfully", movieID));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("")
    public ResponseEntity<List<MovieDTO>> getAll(){
        List<MovieDTO> movieDTOS = service.getAll();
        return ResponseEntity.status(200).body(movieDTOS);
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchMovie(@RequestParam("keyword") String keyword, @RequestParam("title") String title) {
        try {
            if (keyword.equals("name")) {
                List<MovieDTO> movieDTOS = service.findByName(title);
                if (movieDTOS.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No movies found with the specified name");
                }
                return ResponseEntity.ok(movieDTOS);
            }
            if (keyword.equals("director")) {
                List<MovieDTO> movieDTOS = service.findByDirector(title);
                if (movieDTOS.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No movies found with the specified director");
                }
                return ResponseEntity.ok(movieDTOS);
            }
            if (keyword.equals("actors")) {
                List<MovieDTO> movieDTOS = service.findByActor(title);
                if (movieDTOS.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No movies found with the specified actor");
                }
                return ResponseEntity.ok(movieDTOS);
            }
            if (keyword.equals("categories")) {
                List<MovieDTO> movieDTOS = service.findByCategory(title);
                if (movieDTOS.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No movies found with the specified category");
                }
                return ResponseEntity.ok(movieDTOS);
            }
            if (keyword.equals("movieID")) {
                try {
                    MovieDTO movieDTO = service.findById(Integer.parseInt(title));
                    return ResponseEntity.ok(movieDTO);
                } catch (RuntimeException e) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found with the specified ID");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Please enter the correct search value");
    }
}
