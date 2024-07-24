package org.asphalt.lubricant.domain.sample

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface SampleDocumentRepository : MongoRepository<SampleDocument, ObjectId>
