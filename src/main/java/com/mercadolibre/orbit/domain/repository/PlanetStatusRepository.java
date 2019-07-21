package com.mercadolibre.orbit.domain.repository;

import com.mercadolibre.orbit.domain.model.PlanetStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PlanetStatusRepository extends JpaRepository<PlanetStatus, Long> {

    @Query("SELECT ps FROM planets_status ps WHERE planet_id = :pid ORDER BY ID DESC")
    List<PlanetStatus> getLastStatus(@Param("pid") Long planetId, Pageable pageable);

}
