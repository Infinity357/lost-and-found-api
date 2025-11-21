package com.Infinity.lost_and_found.clowdinary

import com.cloudinary.Cloudinary
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CloudinaryConfig(
    @Value("\${cloudinary.cloud_name}") private val cloudName: String,
    @Value("\${cloudinary.api_key}") private val apiKey: String,
    @Value("\${cloudinary.api_secret}") private val apiSecret: String
) {
    @Bean
    fun cloudinary(): Cloudinary {
        val config: MutableMap<String, String> = mutableMapOf(
        "cloud_name" to cloudName,
            "api_key" to apiKey,
            "api_secret" to apiSecret
        )
        return Cloudinary(/* config  */ config)
    }
}
