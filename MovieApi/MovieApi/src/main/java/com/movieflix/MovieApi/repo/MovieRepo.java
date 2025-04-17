package com.movieflix.MovieApi.repo;

import com.movieflix.MovieApi.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepo extends JpaRepository<Movie,Integer> {
}
