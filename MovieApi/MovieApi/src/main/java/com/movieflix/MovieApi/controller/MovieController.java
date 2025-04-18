package com.movieflix.MovieApi.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieflix.MovieApi.dto.MovieDto;
import com.movieflix.MovieApi.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {
    
    @Autowired
    private MovieService movieService;
    
    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovie(@RequestPart MultipartFile file,
                                             @RequestPart String movieDto) throws IOException {
        
        MovieDto dto = convertToMovieDto(movieDto);
        return new ResponseEntity<>(movieService.addMovie(dto,file) , HttpStatus.OK);
    }
    
    
    //json file convert to dto
    private MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        MovieDto movieDto ;
        ObjectMapper objectMapper = new ObjectMapper();
        movieDto =objectMapper.readValue(movieDtoObj,MovieDto.class);
        return movieDto;
    }
    
}
