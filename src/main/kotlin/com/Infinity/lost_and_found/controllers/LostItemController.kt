package com.Infinity.lost_and_found.controllers

import com.Infinity.lost_and_found.controllers.FoundItemController.foundResponse
import com.Infinity.lost_and_found.database.models.LostItem
import com.Infinity.lost_and_found.database.repository.LostItemRepository
import com.Infinity.lost_and_found.database.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@RestController
@RequestMapping("/lost")
class LostItemController(
    private val lostItemRepository: LostItemRepository,
    private val userRepository: UserRepository
) {
    data class lostRequest(
        val itemId: String?,
        val itemName: String,
        val description: String,
        val lostLocation: String?,
        val lostDate: String,
        val imageUrl: String,
        val userId: String,
    )
/*
    {
        id: "8",
        name: "Laptop Charger",
        description: "MacBook Pro charger, 61W USB-C power adapter.",
        location: "Computer Lab, Building C",
        date: "2023-04-17",
        image: "/placeholder.svg?height=400&width=400",
        status: "lost",
        isOwner: true,
    },
   */
    data class lostResponse(
        val itemId: String,
        val itemName: String,
        val description: String,
        val lostLocation: String?,
        val lostDate: String,
        val imageUrl: String,
        val founderUserIds: List<String>,
        val userId: String
    )

    @PostMapping
    fun save(
        @RequestBody body: lostRequest
    ): lostResponse{
        val item = lostItemRepository.save(
            LostItem(
                itemId = body.itemId?.let { ObjectId(it) } ?: ObjectId.get(),
                itemName = body.itemName,
                description = body.description,
                lostLocation = body.lostLocation,
                lostDate = body.lostDate,
                imageUrl = body.imageUrl,
                userId = body.userId,
                createdAt = Instant.now(),
            )
        )

        return item.toResponse()
    }
    @GetMapping
    fun getLostItems(@RequestParam(required = false) userId: String?): List<lostResponse> {
        return try {
    if (userId != null) {
        lostItemRepository.findByUserId(userId).map { it.toResponse() }
    } else {
        lostItemRepository.findAll().map { it.toResponse() }
    }
} catch (e: IllegalArgumentException) {
    print("invalid id ")
            print(userId)
    throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid userId format", e) as Throwable
}
    }
/*
    @GetMapping
    fun getAll(): List<lostResponse>{
        return lostItemRepository.findAll().map {
            it.toResponse()
        }
    }

    @GetMapping
    fun findByOwnerId(
        @RequestParam(required = true) userId: String
    ): List<lostResponse>{
        return lostItemRepository.findByUserId(ObjectId(userId)).map {
            it.toResponse()
        }
    }
    */
@GetMapping("/{itemId}")
fun getItemById(
    @PathVariable itemId: String
): lostResponse{
    println(itemId)
    val item = lostItemRepository.findById(ObjectId(itemId)).orElseThrow{
        throw IllegalArgumentException("Item not found")
    }
    return item.toResponse()
}

    @DeleteMapping(path = ["/{itemId}"])
    fun deleteByItemId(
        @PathVariable itemId: String
    ) {
        val note =lostItemRepository.findById(ObjectId(itemId)).orElseThrow {
            IllegalArgumentException("Item not lost")
        }

        lostItemRepository.deleteById(ObjectId(itemId))
    }

    @PostMapping("/claim/{userId}/{itemId}")
    fun putClaim(
        @PathVariable itemId: String,
        @PathVariable userId: String
    ){
        val user = userRepository.findByUserId(ObjectId(userId))
            ?: throw IllegalArgumentException("User Not Found")

        val item = lostItemRepository.findById(ObjectId(itemId)).orElseThrow {
            throw IllegalArgumentException("Item not lost")
        }

        synchronized(item) {
    if (!item.founderUserId.contains(userId)) {
        item.founderUserId.add(userId)
    } else {
       // throw IllegalArgumentException("Already claimed")
    }
}

        lostItemRepository.save(item)
    }

    private fun LostItem.toResponse(): lostResponse{
        return lostResponse(
            itemId = itemId.toHexString(),
            itemName = itemName,
            description = description,
            lostLocation = lostLocation,
            lostDate = lostDate,
            imageUrl = imageUrl,
            founderUserIds = founderUserId,
            userId= userId
        )
    }
}
