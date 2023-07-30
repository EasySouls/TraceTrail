package dev.easysouls.tracetrail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseUser
import dev.easysouls.tracetrail.data.MissingPerson
import dev.easysouls.tracetrail.presentation.profile.ProfileScreen
import dev.easysouls.tracetrail.presentation.sign_in.FirebaseAuthManager
import dev.easysouls.tracetrail.presentation.sign_in.GoogleAuthViewModel
import dev.easysouls.tracetrail.presentation.sign_in.RegistrationScreen
import dev.easysouls.tracetrail.presentation.sign_in.RegistrationViewModel
import dev.easysouls.tracetrail.presentation.sign_in.SignInScreen
import dev.easysouls.tracetrail.presentation.sign_in.StartScreen
import dev.easysouls.tracetrail.ui.finder.FinderUI
import dev.easysouls.tracetrail.ui.theme.TraceTrailTheme
import kotlinx.coroutines.launch

private const val MAPS_API_KEY = BuildConfig.MAPS_API_KEY

class MainActivity : ComponentActivity() {

    /*private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }*/

    private val firebaseAuthManager: FirebaseAuthManager by lazy {
        FirebaseAuthManager(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TraceTrailTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "auth") {
                    navigation(
                        startDestination = "start_screen",
                        route = "auth"
                    ) {
                        composable("start_screen") {
                            StartScreen(
                                signInWithEmailAndPassword = { navController.navigate("register") },
                                signInWithGoogle = { navController.navigate("login") }
                            )
                        }
                        composable("login") {
                            val viewModel = it.sharedViewModel<GoogleAuthViewModel>(navController)
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            LaunchedEffect(key1 = Unit) {
                                if (firebaseAuthManager.getSignedInUser() != null) {
                                    navController.navigate("main") {
                                        popUpTo("auth") {
                                            inclusive = true
                                        }
                                    }
                                }
                            }

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = firebaseAuthManager.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )

                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if (state.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate("main") {
                                        popUpTo("auth") {
                                            inclusive = true
                                        }
                                    }
                                    viewModel.resetState()
                                }
                            }

                            SignInScreen(
                                state = state,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = firebaseAuthManager.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                }
                            )
                        }
                        composable("register") {
                            val viewModel = viewModel<RegistrationViewModel>()

                            RegistrationScreen(viewModel)
                        }
                        composable("forgot_password") {
                            val viewModel = it.sharedViewModel<GoogleAuthViewModel>(navController)
                        }
                    }
                    navigation(
                        startDestination = "profile",
                        route = "main"
                    ) {
                        composable("profile") {

                            ProfileScreen(
                                userData = firebaseAuthManager.getSignedInUser(),
                                onSignOut = {
                                    lifecycleScope.launch {
                                        firebaseAuthManager.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed Out",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navController.navigate("auth") {
                                            popUpTo("main") {
                                                inclusive = true
                                            }
                                        }
                                    }
                                },
                                onNavigateToFinderUI = {
                                    navController.navigate("missing_persons")
                                }
                            )
                        }

                        composable("missing_persons") {
                            val person1 = MissingPerson("James", "", "", "", "")
                            val person2 = MissingPerson("James", "", "", "", "")
                            val person3 = MissingPerson("James", "", "", "", "")
                            val person4 = MissingPerson("James", "", "", "", "")
                            var missingPersons by remember {
                                mutableStateOf(listOf(person1, person2, person3, person4))
                            }
                            FinderUI(missingPersons)
                        }
                    }
                }
            }
        }
    }
}


@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}
