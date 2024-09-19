package org.asphalt.lubricant.sample.persistence.jpa

import org.asphalt.lubricant.sample.Sample
import org.asphalt.lubricant.sample.service.SampleJdbcRepository
import org.springframework.data.jpa.repository.JpaRepository

interface SampleJpaRepository :
    SampleJdbcRepository,
    JpaRepository<Sample, Long>
