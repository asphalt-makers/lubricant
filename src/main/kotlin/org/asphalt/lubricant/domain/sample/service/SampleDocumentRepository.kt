package org.asphalt.lubricant.domain.sample.service

import org.asphalt.lubricant.domain.sample.SampleDocument

interface SampleDocumentRepository {
    fun save(sampleDocument: SampleDocument): SampleDocument
}
