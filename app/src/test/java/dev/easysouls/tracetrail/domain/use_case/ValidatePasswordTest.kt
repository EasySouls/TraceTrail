package dev.easysouls.tracetrail.domain.use_case

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidatePasswordTest {

    private lateinit var validatePassword: ValidatePassword

    @Before
    fun setUp() {
        validatePassword = ValidatePassword()
    }

    @Test
    fun `Password is letter-only, returns error`() {
        val result = validatePassword.execute("abcdefgh")

        assertThat(result.successful).isFalse()
    }

    @Test
    fun `Password has at least one letter and one digit, returns true`() {
        val result = validatePassword.execute("abcdefg3")

        assertThat(result.successful).isTrue()
    }
}