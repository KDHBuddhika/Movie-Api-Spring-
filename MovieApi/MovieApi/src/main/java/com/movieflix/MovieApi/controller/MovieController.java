package com.movieflix.MovieApi.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieflix.MovieApi.dto.MovieDto;
import com.movieflix.MovieApi.dto.MoviePageResponse;
import com.movieflix.MovieApi.exceptions.EmptyFileException;
import com.movieflix.MovieApi.service.MovieService;
import com.movieflix.MovieApi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {
    
    @Autowired
    private MovieService movieService;
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovie(@RequestPart MultipartFile file,
                                             @RequestPart String movieDto) throws IOException, EmptyFileException {
        
        if(file.isEmpty()){
            throw new EmptyFileException("File is empty !Please send another files");
        }
        MovieDto dto = convertToMovieDto(movieDto);
        return new ResponseEntity<>(movieService.addMovie(dto,file) , HttpStatus.OK);
    }
    
    //get movie by id
    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Integer id){
        System.out.println("1");
        return ResponseEntity.ok(movieService.getMovie(id));
    }
    
    //get all movies bu id
    @GetMapping("/all")
    public ResponseEntity<List<MovieDto>> getMovies(){
        return ResponseEntity.ok(movieService.getAllMovies());
    }
    
    
    //update movie
    @PutMapping("/update/{movieId}")
    public  ResponseEntity<MovieDto> updateMovie(@PathVariable Integer movieId,
                                                 @RequestPart MultipartFile file,
                                                 @RequestPart String movieDtoObj) throws IOException {
        if(file.isEmpty()) file= null;
        MovieDto movieDto = convertToMovieDto(movieDtoObj);
        return ResponseEntity.ok(movieService.updateMovie(movieId,movieDto,file));
        
    }
    
    //delete movie
    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deleteMovie(@PathVariable Integer movieId) throws IOException {
        return ResponseEntity.ok(movieService.deleteMovie(movieId));
    }
    
    
    //get All movies page
    @GetMapping("/allMoviePage")
    public ResponseEntity<MoviePageResponse>  getAllMoviesWithPagination(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER ,required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE ,required = false) Integer pageSize
    ){
        return ResponseEntity.ok(movieService.getAllMoviesWithPagination(pageNumber,pageSize));
    }
    
    //get All movies page
    @GetMapping("/allMoviePageSort")
    public ResponseEntity<MoviePageResponse>  getAllMoviesWithPaginationAndSorting(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER ,required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE ,required = false) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY ,required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR ,required = false) String dir
    ){
        return ResponseEntity.ok(movieService.getAllMoviesWithPaginationAndSorting(pageNumber,pageSize,sortBy,dir));
    }
    
    
    
    //json file convert to dto
    private MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        MovieDto movieDto ;
        ObjectMapper objectMapper = new ObjectMapper();
        movieDto =objectMapper.readValue(movieDtoObj,MovieDto.class);
        return movieDto;
    }
    
}
