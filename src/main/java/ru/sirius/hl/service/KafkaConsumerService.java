package ru.sirius.hl.service;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import lombok.*;
import org.springframework.kafka.annotation.*;
import org.springframework.stereotype.*;
import ru.sirius.hl.dto.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private static final String TOPIC = "var04";
    private static final String CONCURRENCY = "2";

    private final MovieService movieService;
    private final CustomerService customerService;
    private final TicketService ticketService;
    private final ObjectMapper objectMapper;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class KafkaMessage {

        @JsonProperty("entity")
        private String entity;

        @JsonProperty("operation")
        private String operation;

        @JsonProperty("payload")
        private JsonNode payload;

        @JsonProperty("correlationId")
        private String correlationId;

        public <T> T getPayloadAs(Class<T> type, ObjectMapper mapper) throws JsonProcessingException {
            return mapper.treeToValue(payload, type);
        }
    }

    @KafkaListener(topics = TOPIC, concurrency = CONCURRENCY)
    public void consume(KafkaMessage message) {
        try {
            switch (message.entity) {
                case "MOVIE" -> handleMovie(message);
                case "CUSTOMER" -> handleCustomer(message);
                case "TICKET" -> handleTicket(message);
                default -> System.err.println("Unknown entity: " + message.entity);
            }
        } catch (Exception e) {
            System.err.printf("Error processing %s %s: %s%nPayload: %s%n",
                    message.entity, message.operation, e.getMessage(), message.payload);
        }
    }

    private void handleMovie(KafkaMessage message) throws JsonProcessingException {
        switch (message.operation) {
            case "POST" -> movieService.saveMovie(message.getPayloadAs(MovieDto.class, objectMapper));
            case "PUT" -> {
                UpdateRequest<MovieDto> request = message.getPayloadAs(UpdateRequest.class, objectMapper);
                movieService.updateMovie(request.getId(), request.getDto());
            }
            case "DEL" -> movieService.deleteMovie(message.getPayloadAs(DeleteRequest.class, objectMapper).getId());
            case "GET_ALL" -> movieService.getAllMovies();
            case "GET_BY_ID" -> movieService.getMovieById(message.getPayloadAs(GetByIdRequest.class, objectMapper).getId());
            default -> System.err.println("Unknown movie operation: " + message.operation);
        }
    }

    private void handleCustomer(KafkaMessage message) throws JsonProcessingException {
        switch (message.operation) {
            case "POST" -> customerService.saveCustomer(message.getPayloadAs(CustomerDto.class, objectMapper));
            case "PUT" -> {
                UpdateRequest<CustomerDto> request = message.getPayloadAs(UpdateRequest.class, objectMapper);
                customerService.updateCustomer(request.getId(), request.getDto());
            }
            case "DEL" -> customerService.deleteCustomer(message.getPayloadAs(DeleteRequest.class, objectMapper).getId());
            case "GET_ALL" -> customerService.getAllCustomers();
            case "GET_BY_ID" -> customerService.getCustomerById(message.getPayloadAs(GetByIdRequest.class, objectMapper).getId());
            default -> System.err.println("Unknown customer operation: " + message.operation);
        }
    }

    private void handleTicket(KafkaMessage message) throws JsonProcessingException {
        switch (message.operation) {
            case "POST" -> ticketService.saveTicket(message.getPayloadAs(TicketDto.class, objectMapper));
            case "DEL" -> ticketService.deleteTicket(message.getPayloadAs(DeleteRequest.class, objectMapper).getId());
            case "GET_BY_ID" -> ticketService.getTicketById(message.getPayloadAs(GetByIdRequest.class, objectMapper).getId());
            case "GET_ALL" -> ticketService.getAllTickets();
            case "STATS" -> ticketService.getMaxViewersByMovie(message.getPayloadAs(StatsRequest.class, objectMapper).getMovieName());
            default -> System.err.println("Unknown ticket operation: " + message.operation);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UpdateRequest<T> {
        private Long id;
        private T dto;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteRequest {
        private Long id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetByIdRequest {
        private Long id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatsRequest {
        private String movieName;
    }
}