package ru.sirius.hl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sirius.hl.model.Ticket;

import java.time.LocalDate;
import java.util.Map;


public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query(value = """
        SELECT day, MAX(ticket_count) AS max_tickets
        FROM (
            SELECT CAST(t.session_date AS DATE) AS day,
                   COUNT(*) AS ticket_count
            FROM public.ticket t
            WHERE t.movie_id = :movieId
            GROUP BY t.session_date
        ) sub
        GROUP BY day
    """, nativeQuery = true)
    Map<LocalDate, Long> countTicketsByMoviePerDate(@Param("movieId") Long movieId);


}
