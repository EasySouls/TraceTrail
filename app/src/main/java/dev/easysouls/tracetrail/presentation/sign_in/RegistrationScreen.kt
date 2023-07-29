package dev.easysouls.tracetrail.presentation.sign_in

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date

@Composable
fun RegistrationScreen(viewModel: RegistrationViewModel) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when(event) {
                is RegistrationViewModel.ValidationEvent.Success -> {
                    // When validation was successful
                    Toast.makeText(
                        context,
                        "Registration successful",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Email
        TextField(
            value = state.email,
            onValueChange = {
                viewModel.onEvent(RegistrationFormEvent.EmailChanged(it))
            },
            isError = state.emailError != null,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Email")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )
        if (state.emailError != null) {
            Text(
                text = state.emailError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Password
        TextField(
            value = state.password,
            onValueChange = {
                viewModel.onEvent(RegistrationFormEvent.PasswordChanged(it))
            },
            isError = state.passwordError != null,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Password")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation()
        )
        if (state.passwordError != null) {
            Text(
                text = state.passwordError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Repeated password
        TextField(
            value = state.repeatedPassword,
            onValueChange = {
                viewModel.onEvent(RegistrationFormEvent.RepeatedPasswordChanged(it))
            },
            isError = state.repeatedPasswordError != null,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Repeat password")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation()
        )
        if (state.repeatedPasswordError != null) {
            Text(
                text = state.repeatedPasswordError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Terms
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = state.acceptedTerms,
                onCheckedChange = {
                    viewModel.onEvent(RegistrationFormEvent.AcceptTerms(it))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Accept Terms and Conditions")
        }
        if (state.termsError != null) {
            Text(
                text = state.termsError,
                color = MaterialTheme.colorScheme.error
            )
        }
        Button(onClick = {
            viewModel.onEvent(RegistrationFormEvent.Submit)
        },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Submit")
        }
    }
}