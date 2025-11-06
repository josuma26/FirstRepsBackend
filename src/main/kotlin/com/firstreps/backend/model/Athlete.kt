package com.firstreps.backend.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "athlete_profiles")
data class Athlete(
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: UUID? = null,

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    val user: User,

    @Column
    var sports: List<String> = listOf(),

    @Column(columnDefinition = "text")
    var goals: String? = null,

    @Column(name = "budget_min")
    var budgetMin: Int? = null,

    @Column(name = "budget_max")
    var budgetMax: Int? = null
)
