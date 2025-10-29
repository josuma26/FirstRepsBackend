package com.firstreps.backend.model

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "users")
data class User(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var role: Role,

    @Column(nullable = false, unique = true)
    var email: String,

    val firstName: String,

    val lastName: String,

    @Column(nullable = false)
    var passwordHash: String,

    var locale: String = "es-MX",

    var signupDate: OffsetDateTime = OffsetDateTime.now()
)
