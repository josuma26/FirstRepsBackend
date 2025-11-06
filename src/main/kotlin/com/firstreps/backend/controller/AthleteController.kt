package com.firstreps.backend.controller

import com.firstreps.backend.repository.AthleteRepository
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

data class UpdateAthleteRequest(val sports: List<String>, val goals: String)

@RestController
@RequestMapping("/athletes")
class AthleteController(
    private val athleteRepository: AthleteRepository
) {

    @GetMapping("/{athleteId}")
    fun getAthlete(@PathVariable athleteId: UUID): ResponseEntity<Any> {
        val athlete = athleteRepository.findById(athleteId)
        return if (athlete.isPresent) ResponseEntity.ok(athlete.get()) else ResponseEntity.notFound().build()
    }

    @PutMapping("/{athleteId}")
    fun getAthlete(@PathVariable athleteId: UUID, @RequestBody updateAthleteRequest: UpdateAthleteRequest): ResponseEntity<Any> {
        val athlete = athleteRepository.findById(athleteId)
        if (athlete.isEmpty) {
            return ResponseEntity.notFound().build()
        }
        athleteRepository.save(athlete.get().copy(
            goals = updateAthleteRequest.goals,
            sports = updateAthleteRequest.sports
        ))
        return ResponseEntity.ok(athlete)
    }
}