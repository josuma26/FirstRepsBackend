package com.firstreps.backend.model

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "coach_posts")
data class CoachPost(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: UUID? = null,

    @ManyToOne
    @MapsId
    @JoinColumn(name = "coach_id")
    val coach: Coach,

    @Column(name = "coach_id", insertable = false, updatable = false)
    val coachId: UUID? = null,

    var type: PostType?,

    @Column(columnDefinition = "text")
    var body: String? = null,

    var status: PostStatus = PostStatus.DRAFT,

    var createdAt: OffsetDateTime = OffsetDateTime.now()
)
