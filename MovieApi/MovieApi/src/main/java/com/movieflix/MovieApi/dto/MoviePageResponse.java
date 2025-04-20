package com.movieflix.MovieApi.dto;

import java.util.List;

public record MoviePageResponse (List<MovieDto> movieDtos,
                                 Integer pageNumber,
                                 Integer pageSize,
                                 long totalElement,
                                 int totalPages,
                                 boolean isLast){
}
