package ru.hpclab.hl.module1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.dto.MovieDto;
import ru.hpclab.hl.module1.service.MovieService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
@RequiredArgsConstructor
@Tag(name = "Movie Controller")
public class MovieController {

    private final MovieService movieService;


    @PostMapping("/clear")
    public ResponseEntity<String> clearAll() {
        movieService.clearAll();
        return ResponseEntity.ok("All movies and related tickets cleared");
    }

    @GetMapping
    @Operation(summary = "Получение кино `без пагинации")
    public List<MovieDto> getMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение кино")
    public MovieDto getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление кино физическое")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }

    @PostMapping
    @Operation(summary = "Создание кино")
    public MovieDto saveMovie(@RequestBody MovieDto movie) {
        return movieService.saveMovie(movie);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменение кино")
    public MovieDto updateMovie(
            @PathVariable Long id,
            @RequestBody MovieDto movie) {
        return movieService.updateMovie(id, movie);
    }

}
