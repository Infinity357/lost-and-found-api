package com.Infinity.lost_and_found.controllers

import com.Infinity.lost_and_found.database.models.User
import com.Infinity.lost_and_found.database.repository.UserRepository
import com.Infinity.lost_and_found.security.HashEncoder
import io.jsonwebtoken.security.Password
import org.bson.types.ObjectId
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder
) {

    data class UserIdFind(
        val firstName: String,
        val lastName: String,
        val email: String
    )

    data class authRequest(
        val firstName: String,
        val lastName: String,
        val email: String,
        val password: String
    )

    data class authLoginRequest(
        val email: String,
        val password: String
    )

    data class registerReturn(
        val userId: String
    )

    data class loginReturn(
        val email: String,
        val userId: String,
        val firstName: String,
        val lastName: String
    )

    @PostMapping("/register")
    fun register(
        @RequestBody body: authRequest
    ): registerReturn{
        if(userRepository.findByEmail(body.email)!=null){
            throw BadCredentialsException("Email already in use")
        }
        userRepository.save(
            User(
                email = body.email,
                hashedPassword =hashEncoder.encode(body.password),
                firstName = body.firstName,
                lastName = body.lastName
            )
        )
        val user = userRepository.findByEmail(body.email) ?:
        throw BadCredentialsException("Invalid")
        return registerReturn(
            userId = user.userId.toHexString()
        )
    }

    @PostMapping("/login")
    fun login(
        @RequestBody body: authLoginRequest
    ): loginReturn{
        val user = userRepository.findByEmail(body.email) ?:
        throw BadCredentialsException("Invalid Credentials")

        if(!hashEncoder.matches(body.password , user.hashedPassword)){
            throw BadCredentialsException("Invalid Credentials")
        }

        return loginReturn(
            email = user.email,
            userId = user.userId.toHexString(),
            firstName = user.firstName,
            lastName = user.lastName
        )
    }

    @GetMapping
    fun findByUserId(
        @RequestParam(required = true) userId: String
    ): UserIdFind {
        val user = userRepository.findByUserId(ObjectId(userId)) ?:
        throw IllegalArgumentException("User Not Found")

        return UserIdFind(
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email
        )
    }
}