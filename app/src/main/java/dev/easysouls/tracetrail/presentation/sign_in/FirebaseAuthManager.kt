package dev.easysouls.tracetrail.presentation.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dev.easysouls.tracetrail.R
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import kotlin.random.Random

class FirebaseAuthManager(
    private val oneTapClient: SignInClient,
    private val context: Context
) {
    private val auth = FirebaseAuth.getInstance()

    suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResult {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user
            user?.let {
                Log.d(TAG, "User logged in - Email: ${it.email}, Username: ${it.displayName}")
            }

            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = null
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.localizedMessage
            )
        }
    }

    // I think this logs the user instantly in if successful
    suspend fun createAccountWithEmailAndPassword(email: String, password: String): SignInResult {
        return try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user
            user?.let {
                Log.d(TAG, "New user account - Email: ${it.email}, Username: ${it.displayName}")
            }

            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = null
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.localizedMessage
            )
        }
    }

    // The basic UID is 8 characters long for anonymous users
    private fun getRandomUserId(length: Int = 8): String {
        val characters = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        val random = Random(System.currentTimeMillis())

        return (1..length)
            .map { characters[random.nextInt(characters.size)] }
            .joinToString("")
    }

    suspend fun signInAnonymously(): SignInResult {
        return try {
            val user = auth.signInAnonymously().await().user
            user?.let {
                Log.d(TAG, "User signed in anonymously - Email: ${it.email}, Username: ${it.displayName}")
            }

            SignInResult(
                data = user.run {
                    UserData(
                        userId = getRandomUserId(),
                        username = "anonymous",
                        profilePictureUrl = null
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.localizedMessage
            )
        }
    }

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

     suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            user?.let {
                Log.d(TAG, "User signed in with Google - Email: ${it.email}, Username: ${it.displayName}")
            }

            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl.toString()
        )
    }

    // TODO: If there is no logged on Google Account on the phone, it throws an api exception
    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.resources.getString(R.string.web_client_id))
                    .build()
            )
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()
    }

    companion object {
        private const val TAG = "FirebaseAuthManager"
    }
}