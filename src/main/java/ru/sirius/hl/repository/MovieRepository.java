package ru.sirius.hl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sirius.hl.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
