package dev.easysouls.tracetrail.domain.use_case

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidateEmailTest {

    private lateinit var validateEmail: ValidateEmail

    @Before
    fun setUp() {
        validateEmail = ValidateEmail()
    }

    @Test
    fun `Blank email returns false`() {
        val result = validateEmail.execute("")

        assertThat(result.successful).isFalse()
    }
}