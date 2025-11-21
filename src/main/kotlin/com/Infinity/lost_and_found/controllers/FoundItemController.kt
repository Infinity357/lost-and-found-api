package com.Infinity.lost_and_found.controllers

import com.Infinity.lost_and_found.controllers.LostItemController.lostResponse
import com.Infinity.lost_and_found.database.models.FoundItem
import com.Infinity.lost_and_found.database.repository.FoundItemRepository
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/found")
class FoundItemController(
    private val foundItemRepository: FoundItemRepository
) {
    data class foundRequest(
        val itemId: String?,
        val itemName: String,
        val description: String,
        val foundLocation: String,
        val foundDate: String,
        val imageUrl: String,
        val userId: String,
    )
    data class foundResponse(
        val itemId: String,
        val itemName: String,
        val description: String,
        val foundLocation: String,
        val foundDate: String,
        val imageUrl: String,
        val claimerUserIds: List<String>,
        val userId: String
    )

@PostMapping
fun save(
    @RequestBody body: foundRequest
): foundResponse{
    val item = foundItemRepository.save(
        FoundItem(
            itemId = body.itemId?.let { ObjectId(it) } ?: ObjectId.get(),
            itemName = body.itemName,
            description = body.description,
            foundLocation = body.foundLocation,
            foundDate = body.foundDate,
            imageUrl = body.imageUrl,
            userId = body.userId,
            createdAt = Instant.now(),
        )
    )

    return item.toResponse()
}

    @GetMapping
    fun getFoundItems(@RequestParam(required = false) userId: String?): List<foundResponse> {
        return if (userId != null) {
            print("found by userId")
            foundItemRepository.findByUserId(userId).map { it.toResponse() }
        } else {
            foundItemRepository.findAll().map { it.toResponse() }
        }
    }
/*
    @GetMapping
    fun findByOwnerId(
        @RequestParam(required = true) userId: String
    ): List<foundResponse>{
        return foundItemRepository.findByUserId(ObjectId(userId)).map {
            it.toResponse()
        }
    }
*/
    @DeleteMapping(path = ["/{itemId}"])
    fun deleteByItemId(
        @PathVariable itemId: String
    ) {
        val note =foundItemRepository.findById(ObjectId(itemId)).orElseThrow {
            IllegalArgumentException("Item not found")
        }

        foundItemRepository.deleteById(ObjectId(itemId))
    }

    @GetMapping("/{itemId}")
    fun getItemById(
        @PathVariable itemId: String
    ): foundResponse{
        val item = foundItemRepository.findById(ObjectId(itemId)).orElseThrow{
            throw IllegalArgumentException("Item not found")
        }
        return item.toResponse()
    }

    @PostMapping("/claim/{userId}/{itemId}")
    fun putClaim(
        @PathVariable itemId: String,
        @PathVariable userId: String
    ){
        val item = foundItemRepository.findById(ObjectId(itemId)).orElseThrow {
            throw IllegalArgumentException("Item not found")
        }

        item.claimerUserId.add(userId)

        foundItemRepository.save(item)
    }

    private fun FoundItem.toResponse(): foundResponse{
        return foundResponse(
            itemId = itemId.toHexString(),
            itemName = itemName,
            description = description,
            foundLocation = foundLocation,
            foundDate = foundDate,
            imageUrl = imageUrl,
            claimerUserIds = claimerUserId,
            userId = userId
        )
    }
}