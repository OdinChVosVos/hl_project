package ru.hpclab.hl.module1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hpclab.hl.module1.model.Customer;
import ru.hpclab.hl.module1.model.Ticket;

import java.time.LocalDateTime;


public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.movie.id = :movieId AND DATE(t.sessionDate) = DATE(:date)")
    Long countTicketsByMovieAndDate(
            @Param("movieId") Long movieId,
            @Param("date") LocalDateTime date);

}
