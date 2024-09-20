package org.asphalt.lubricant.domain.sample.service

import org.asphalt.lubricant.domain.sample.Sample

interface SampleJdbcRepository {
    fun save(sample: Sample): Sample
}
