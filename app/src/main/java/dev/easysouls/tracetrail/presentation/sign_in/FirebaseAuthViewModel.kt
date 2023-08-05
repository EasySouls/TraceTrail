package dev.easysouls.tracetrail.presentation.sign_in

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.easysouls.tracetrail.domain.use_case.ValidateEmail
import dev.easysouls.tracetrail.domain.use_case.ValidatePassword
import dev.easysouls.tracetrail.domain.use_case.ValidateRepeatedPassword
import dev.easysouls.tracetrail.domain.use_case.ValidateTerms
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FirebaseAuthViewModel(
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateRepeatedPassword: ValidateRepeatedPassword = ValidateRepeatedPassword(),
    private val validateTerms: ValidateTerms = ValidateTerms()
): ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val signInState = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    var loginFormState by mutableStateOf(LoginFormState())

    var registrationFormState by mutableStateOf(RegistrationFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: RegistrationFormEvent) {
        when (event) {
            is RegistrationFormEvent.EmailChanged -> {
                registrationFormState = registrationFormState.copy(email = event.email)
            }
            is RegistrationFormEvent.PasswordChanged -> {
                registrationFormState = registrationFormState.copy(password = event.password)
            }
            is RegistrationFormEvent.RepeatedPasswordChanged -> {
                registrationFormState = registrationFormState.copy(repeatedPassword = event.repeatedPassword)
            }
            is RegistrationFormEvent.AcceptTerms -> {
                registrationFormState = registrationFormState.copy(acceptedTerms = event.isAccepted)
            }
            is RegistrationFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmail.execute(registrationFormState.email)
        val passwordResult = validatePassword.execute(registrationFormState.password)
        val repeatedPasswordResult = validateRepeatedPassword.execute(
            registrationFormState.password, registrationFormState.repeatedPassword
        )
        val termsResult = validateTerms.execute(registrationFormState.acceptedTerms)

        val hasError = listOf(
            emailResult,
            passwordResult,
            repeatedPasswordResult,
            termsResult
        ).any { !it.successful}

        if (hasError) {
            registrationFormState = registrationFormState.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                repeatedPasswordError = repeatedPasswordResult.errorMessage,
                termsError = termsResult.errorMessage,
            )
            return
        }
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    sealed class ValidationEvent {
        data object Success: ValidationEvent()
    }
}