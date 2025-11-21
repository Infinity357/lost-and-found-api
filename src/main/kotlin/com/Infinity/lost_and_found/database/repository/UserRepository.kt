package com.Infinity.lost_and_found.database.repository

import com.Infinity.lost_and_found.database.models.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, ObjectId> {
    fun findByEmail(email: String): User?
    fun findByUserId(userId: ObjectId): User?
}