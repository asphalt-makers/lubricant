package org.asphalt.lubricant.sample.service

import org.asphalt.lubricant.sample.Sample

interface SampleJdbcRepository {
    fun save(sample: Sample): Sample
}
