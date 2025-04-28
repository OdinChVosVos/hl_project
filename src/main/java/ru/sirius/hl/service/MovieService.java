package ru.sirius.hl.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.sirius.hl.dto.MovieDto;
import ru.sirius.hl.model.Movie;
import ru.sirius.hl.repository.MovieRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static ru.sirius.hl.service.BeanUtilsHelper.getNullPropertyNames;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MovieService {

    private final MovieRepository movieRepository;
    private final ModelMapper modelMapper;
    private final TicketService ticketService;
    private final ObservabilityService observabilityService;

    @Transactional
    public void clearAll() {
        long startTime = observabilityService.startTiming();
        try {
            ticketService.clearAll(); // Clear dependent tickets first
            movieRepository.deleteAll();
        } finally {
            observabilityService.stopTiming(startTime, "database");
        }
    }

    public List<MovieDto> getAllMovies() {
        long startTime = observabilityService.startTiming();
        try {
            List<Movie> movies = movieRepository.findAll();
            return movies.stream()
                    .map(movie -> modelMapper.map(movie, MovieDto.class))
                    .collect(Collectors.toList());
        } finally {
            observabilityService.stopTiming(startTime, "database");
        }
    }

    public MovieDto getMovieById(Long id) {
        long startTime = observabilityService.startTiming();
        try {
            Movie movie = movieRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Movie with ID " + id + " not found"));
            return modelMapper.map(movie, MovieDto.class);
        } finally {
            observabilityService.stopTiming(startTime, "database");
        }
    }

    public void deleteMovie(Long id) {
        long startTime = observabilityService.startTiming();
        try {
            movieRepository.deleteById(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID format: " + id);
        } catch (Exception e) {
            throw new NoSuchElementException("Movie with ID " + id + " not found");
        } finally {
            observabilityService.stopTiming(startTime, "database");
        }
    }

    public MovieDto saveMovie(MovieDto movie) {
        long startTime = observabilityService.startTiming();
        try {
            Movie savedMovie = movieRepository.save(
                    modelMapper.map(movie, Movie.class));
            return modelMapper.map(savedMovie, MovieDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to save movie due to invalid or duplicate data.", e);
        } finally {
            observabilityService.stopTiming(startTime, "database");
        }
    }

    public MovieDto updateMovie(Long id, MovieDto updatedMovie) {
        long startTime = observabilityService.startTiming();
        try {
            Movie existingMovie = movieRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Movie with ID " + id + " not found"));

            BeanUtils.copyProperties(updatedMovie, existingMovie, getNullPropertyNames(updatedMovie));

            Movie savedMovie = movieRepository.save(existingMovie);
            return modelMapper.map(savedMovie, MovieDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update movie with ID " + id, e);
        } finally {
            observabilityService.stopTiming(startTime, "database");
        }
    }
}