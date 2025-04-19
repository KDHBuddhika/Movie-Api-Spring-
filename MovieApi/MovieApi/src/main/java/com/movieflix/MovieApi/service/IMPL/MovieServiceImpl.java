package com.movieflix.MovieApi.service.IMPL;

import com.movieflix.MovieApi.dto.MovieDto;
import com.movieflix.MovieApi.entities.Movie;
import com.movieflix.MovieApi.repo.MovieRepo;
import com.movieflix.MovieApi.service.FileService;
import com.movieflix.MovieApi.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class MovieServiceImpl implements MovieService {
    
    @Autowired
    private MovieRepo movieRepo;
    
    @Autowired
    private FileService fileService;
    
    @Value("${project.poster.path}")
    private String path;
    
    @Value("${base.url}")
    private String baseUrl;
    
    
    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        //1. upload file
        String uploadedFileName = fileService.uploadFile(path,file);
    
        System.out.println(uploadedFileName);
        System.out.println(movieDto);
        
        //2.set the value of field 'poster' as file name
        movieDto.setPoster(uploadedFileName);
        System.out.println(movieDto);
        
        //3.map dto to Movie object
        Movie movie = new Movie(
                movieDto.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );
        System.out.println("1");
        //4.save the movie object
        movieRepo.save(movie);
        System.out.println("2");
        
        //5. generate the posterUrl
        String posterUrl =baseUrl + "/file/" +uploadedFileName;
        
        //6.map movie object to dto and return it
        MovieDto response = new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
        return response;
    }
    
    
    
    @Override
    public MovieDto getMovie(Integer movieId) {
        //1 .cheack the data in db and if exist fetch the data of give id
        Movie movie = movieRepo.findById(movieId).orElseThrow(() -> new RuntimeException("Movie Not Found"));
        //2.generate poster url
        String posterUrl =baseUrl + "/file/" +movie.getPoster();
        
        //3.map to moviedto object
        MovieDto response = new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
        
        return response;
    }
    
    
    
    @Override
    public List<MovieDto> getAllMovies() {
   
return  null;
    }
}
