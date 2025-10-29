package com.firstreps.backend.model

import java.util.*
import jakarta.persistence.*;

@Entity
@Table(name = "coaches")
data class Coach(
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: UUID? = null,

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(columnDefinition = "text")
    var bio: String? = null,

    @Column(name = "specialties", columnDefinition = "text[]")
    var specialties: Array<String>? = null,

    var priceCents: Int? = null,

    var verifiedStatus: String = "pending"
)