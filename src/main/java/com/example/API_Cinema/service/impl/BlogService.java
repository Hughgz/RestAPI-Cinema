package com.example.API_Cinema.service.impl;

import com.example.API_Cinema.dto.BlogDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.model.Blog;
import com.example.API_Cinema.repository.BlogRepo;
import com.example.API_Cinema.response.CloudinaryResponse;
import com.example.API_Cinema.service.IBlogService;
import com.example.API_Cinema.utils.FileUploadUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class BlogService implements IBlogService {
    private final BlogRepo blogRepo;
    private final CloudinaryService cloudinaryService;

    public BlogService(BlogRepo blogRepo, CloudinaryService cloudinaryService) {
        this.blogRepo = blogRepo;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public void insertBlog(BlogDTO dto) {
        Blog blog = new ModelMapper().map(dto, Blog.class);
        blogRepo.save(blog);
    }

    @Override
    public BlogDTO updateBlog(BlogDTO dto) {
        return null;
    }

    @Override
    public List<BlogDTO> getAllBlog() {
        List<Blog> blogs = blogRepo.findAll();
        return blogs.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public void uploadImage(Integer id, MultipartFile file) throws DataNotFoundException {
        final Blog blog = this.blogRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Blog not found"));
        FileUploadUtils.assertAllowed(file, FileUploadUtils.IMAGE_PATTERN);
        final String fileName = FileUploadUtils.getFileName(file.getOriginalFilename());
        final CloudinaryResponse response = this.cloudinaryService.uploadFile(file, fileName);
        blog.setImage(response.getUrl());
        this.blogRepo.save(blog);
    }

    @Override
    public BlogDTO convert(Blog blog) {
        return new ModelMapper().map(blog, BlogDTO.class);
    }
}
