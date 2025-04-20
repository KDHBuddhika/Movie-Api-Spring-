package com.movieflix.MovieApi.service.IMPL;

import com.movieflix.MovieApi.dto.MovieDto;
import com.movieflix.MovieApi.dto.MoviePageResponse;
import com.movieflix.MovieApi.entities.Movie;
import com.movieflix.MovieApi.exceptions.FileExistsException;
import com.movieflix.MovieApi.exceptions.MovieNotFoundException;
import com.movieflix.MovieApi.repo.MovieRepo;
import com.movieflix.MovieApi.service.FileService;
import com.movieflix.MovieApi.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        if( Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))){
            throw  new FileExistsException("File Already exist. please enter another file name ");
        }
        String uploadedFileName = fileService.uploadFile(path,file);
    
        System.out.println(uploadedFileName);
        System.out.println(movieDto);
        
        //2.set the value of field 'poster' as file name
        movieDto.setPoster(uploadedFileName);
        System.out.println(movieDto);
        
        //3.map dto to Movie object
        Movie movie = new Movie(
               null,
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );
        System.out.println("1");
        //4.save the movie object
       Movie savedMovie = movieRepo.save(movie);
        System.out.println("2");
        
        //5. generate the posterUrl
        String posterUrl =baseUrl + "/file/" +uploadedFileName;
        
        //6.map movie object to dto and return it
        MovieDto response = new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );
        return response;
    }
    
    
    
    @Override
    public MovieDto getMovie(Integer movieId) {
        //1 .cheack the data in db and if exist fetch the data of give id
        Movie movie = movieRepo.findById(movieId).orElseThrow(() -> new MovieNotFoundException("Movie Not Found"));
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
    
        //1.fetch all movies
        List<Movie> movies = movieRepo.findAll();
    
        List<MovieDto> movieDtos = new ArrayList<>();
        //2.iterate through the list , generate posterUrl for each movie obj
        for (Movie movie : movies){
            String posterUrl =baseUrl + "/file/" +movie.getPoster();
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
            movieDtos.add(response);
        }
         
        
       
         return  movieDtos;
    }
    
    //update movie
    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
        // 1. cheack if movie object exist with given movieid
        Movie movie = movieRepo.findById(movieId).orElseThrow(() -> new MovieNotFoundException("Movie Not Found"));
        
        //2.if file is null , do nothing
        //if file is not null , them delete existing file and upload now file
        String fileName = movie.getPoster();
        if(file != null){
            Files.deleteIfExists(Paths.get(path + File.separator + fileName));
            fileName = fileService.uploadFile(path,file);
        }
        
        
        //3.set movie dto's poster value , according to step2
        movieDto.setPoster(fileName);
        
        //4. map it to movie to movie object
        Movie movie1 = new Movie(
                movie.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );
        
        //5.save the movie object -> return saved movie object
        Movie updatedMovie = movieRepo.save(movie1);
        
        //6.generate poster url for it
        String posterUrl =baseUrl + "/file/" + fileName;
        
        //7. map to movie dto and return it
        MovieDto response = new MovieDto(
                updatedMovie.getMovieId(),
                updatedMovie.getTitle(),
                updatedMovie.getDirector(),
                updatedMovie.getStudio(),
                updatedMovie.getMovieCast(),
                updatedMovie.getReleaseYear(),
                updatedMovie.getPoster(),
                posterUrl
        );
        
        return response;
    }
    
    
    //delete movie
    @Override
    public String deleteMovie(Integer movieId) throws IOException {
        
        //1. check iif movie object exist in DB
        Movie movie = movieRepo.findById(movieId).orElseThrow(() -> new MovieNotFoundException("Movie Not Found"));
        
        //2.delete the file associated with this object
        Files.deleteIfExists(Paths.get(path + File.separator + movie.getMovieId()));
        
        //3. delete movie object
        movieRepo.delete(movie);
        return "Movie is deleted with ID="+movie.getMovieId();
    }
    
    
    
    
    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        
        Page<Movie> moviePage = movieRepo.findAll(pageable);
        List<Movie> movies = moviePage.getContent();
        
        List<MovieDto> movieDtos = new ArrayList<>();
        
        for (Movie movie : movies){
            String posterUrl =baseUrl + "/file/" +movie.getPoster();
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
            movieDtos.add(response);
        }
        
        
        
        return new MoviePageResponse(movieDtos,pageNumber,pageSize,
                                     moviePage.getTotalElements(),
                                     moviePage.getTotalPages(),
                                     moviePage.isLast());
    }
    
    @Override
    public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
    
        Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() :
                                                                Sort.by(sortBy).descending();
    
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Movie> moviePages = movieRepo.findAll(pageable);
        List<Movie> movies = moviePages.getContent();
    
        List<MovieDto> movieDtos = new ArrayList<>();
        
        for (Movie movie : movies){
            String posterUrl =baseUrl + "/file/" +movie.getPoster();
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
            movieDtos.add(response);
        }
    
    
    
        return new MoviePageResponse(movieDtos,pageNumber,pageSize,
                moviePages.getTotalElements(),
                moviePages.getTotalPages(),
                moviePages.isLast());
    
    }
}
