package com.example.API_Cinema.apis;

import com.example.API_Cinema.dto.MovieDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.service.impl.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movie")
public class MovieApi {

    private final MovieService service;

    public MovieApi(MovieService service) {
        this.service = service;
    }

    // Tạo mới movie
    @PostMapping("/new")
    public ResponseEntity<?> createMovie(@Valid @RequestBody MovieDTO dto, BindingResult result) {
        return handleValidationAndExecute(result, () -> {
            service.insert(dto);
            return ResponseEntity.status(201).body("Movie created successfully");
        });
    }
    @PostMapping("/upload/smallImage/{id}")
    public ResponseEntity<?> uploadSmallImage(@PathVariable final Integer id, @RequestPart final MultipartFile file) throws DataNotFoundException {
        service.uploadSmallImage(id, file);
        return ResponseEntity.ok("Upload successfully");
    }
    @PostMapping("/upload/largeImage/{id}")
    public ResponseEntity<?> uploadLargeImage(@PathVariable final Integer id, @RequestPart final MultipartFile file) throws DataNotFoundException {
        service.uploadLargeImage(id, file);
        return ResponseEntity.ok("Upload successfully");
    }
    // Cập nhật movie
    @PutMapping("/update")
    public ResponseEntity<?> updateMovie(@Valid @RequestBody MovieDTO dto, BindingResult result) {
        return handleValidationAndExecute(result, () -> {
            MovieDTO updatedMovie = service.update(dto);
            return ResponseEntity.ok(updatedMovie);
        });
    }

    // Xóa movie
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMovie(@RequestParam("movieID") int movieID) {
        try {
            service.delete(movieID);
            return ResponseEntity.ok(String.format("Movie with ID = %d deleted successfully", movieID));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/getMovieByMovieUrl/{movieUrl}")
    public ResponseEntity<MovieDTO> getMovieByMovieUrl(@PathVariable String movieUrl) throws DataNotFoundException {
        MovieDTO movie = service.getMovieByMovieUrl(movieUrl);
        return ResponseEntity.ok(movie);
    }

    @GetMapping()
    public ResponseEntity<List<MovieDTO>> getAll() {
        List<MovieDTO> movieDTOS = service.getAll();
        return ResponseEntity.ok(movieDTOS);
    }

    // Tìm kiếm movie
    @GetMapping("/search")
    public ResponseEntity<?> searchMovie(@RequestParam("keyword") String keyword, @RequestParam("title") String title) {
        Map<String, Function<String, List<MovieDTO>>> searchStrategies = Map.of(
                "name", service::findByName,
                "director", service::findByDirector,
                "actors", service::findByActor,
                "categories", service::findByCategory
        );

        if (searchStrategies.containsKey(keyword)) {
            List<MovieDTO> result = searchStrategies.get(keyword).apply(title);
            if (result.isEmpty()) {
                return ResponseEntity.status(404).body("No movies found for the given criteria");
            }
            return ResponseEntity.ok(result);
        }

        if (keyword.equals("movieID")) {
            try {
                MovieDTO movie = service.findById(Integer.parseInt(title));
                return ResponseEntity.ok(movie);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid ID format");
            } catch (RuntimeException e) {
                return ResponseEntity.status(404).body("Movie not found with the specified ID");
            }
        }

        return ResponseEntity.badRequest().body("Invalid keyword for search");
    }
    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getByMovieId(@PathVariable int id) {
        MovieDTO movie = service.findById(id);
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movie);
    }


    // Utility function for handling validation and execution
    private ResponseEntity<?> handleValidationAndExecute(BindingResult result, ExecutableAction action) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        return action.execute();
    }

    // Functional interface for executing actions
    @FunctionalInterface
    private interface ExecutableAction {
        ResponseEntity<?> execute();
    }
}
