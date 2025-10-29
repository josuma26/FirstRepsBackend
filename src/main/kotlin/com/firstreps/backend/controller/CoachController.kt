package com.firstreps.backend.controller

import com.firstreps.backend.model.Coach
import com.firstreps.backend.model.CoachPost
import com.firstreps.backend.model.PostStatus
import com.firstreps.backend.model.PostType
import com.firstreps.backend.repository.CoachPostRepository
import com.firstreps.backend.repository.CoachRepository
import org.apache.coyote.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

data class CreatePostRequest(val description: String, val type: PostType, val status: PostStatus)

@RestController
@RequestMapping("/coaches")
class CoachController(
    private val coachRepository: CoachRepository,
    private val coachPostRepository: CoachPostRepository) {


    @GetMapping("/{id}")
    fun getCoach(@PathVariable id: UUID): ResponseEntity<Any> {
        val coach = coachRepository.findById(id)
        return if (coach.isPresent) ResponseEntity.ok(coach.get()) else ResponseEntity.notFound().build()
    }

    @GetMapping("/{id}/posts")
    fun getPosts(@PathVariable id: UUID): ResponseEntity<Any> {
        val coach = coachRepository.findById(id)
        if (!coach.isPresent) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok(coachPostRepository.findAllByCoachId(coachId = coach.get().userId!!))
    }

    @PostMapping("/{id}/post")
    fun createPost(@PathVariable id: UUID, @RequestBody request: CreatePostRequest): ResponseEntity<Any> {
        val coach = coachRepository.findById(id)
        if (!coach.isPresent) {
            return ResponseEntity.notFound().build()
        }
        val post = CoachPost(coach = coach.get(), body = request.description, type = request.type, status = request.status)
        coachPostRepository.save(post)
        return ResponseEntity.ok(post.id)
    }

    @GetMapping("/posts")
    fun getAllPosts(): ResponseEntity<Any> {
        return ResponseEntity.ok(coachPostRepository.findAll());
    }
}