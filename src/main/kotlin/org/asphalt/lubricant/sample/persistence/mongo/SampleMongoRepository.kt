package org.asphalt.lubricant.sample.persistence.mongo

import org.asphalt.lubricant.sample.SampleDocument
import org.asphalt.lubricant.sample.service.SampleDocumentRepository
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface SampleMongoRepository :
    SampleDocumentRepository,
    MongoRepository<SampleDocument, ObjectId>
