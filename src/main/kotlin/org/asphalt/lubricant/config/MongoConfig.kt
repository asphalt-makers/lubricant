package org.asphalt.lubricant.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.MongoConfigurationSupport

@Configuration
class MongoConfig : MongoConfigurationSupport() {
    @Autowired
    lateinit var mongoProperties: MongoProperties

    override fun getDatabaseName(): String = mongoProperties.mongoClientDatabase
}
