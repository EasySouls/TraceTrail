package dev.easysouls.tracetrail.domain.use_case

class ValidatePassword {
    fun execute(password: String): ValidationResult {
        if (password.length < MIN_REQUIRED_CHARS) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to consists of at least 6 characters"
            )
        }
        val containsLettersAndDigits = password.any { it.isDigit() } &&
                password.any { it.isLetter() }
        if (!containsLettersAndDigits) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to contain at least one digit and one letter"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    companion object {
        private const val MIN_REQUIRED_CHARS = 6
    }
}