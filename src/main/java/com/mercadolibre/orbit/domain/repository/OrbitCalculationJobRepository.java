package com.mercadolibre.orbit.domain.repository;

import com.mercadolibre.orbit.domain.model.OrbitCalculationJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrbitCalculationJobRepository extends JpaRepository<OrbitCalculationJob, Long> {
}
