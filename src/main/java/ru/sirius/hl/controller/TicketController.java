package ru.sirius.hl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sirius.hl.dto.TicketDto;
import ru.sirius.hl.service.ObservabilityService;
import ru.sirius.hl.service.TicketService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ticket")
@RequiredArgsConstructor
@Tag(name = "Ticket Controller")
public class TicketController {

    private final TicketService ticketService;
    private final ObservabilityService observabilityService;

    @DeleteMapping("/clear")
    @Operation(summary = "Очистка всех билетов")
    public ResponseEntity<String> clearAll() {
        long startTime = observabilityService.startTiming();
        try {
            ticketService.clearAll();
            return ResponseEntity.ok("All tickets cleared");
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }

    @GetMapping
    @Operation(summary = "Получение всех билетов без пагинации")
    public ResponseEntity<List<TicketDto>> getTickets() {
        long startTime = observabilityService.startTiming();
        try {
            List<TicketDto> tickets = ticketService.getAllTickets();
            return ResponseEntity.ok(tickets);
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение билета по ID")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id) {
        long startTime = observabilityService.startTiming();
        try {
            TicketDto ticket = ticketService.getTicketById(id);
            return ResponseEntity.ok(ticket);
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Физическое удаление билета")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        long startTime = observabilityService.startTiming();
        try {
            ticketService.deleteTicket(id);
            return ResponseEntity.noContent().build();
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }

    @PostMapping
    @Operation(summary = "Создание нового билета")
    public ResponseEntity<TicketDto> saveTicket(@RequestBody TicketDto ticket) {
        long startTime = observabilityService.startTiming();
        try {
            TicketDto savedTicket = ticketService.saveTicket(ticket);
            return ResponseEntity.ok(savedTicket);
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновление существующего билета")
    public ResponseEntity<TicketDto> updateTicket(@PathVariable Long id, @RequestBody TicketDto ticket) {
        long startTime = observabilityService.startTiming();
        try {
            TicketDto updatedTicket = ticketService.updateTicket(id, ticket);
            return ResponseEntity.ok(updatedTicket);
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }

    @GetMapping("/statistics")
    @Operation(summary = "Получение максимального количества зрителей на фильме за каждый день")
    public ResponseEntity<Map<LocalDate, Long>> getMaxMovieViewersByDay(@RequestParam("movie") String movieName) {
        long startTime = observabilityService.startTiming();
        try {
            Map<LocalDate, Long> statistics = ticketService.getMaxViewersByDay(movieName);
            return ResponseEntity.ok(statistics);
        } finally {
            observabilityService.stopTiming(startTime, "controller");
        }
    }
}