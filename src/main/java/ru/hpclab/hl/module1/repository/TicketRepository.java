package ru.hpclab.hl.module1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hpclab.hl.module1.model.Customer;
import ru.hpclab.hl.module1.model.Ticket;

import java.time.LocalDate;
import java.time.LocalDateTime;


public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query(value = """
            SELECT MAX(ticketCount)\s
            FROM (
                SELECT COUNT(t) AS ticketCount
                FROM public.ticket t
                WHERE t.movie_id = :movieId
                AND DATE(t.session_date) = :sessionDate
                GROUP BY t.session_date
            ) as foo
    """, nativeQuery = true)
    Long countTicketsByMovieAndDate(
            @Param("movieId") Long movieId,
            @Param("sessionDate") LocalDate sessionDate);

}
