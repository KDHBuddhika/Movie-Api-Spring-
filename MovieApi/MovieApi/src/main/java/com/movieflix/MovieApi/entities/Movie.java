package com.movieflix.MovieApi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Movie {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;
    
    @Column(nullable = false,length = 200)
    @NotBlank(message = "Please provide movie's title")
    private String title;
    
//    @Version
//    private Integer version;
    
    
    @Column(nullable = false)
    @NotBlank(message = "Please provide movie's director")
    private String director;
    
    @Column(nullable = false)
    @NotBlank(message = "Please provide movie's studio")
    private String studio;
    
    @ElementCollection
    @CollectionTable(name = "movie_cast")
    private Set<String> movieCast;
    
//    @Column(nullable = false)
//    @NotBlank
    private Integer releaseYear;
    
    @Column(nullable = false)
    @NotBlank(message = "Please provide movie's poster")
    private String poster;
//
//    public Movie(Integer movieId, String title, String director, String studio, Set<String> movieCast, Integer releaseYear, String poster) {
//        this.movieId = movieId;
//        this.title = title;
//        this.director = director;
//        this.studio = studio;
//        this.movieCast = movieCast;
//        this.releaseYear = releaseYear;
//        this.poster = poster;
//    }
}
