package com.Infinity.lost_and_found.database.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("FoundItems")
data class FoundItem(
    @Id val itemId: ObjectId = ObjectId(),
    val itemName: String,
    val description: String,
    val foundLocation: String,
    val foundDate: String,
    val imageUrl: String,
    val userId: String,
    val claimerUserId: MutableList<String> = mutableListOf(),
    val createdAt: Instant = Instant.now())