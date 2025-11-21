package com.Infinity.lost_and_found.database.repository

import com.Infinity.lost_and_found.database.models.FoundItem
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface FoundItemRepository: MongoRepository<FoundItem, ObjectId> {
    fun findByUserId(userId: String): List<FoundItem>
}