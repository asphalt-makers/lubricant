package org.asphalt.lubricant.sample.service

import org.asphalt.lubricant.sample.SampleDocument

interface SampleDocumentRepository {
    fun save(sampleDocument: SampleDocument): SampleDocument
}
