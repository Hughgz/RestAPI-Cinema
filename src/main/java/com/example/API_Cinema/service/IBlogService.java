package com.example.API_Cinema.service;

import com.example.API_Cinema.dto.BlogDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.model.Blog;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IBlogService {
    void insertBlog(BlogDTO dto);
    BlogDTO updateBlog(BlogDTO dto);
    List<BlogDTO> getAllBlog();
    void uploadImage(final Integer id, final MultipartFile file) throws DataNotFoundException;
    BlogDTO convert(Blog blog);

}
