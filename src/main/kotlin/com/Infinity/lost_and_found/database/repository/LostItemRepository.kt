package com.Infinity.lost_and_found.database.repository

import com.Infinity.lost_and_found.database.models.FoundItem
import com.Infinity.lost_and_found.database.models.LostItem
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface LostItemRepository: MongoRepository<LostItem, ObjectId> {
    fun findByUserId(userId: String): List<LostItem>
}