package dev.easysouls.tracetrail.domain.use_case

import android.content.Context
import android.util.Patterns
import dev.easysouls.tracetrail.R

class ValidateEmail {
    
    // TODO: Add regex validation
    fun execute(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The email can't be blank"
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "That's not a valid email"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}