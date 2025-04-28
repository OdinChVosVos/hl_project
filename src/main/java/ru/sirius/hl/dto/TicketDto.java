package ru.sirius.hl.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("ticket")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class TicketDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("movie_id")
    private Long movie;

    @JsonProperty("movie_name")
    private String movieName;

    @JsonProperty("customer_id")
    private Long customer;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("session_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime sessionDate;

    @JsonProperty("seat_number")
    private int seat;

    @JsonProperty("ticket_price")
    private float price;
}