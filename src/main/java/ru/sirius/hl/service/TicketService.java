package ru.sirius.hl.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import ru.sirius.hl.dto.TicketDto;
import ru.sirius.hl.model.Ticket;
import ru.sirius.hl.repository.TicketRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static ru.sirius.hl.service.BeanUtilsHelper.getNullPropertyNames;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;


    @Value("${additional.app.host:additional-app}")
    private String ADDITIONAL_APP_HOST;

    @Transactional
    public void clearAll() {
        ticketRepository.deleteAll();
    }


    // Get all tickets (no pagination)
    public List<TicketDto> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();

        return tickets.stream()
                .map(ticket -> modelMapper.map(ticket, TicketDto.class))
                .collect(Collectors.toList());
    }



    // Get ticket by ID
    public TicketDto getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ticket with ID " + id + " not found"));

        return modelMapper.map(ticket, TicketDto.class);
    }



    // Delete ticket by ID (physical deletion)
    public void deleteTicket(Long id) {
        try {
            ticketRepository.deleteById(id);
        } catch (Exception e) {
            throw new NoSuchElementException("Ticket with ID " + id + " not found");
        }
    }


    // Save a new ticket
    public TicketDto saveTicket(TicketDto ticketDto) {
        try {
            Ticket ticket = modelMapper.map(ticketDto, Ticket.class);
            Ticket savedTicket = ticketRepository.save(ticket);

            return modelMapper.map(savedTicket, TicketDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Failed to save ticket due to invalid or duplicate data.", e);
        }
    }

    // Update an existing ticket
    public TicketDto updateTicket(Long id, TicketDto updatedTicketDto) {
        try {
            Ticket existingTicket = ticketRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Ticket with ID " + id + " not found"));

            // Update fields, ignoring null values
            BeanUtils.copyProperties(updatedTicketDto, existingTicket, getNullPropertyNames(updatedTicketDto));

            Ticket savedTicket = ticketRepository.save(existingTicket);
            return modelMapper.map(savedTicket, TicketDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update ticket with ID " + id, e);
        }
    }

}