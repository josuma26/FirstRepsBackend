package com.firstreps.backend.repository

import com.firstreps.backend.model.Athlete
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AthleteRepository: JpaRepository<Athlete, UUID> {
}