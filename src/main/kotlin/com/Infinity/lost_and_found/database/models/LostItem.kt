package com.Infinity.lost_and_found.database.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("LostItems")
data class LostItem(
    @Id val itemId: ObjectId = ObjectId(),
    val itemName: String,
    val description: String,
    val lostLocation: String?,
    val lostDate: String,
    val imageUrl: String,
    val userId: String,
    val founderUserId: MutableList<String> = mutableListOf(),
    val createdAt: Instant = Instant.now()
)
