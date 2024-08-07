package org.asphalt.lubricant.sample.service

import org.asphalt.lubricant.sample.SampleDocument
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface SampleDocumentRepository : MongoRepository<SampleDocument, ObjectId>
