package com.firstreps.backend.repository

import com.firstreps.backend.model.CoachPost
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CoachPostRepository: JpaRepository<CoachPost, UUID> {
    fun findAllByCoachId(coachId: UUID): List<CoachPost>
}