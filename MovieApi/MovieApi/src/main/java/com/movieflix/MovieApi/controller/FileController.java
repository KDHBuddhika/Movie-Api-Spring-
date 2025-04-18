package com.movieflix.MovieApi.controller;


import com.movieflix.MovieApi.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file/")
public class FileController {
    
    @Autowired
    private  FileService fileService;
    
    @Value("${project.poster.path}")
    private String path;
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestPart MultipartFile file) throws IOException{
       String uploadedFileName =  fileService.uploadFile(path, file);
       return ResponseEntity.ok("File uploaded :" + uploadedFileName);
    }
    
    @GetMapping("/{fileName}")
    public void serveFileHandler(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        InputStream resourcesFile = fileService.getResourcesFile(path , fileName);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(resourcesFile,response.getOutputStream());
    }
    
    
}
