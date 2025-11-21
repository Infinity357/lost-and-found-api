package com.Infinity.lost_and_found.database.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class User(
    @Id val userId: ObjectId = ObjectId(),
    val email: String,
    val hashedPassword: String,
    val firstName: String,
    val lastName: String
)

