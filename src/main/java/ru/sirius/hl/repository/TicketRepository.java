package ru.sirius.hl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sirius.hl.model.Ticket;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query(value = """
        SELECT CAST(t.session_date AS DATE) AS day, COUNT(*) AS ticket_count
        FROM public.ticket t
        WHERE t.movie_id = :movieId
        GROUP BY CAST(t.session_date AS DATE)
    """, nativeQuery = true)
    List<Object[]> countTicketsByMoviePerDate(@Param("movieId") Long movieId);


}
