package ru.hpclab.hl.module1.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;



@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketDto {
    private Long id;
    private Long movie;
    private String movieName;
    private Long customer;
    private String customerName;
    private LocalDateTime sessionDate;
    private int seat;
    private float price;
}
