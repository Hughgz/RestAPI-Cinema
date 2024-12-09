package com.example.API_Cinema.apis;

import com.example.API_Cinema.dto.BlogDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.service.impl.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/blog")
public class BlogApi {
    private final BlogService service;

    public BlogApi(BlogService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> insertBlog(@RequestBody BlogDTO dto){
        service.insertBlog(dto);
        return ResponseEntity.status(201).body("Created blog successfully");
    }

    @PostMapping("/uploadImage/{id}")
    public ResponseEntity<?> uploadImage(@PathVariable int id, @RequestPart MultipartFile file) throws DataNotFoundException {
        service.uploadImage(id, file);
        return ResponseEntity.ok("Upload successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllBlog() throws DataNotFoundException {
        List<BlogDTO> blogs = service.getAllBlog();
        if(blogs.isEmpty()){
            throw new DataNotFoundException("Blog is empty");
        }
        return ResponseEntity.ok(blogs);
    }
}
