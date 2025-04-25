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


    @Transactional
    public void clearAll() {
        ticketService.clearAll();  // Clear dependent tickets first
        movieRepository.deleteAll();
    }



    // Get all movies (no pagination)
    public List<MovieDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();

        return movies.stream()
                .map(movie -> modelMapper.map(movie, MovieDto.class))
                .collect(Collectors.toList());
    }



    // Get movie by ID
    public MovieDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Movie with ID " + id + " not found"));

        return modelMapper.map(movie, MovieDto.class);
    }



    // Delete movie by ID (physical deletion)
    public void deleteMovie(Long id) {
        try {
            movieRepository.deleteById(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID format: " + id);
        } catch (Exception e) {
            throw new NoSuchElementException("Movie with ID " + id + " not found");
        }
    }



    // Save a new movie
    public MovieDto saveMovie(MovieDto movie) {
        try {
            Movie savedMovie = movieRepository.save(
                    modelMapper.map(movie, Movie.class));

            return modelMapper.map(savedMovie, MovieDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to save movie due to invalid or duplicate data.", e);
        }
    }


    // Update an existing movie
    public MovieDto updateMovie(Long id, MovieDto updatedMovie) {
        try {
            Movie existingMovie = movieRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Movie with ID " + id + " not found"));

            // Update fields
            BeanUtils.copyProperties(updatedMovie, existingMovie, getNullPropertyNames(updatedMovie));

            Movie savedmovie = movieRepository.save(existingMovie);
            return modelMapper.map(savedmovie, MovieDto.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to update key with ID " + id, e);
        }
    }

}
