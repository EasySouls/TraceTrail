package dev.easysouls.tracetrail.presentation.sign_in

import java.util.Date

data class Account(
    val email: String = "",
    val password: String = "",
    val dateOfCreation: Date = Date()
)
