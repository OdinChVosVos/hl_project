package ru.sirius.hl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sirius.hl.model.Ticket;


public interface TicketRepository extends JpaRepository<Ticket, Long> {

//    @Query(value = """
//            SELECT MAX(ticketCount)\s
//            FROM (
//                SELECT COUNT(t) AS ticketCount
//                FROM public.ticket t
//                WHERE t.movie_id = :movieId
//                AND DATE(t.session_date) = :sessionDate
//                GROUP BY t.session_date
//            ) as foo
//    """, nativeQuery = true)
//    Long countTicketsByMovieAndDate(
//            @Param("movieId") Long movieId,
//            @Param("sessionDate") LocalDate sessionDate);

}
