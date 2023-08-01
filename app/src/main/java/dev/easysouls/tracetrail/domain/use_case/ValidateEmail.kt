package dev.easysouls.tracetrail.domain.use_case

import android.util.Patterns

class ValidateEmail {
    
    // TODO: Add regex validation and check whether this email has been used before
    // Maybe don't have to check whether it's been used before
    // I think Firebase throws an error, and I can catch it and display an error message based on that
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