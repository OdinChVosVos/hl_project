package ru.sirius.hl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sirius.hl.dto.MovieDto;
import ru.sirius.hl.service.MovieService;
import ru.sirius.hl.service.ObservabilityService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
@RequiredArgsConstructor
@Tag(name = "Movie Controller")
public class MovieController {

    private final MovieService movieService;
    private final ObservabilityService observabilityService;

    @DeleteMapping("/clear")
    @Operation(summary = "Очистка всех фильмов и связанных билетов")
    public ResponseEntity<String> clearAll() {
        long startTime = observabilityService.startTiming();
        try {
            movieService.clearAll();
            return ResponseEntity.ok("All movies and related tickets cleared");
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }

    @GetMapping
    @Operation(summary = "Получение всех фильмов без пагинации")
    public ResponseEntity<List<MovieDto>> getMovies() {
        long startTime = observabilityService.startTiming();
        try {
            List<MovieDto> movies = movieService.getAllMovies();
            return ResponseEntity.ok(movies);
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение фильма по ID")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        long startTime = observabilityService.startTiming();
        try {
            MovieDto movie = movieService.getMovieById(id);
            return ResponseEntity.ok(movie);
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Физическое удаление фильма")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        long startTime = observabilityService.startTiming();
        try {
            movieService.deleteMovie(id);
            return ResponseEntity.noContent().build();
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }

    @PostMapping
    @Operation(summary = "Создание нового фильма")
    public ResponseEntity<MovieDto> saveMovie(@RequestBody MovieDto movie) {
        long startTime = observabilityService.startTiming();
        try {
            MovieDto savedMovie = movieService.saveMovie(movie);
            return ResponseEntity.ok(savedMovie);
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновление существующего фильма")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long id, @RequestBody MovieDto movie) {
        long startTime = observabilityService.startTiming();
        try {
            MovieDto updatedMovie = movieService.updateMovie(id, movie);
            return ResponseEntity.ok(updatedMovie);
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }
}