package org.asphalt.lubricant.sample.service

import org.asphalt.lubricant.sample.Sample
import org.springframework.data.jpa.repository.JpaRepository

interface SampleJpaRepository : JpaRepository<Sample, Long>
