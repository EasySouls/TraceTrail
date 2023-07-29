package dev.easysouls.tracetrail.domain.use_case

class ValidateTerms {
    fun execute(accepted: Boolean): ValidationResult {
        if (!accepted) {
            return ValidationResult(
                successful = false,
                errorMessage = "Please accept the terms"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}