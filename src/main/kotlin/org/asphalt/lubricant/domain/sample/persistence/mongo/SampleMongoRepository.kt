package org.asphalt.lubricant.domain.sample.persistence.mongo

import org.asphalt.lubricant.domain.sample.SampleDocument
import org.asphalt.lubricant.domain.sample.service.SampleDocumentRepository
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface SampleMongoRepository :
    SampleDocumentRepository,
    MongoRepository<SampleDocument, ObjectId>
