package dev.easysouls.tracetrail.presentation.sign_in

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StartScreen(
    state: SignInState,
    signInWithEmailAndPassword: () -> Unit,
    loginWithEmailAndPassword: () -> Unit,
    signInWithGoogle: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Button(
                onClick = signInWithEmailAndPassword,
                shape = RoundedCornerShape(2.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(4.dp)
            ) {
                Text(text = "Sign in with email")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(imageVector = Icons.Default.Email, contentDescription = "Sign in with email")
            }
            Button(
                onClick = loginWithEmailAndPassword,
                shape = RoundedCornerShape(2.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(4.dp)
            ) {
                Text(text = "Login")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Log in with email")
            }
            ElevatedButton(
                onClick = signInWithGoogle,
                shape = RoundedCornerShape(2.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondary),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(4.dp)
            ) {
                Text(text = "Sign in with Google")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(imageVector = Icons.Default.Place, contentDescription = "Sign in with Google")
            }
        }
    }
}

@Preview
@Composable
fun StartScreenPreview() {
    StartScreen(
        state = SignInState(
            isSignInSuccessful = false,
            signInError = null
        ),
        signInWithEmailAndPassword = {},
        loginWithEmailAndPassword = {},
        signInWithGoogle = {}
    )
}