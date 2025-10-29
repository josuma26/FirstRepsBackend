package com.firstreps.backend.service

import de.mkammerer.argon2.Argon2Factory
import org.springframework.stereotype.Service

@Service
class PasswordService {
    private val argon2 = Argon2Factory.create()
    fun hash(password: String): String = argon2.hash(2, 65536, 1, password.toCharArray())
    fun verify(hash: String, password: String): Boolean = argon2.verify(hash, password.toCharArray())
}