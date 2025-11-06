package com.firstreps.backend.seed

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.firstreps.backend.model.*
import com.firstreps.backend.repository.AthleteRepository
import com.firstreps.backend.repository.CoachPostRepository
import com.firstreps.backend.repository.CoachRepository
import com.firstreps.backend.repository.UserRepository
import com.firstreps.backend.service.PasswordService
import org.apache.coyote.http11.Constants.a
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ResourceLoader
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.util.*
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

@Profile("dev")
@Component
class Seeder(
    private val resourceLoader: ResourceLoader,
    private val coachRepository: CoachRepository,
    private val coachPostRepository: CoachPostRepository,
    private val athleteRepository: AthleteRepository,
    private val passwordService: PasswordService,
    private val objectMapper: ObjectMapper
): CommandLineRunner {

    override fun run(vararg args: String?) {
        seedCoaches()
        seedCoachPosts()
        seedAthletes()
    }

    private fun seedCoaches() {
        seed("coach", coachRepository) { obj ->
            val user: User = mapToTarget(obj, mapOf(
                "id" to { UUID.randomUUID() },
                "role" to { Role.COACH },
                "passwordHash" to { passwordService.hash(obj["password"].toString()) },
                "signupDate" to { OffsetDateTime.now() }
            ))
            setProperties(obj, Coach(user = user))
        }
    }

    private fun seedCoachPosts() {
        seed("coachPost", coachPostRepository) {obj ->
            val coach = coachRepository.findByUserEmail(obj["coachEmail"].toString()) ?: throw IllegalArgumentException("No coach found!")
            mapToTarget<CoachPost>(obj, mapOf(
                "coach" to { coach },
                "type" to { PostType.valueOf(it.toString()) },
                "status" to { PostStatus.valueOf(it.toString()) },
                "createdAt" to { OffsetDateTime.now() }
            ))
        }
    }

    private fun seedAthletes() {
        seed("athlete", athleteRepository) { obj ->
            val user = mapToTarget<User>(obj, mapOf(
                "id" to { UUID.randomUUID() },
                "role" to { Role.ATHLETE },
                "passwordHash" to { passwordService.hash(obj["password"].toString()) },
                "signupDate" to { OffsetDateTime.now() }
            ))

            setProperties(obj, Athlete(user = user))
        }
    }

    private fun <T : Any, ID> seed(resourceFileName: String, repository: JpaRepository<T, ID>, create: (Map<String, Any>) -> T) {
        if (repository.count() > 0) {
            return
        }
        readResources(resourceFileName).map { create(it) }.forEach { repository.save(it) }

    }


    private inline fun <reified T: Any> mapToTarget(obj: Map<String, Any>, override: Map<String, (Any?) -> Any?>): T {
        val constructor = T::class.primaryConstructor ?: throw IllegalArgumentException("Target class has no primary constructor.")
        val args = constructor.parameters.associateWith { param ->
            override[param.name]?.let {
                it(obj[param.name])
            } ?: obj[param.name]
        }
        return constructor.callBy(args)
    }

    private inline fun <reified T: Any> setProperties(source: Map<String, Any>, targetInstance: T): T {
        T::class.memberProperties
            .filterIsInstance<KMutableProperty1<T, Any?>>()
            .forEach { targetProp ->
                source[targetProp.name]?.let { value ->
                    targetProp.set(targetInstance, value)
                }
            }
        return targetInstance
    }

    private fun readResources(kind: String): List<Map<String, Any>> {
        val contents = resourceLoader.getResource("classpath:seed/${kind}.json")
        return objectMapper.readValue(contents.url, object: TypeReference<List<Map<String, Any>>>() {})
    }
}