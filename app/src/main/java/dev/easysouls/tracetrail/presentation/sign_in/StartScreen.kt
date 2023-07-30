package dev.easysouls.tracetrail.presentation.sign_in

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.easysouls.tracetrail.R

@Composable
fun StartScreen(
    signInWithEmailAndPassword: () -> Unit,
    signInWithGoogle: () -> Unit
) {
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
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary)
            ) {
                Text(text = "Sign in with email")
                Icon(painter = painterResource(R.drawable.ic_profile), contentDescription = "Sign in with email icon")
            }
            Button(
                onClick = signInWithGoogle,
                shape = RoundedCornerShape(2.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondary)
            ) {
                Text(text = "Sign in with Google")
                Icon(painter = painterResource(R.drawable.ic_profile), contentDescription = "Sign in with Google icon")
            }
        }
    }
}