package org.asphalt.lubricant.domain.sample.persistence.jpa

import org.asphalt.lubricant.domain.sample.Sample
import org.asphalt.lubricant.domain.sample.service.SampleJdbcRepository
import org.springframework.data.jpa.repository.JpaRepository

interface SampleJpaRepository :
    SampleJdbcRepository,
    JpaRepository<Sample, Long>
