package com.Infinity.lost_and_found.controllers

import com.cloudinary.Cloudinary
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/upload")
class ImageUploadController(
    private val cloudinary: Cloudinary
) {
    data class response(
        val imageUrl: String
    )
    /*
    @PostMapping( consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadImage(@RequestParam("image") file: MultipartFile): /*ResponseEntity<Map<String, String>>*/ response {
        print("Image recieved")
        val uploadResult = cloudinary.uploader().upload(file.bytes, mapOf("folder" to "my_uploads"))
        val url = uploadResult["secure_url"] as String
        return response(
            imageUrl = url
        )
    }
     */


    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadImage(@RequestParam("image") file: MultipartFile): ResponseEntity<Any> {
        return try {
            println("Image received")
            val uploadResult = cloudinary.uploader().upload(file.bytes, mapOf("folder" to "my_uploads"))
            val url = uploadResult["secure_url"] as String
            ResponseEntity.ok(mapOf("imageUrl" to url))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("message" to "Failed to upload image"))
        }
    }
}
