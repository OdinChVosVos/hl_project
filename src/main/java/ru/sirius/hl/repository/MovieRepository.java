package ru.sirius.hl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sirius.hl.model.Movie;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query(value = "SELECT * FROM movie WHERE name = :name LIMIT 1", nativeQuery = true)
    Optional<Movie> findByName(@Param("name") String name);
}
