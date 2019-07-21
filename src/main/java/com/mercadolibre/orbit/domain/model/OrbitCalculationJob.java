package com.mercadolibre.orbit.domain.model;


import com.mercadolibre.orbit.domain.enums.JobStatus;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.Calendar;

@Entity(name = "orbitcalculationjobs")
@Table(name = "orbitcalculationjobs")
public class OrbitCalculationJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Date creationDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private JobStatus jobStatus;



    public OrbitCalculationJob() {
        this.setJobStatus(JobStatus.CREATED);
    }




    /*
    Getters & Setters
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }
}
