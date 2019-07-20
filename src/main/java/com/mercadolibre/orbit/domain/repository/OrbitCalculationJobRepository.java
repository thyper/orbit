package com.mercadolibre.orbit.domain.repository;

import com.mercadolibre.orbit.domain.enums.JobStatus;
import com.mercadolibre.orbit.domain.model.OrbitCalculationJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface OrbitCalculationJobRepository extends JpaRepository<OrbitCalculationJob, Long> {

    @Query("SELECT ocj FROM orbitcalculationjobs ocj WHERE ocj.jobStatus = :jobstatus ORDER BY ocj.id DESC")
    OrbitCalculationJob getLast(@Param("jobstatus") JobStatus jobStatus);

}
