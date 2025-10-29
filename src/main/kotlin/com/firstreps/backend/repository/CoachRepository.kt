package com.firstreps.backend.repository

import com.firstreps.backend.model.Coach
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CoachRepository: JpaRepository<Coach, UUID> {

}