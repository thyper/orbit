package com.mercadolibre.orbit.domain.repository;

import com.mercadolibre.orbit.domain.enums.JobStatus;
import com.mercadolibre.orbit.domain.model.jpa.OrbitCalculationJob;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrbitCalculationJobRepository extends JpaRepository<OrbitCalculationJob, Long> {

    @Query("SELECT ocj FROM orbitcalculationjobs ocj WHERE ocj.jobStatus = :jobstatus ORDER BY ocj.id DESC")
    List<OrbitCalculationJob> getLast(@Param("jobstatus") JobStatus jobStatus, Pageable pageable);

    @Query("SELECT COUNT(ocj) FROM orbitcalculationjobs ocj WHERE ocj.jobStatus = :jobStatus")
    int countByStatus(@Param("jobStatus") JobStatus jobStatus);

}
