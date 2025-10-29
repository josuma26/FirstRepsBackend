package com.firstreps.backend.controller

import com.firstreps.backend.model.Athlete
import com.firstreps.backend.model.Coach
import com.firstreps.backend.model.Role
import com.firstreps.backend.model.User
import com.firstreps.backend.repository.AthleteRepository
import com.firstreps.backend.repository.CoachRepository
import com.firstreps.backend.repository.UserRepository
import com.firstreps.backend.service.JwtService
import com.firstreps.backend.service.PasswordService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

data class SignupRequest(val firstName: String, val lastName: String, val email: String, val password: String, val role: Role)
data class LoginRequest(val email: String, val password: String)
data class AuthResponse(val accessToken: String, val firstName: String, val lastName: String, val userId: UUID, val role: Role)

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userRepository: UserRepository,
    private val coachRepository: CoachRepository,
    private val athleteRepository: AthleteRepository,
    private val passwordService: PasswordService,
    private val jwtService: JwtService
) {

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody body: SignupRequest): ResponseEntity<Any> {
        if (userRepository.findByEmail(body.email) != null) {
            return ResponseEntity.badRequest().body(mapOf("error" to "Email in use"))
        }
        val hashed = passwordService.hash(body.password)

        val user = User(
            firstName = body.firstName, lastName = body.lastName, role = body.role,
            email = body.email, passwordHash = hashed
        )
        if (body.role == Role.COACH) {
            val coach = Coach(user = user)
            coachRepository.save(coach)
        } else {
            val athlete = Athlete(user = user)
            athleteRepository.save(athlete)
        }
        val access = jwtService.generateToken(user.id.toString(), mapOf("role" to user.role))
        return ResponseEntity.ok(AuthResponse(access, user.firstName, user.lastName, user.id, user.role))
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody body: LoginRequest): ResponseEntity<Any> {
        val user = userRepository.findByEmail(body.email) ?: return ResponseEntity.status(401).body(mapOf("error" to "Invalid creds"))
        if (!passwordService.verify(user.passwordHash, body.password)) {
            return ResponseEntity.status(401).body(mapOf("error" to "Invalid creds"))
        }
        val access = jwtService.generateToken(user.id.toString(), mapOf("role" to user.role))
        // For demo we use access token as refresh too; in prod generate a secure refresh token and store it
        return ResponseEntity.ok(AuthResponse(access, user.firstName, user.lastName, user.id, user.role))
    }
}