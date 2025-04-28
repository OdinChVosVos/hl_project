package ru.sirius.hl.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import ru.sirius.hl.dto.TicketDto;
import ru.sirius.hl.model.Movie;
import ru.sirius.hl.model.Ticket;
import ru.sirius.hl.repository.MovieRepository;
import ru.sirius.hl.repository.TicketRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static ru.sirius.hl.service.BeanUtilsHelper.getNullPropertyNames;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TicketService {

    private final MovieRepository movieRepository;
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final ObservabilityService observabilityService;


    @Transactional
    public void clearAll() {
        long startTime = observabilityService.startTiming();
        try {
            ticketRepository.deleteAll();
        } finally {
            observabilityService.stopTiming(startTime, "database");
        }
    }

    public List<TicketDto> getAllTickets() {
        long startTime = observabilityService.startTiming();
        try {
            List<Ticket> tickets = ticketRepository.findAll();
            return tickets.stream()
                    .map(ticket -> modelMapper.map(ticket, TicketDto.class))
                    .collect(Collectors.toList());
        } finally {
            observabilityService.stopTiming(startTime, "database");
        }
    }

    public TicketDto getTicketById(Long id) {
        long startTime = observabilityService.startTiming();
        try {
            Ticket ticket = ticketRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Ticket with ID " + id + " not found"));
            return modelMapper.map(ticket, TicketDto.class);
        } finally {
            observabilityService.stopTiming(startTime, "database");
        }
    }

    public void deleteTicket(Long id) {
        long startTime = observabilityService.startTiming();
        try {
            ticketRepository.deleteById(id);
        } catch (Exception e) {
            throw new NoSuchElementException("Ticket with ID " + id + " not found");
        } finally {
            observabilityService.stopTiming(startTime, "database");
        }
    }

    public TicketDto saveTicket(TicketDto ticketDto) {
        long startTime = observabilityService.startTiming();
        try {
            Ticket ticket = modelMapper.map(ticketDto, Ticket.class);
            Ticket savedTicket = ticketRepository.save(ticket);
            return modelMapper.map(savedTicket, TicketDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to save ticket due to invalid or duplicate data.", e);
        } finally {
            observabilityService.stopTiming(startTime, "database");
        }
    }

    public TicketDto updateTicket(Long id, TicketDto updatedTicketDto) {
        long startTime = observabilityService.startTiming();
        try {
            Ticket existingTicket = ticketRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Ticket with ID " + id + " not found"));

            BeanUtils.copyProperties(updatedTicketDto, existingTicket, getNullPropertyNames(updatedTicketDto));

            Ticket savedTicket = ticketRepository.save(existingTicket);
            return modelMapper.map(savedTicket, TicketDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update ticket with ID " + id, e);
        } finally {
            observabilityService.stopTiming(startTime, "database");
        }
    }

    public Map<LocalDate, Long> getMaxViewersByDay(String movieName) {
        long startTime = observabilityService.startTiming();
        try {
            Long movieId = movieRepository.findByName(movieName)
                    .orElseThrow(() -> new NoSuchElementException("Нет фильма с названием " + movieName))
                    .getId();
            List<Object[]> results = ticketRepository.countTicketsByMoviePerDate(movieId);
            return results.stream()
                    .collect(Collectors.toMap(
                            row -> ((java.sql.Date) row[0]).toLocalDate(),
                            row -> (Long) row[1],
                            (v1, v2) -> v1
                    ));
        } finally {
            observabilityService.stopTiming(startTime, "additionalStats");
        }
    }
}