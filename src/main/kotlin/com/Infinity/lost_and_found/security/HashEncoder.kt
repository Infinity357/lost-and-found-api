package com.Infinity.lost_and_found.security

import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class HashEncoder {
    //this is from a library BCrypt which is present in spring boot security
    private val bcrypt = BCryptPasswordEncoder()

    fun encode(raw: String): String = bcrypt.encode(raw)

    //we use bcrypt.matches as bcrypt hashing always return a different hash each time even with the same password as it adds a random salt
    //thus bcrypt.matches() hashes the raw and removes the salt from hashed and hashes them from the same hash to check if they are equal or not
    fun matches(raw: String, hashed: String): Boolean = bcrypt.matches(raw,hashed)
}
