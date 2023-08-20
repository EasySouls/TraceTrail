package dev.easysouls.tracetrail

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import dev.easysouls.tracetrail.domain.missing_person.model.MissingPerson
import dev.easysouls.tracetrail.domain.services.NavigationService
import dev.easysouls.tracetrail.presentation.BottomNavigationBar
import dev.easysouls.tracetrail.presentation.CircularProgressBar
import dev.easysouls.tracetrail.presentation.CoarseLocationTextProvider
import dev.easysouls.tracetrail.presentation.FineLocationTextProvider
import dev.easysouls.tracetrail.presentation.PermissionDialog
import dev.easysouls.tracetrail.presentation.PostNotificationsTextProvider
import dev.easysouls.tracetrail.presentation.TopNavigationBar
import dev.easysouls.tracetrail.presentation.finder.FinderUI
import dev.easysouls.tracetrail.presentation.map.FinderViewModel
import dev.easysouls.tracetrail.presentation.map.MapScreen
import dev.easysouls.tracetrail.presentation.profile.ProfileScreen
import dev.easysouls.tracetrail.presentation.sign_in.FirebaseAuthManager
import dev.easysouls.tracetrail.presentation.sign_in.FirebaseAuthViewModel
import dev.easysouls.tracetrail.presentation.sign_in.LoginScreen
import dev.easysouls.tracetrail.presentation.sign_in.RegistrationScreen
import dev.easysouls.tracetrail.presentation.sign_in.StartScreen
import dev.easysouls.tracetrail.presentation.weather.WeatherCart
import dev.easysouls.tracetrail.presentation.weather.WeatherViewModel
import dev.easysouls.tracetrail.ui.theme.DarkBlue
import dev.easysouls.tracetrail.ui.theme.DeepBlue
import dev.easysouls.tracetrail.ui.theme.TraceTrailTheme
import kotlinx.coroutines.launch

// private const val MAPS_API_KEY = BuildConfig.MAPS_API_KEY

data class BottomNavigationItem(
    val title: String,
    val destScreen: Screen,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

@AndroidEntryPoint
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private val firebaseAuthManager: FirebaseAuthManager by lazy {
        FirebaseAuthManager(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TraceTrailTheme {
                val context = LocalContext.current

                val hasNotificationPermission by remember {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        mutableStateOf(
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        )
                    } else mutableStateOf(true)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.POST_NOTIFICATIONS,
                        ),
                        0
                    )
                }

                val navController = rememberNavController()
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(key1 = Unit) {
                    if (firebaseAuthManager.getSignedInUser() != null) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Loading.route) {
                                inclusive = true
                            }
                        }
                    } else {
                        navController.navigate(Screen.Auth.route) {
                            popUpTo(Screen.Loading.route) {
                                inclusive = true
                            }
                        }
                    }
                }

                // TODO: Make the badge reactive
                val navigationItems = remember {
                    listOf(
                        BottomNavigationItem(
                            title = "Home",
                            destScreen = Screen.Home,
                            selectedIcon = Icons.Filled.Home,
                            unselectedIcon = Icons.Outlined.Home,
                            hasNews = false
                        ),
                        BottomNavigationItem(
                            title = "Map",
                            destScreen = Screen.Map,
                            selectedIcon = Icons.Filled.LocationOn,
                            unselectedIcon = Icons.Outlined.LocationOn,
                            hasNews = true
                        ),
                        BottomNavigationItem(
                            title = "Profile",
                            destScreen = Screen.Profile,
                            selectedIcon = Icons.Filled.AccountCircle,
                            unselectedIcon = Icons.Outlined.AccountCircle,
                            hasNews = false,
                            badgeCount = 4
                        ),
                        BottomNavigationItem(
                            title = "Settings",
                            destScreen = Screen.Settings,
                            selectedIcon = Icons.Filled.Settings,
                            unselectedIcon = Icons.Outlined.Settings,
                            hasNews = true,
                            badgeCount = 23
                        )
                    )
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        TopNavigationBar(
                            navController = navController,
                            scrollBehavior = scrollBehavior
                        )
                    },
                    bottomBar = {
                        BottomNavigationBar(
                            navController = navController, navItems = navigationItems
                        )
                    },
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Loading.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        //Only for displaying the loading screen while the app loads
                        composable(Screen.Loading.route) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                CircularProgressBar(
                                    percentage = 1f,
                                    maxNumber = 100
                                )
                            }
                        }

                        navigation(
                            startDestination = "start_screen",
                            route = Screen.Auth.route
                        ) {
                            composable("start_screen") {
                                val viewModel =
                                    it.sharedViewModel<FirebaseAuthViewModel>(navController)
                                val state by viewModel.signInState.collectAsStateWithLifecycle()

                                val launcher = rememberLauncherForActivityResult(
                                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                                    onResult = { result ->
                                        if (result.resultCode == RESULT_OK) {
                                            lifecycleScope.launch {
                                                val signInResult =
                                                    firebaseAuthManager.signInWithIntent(
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

                                        navController.navigate(Screen.Main.route) {
                                            popUpTo(Screen.Auth.route) {
                                                inclusive = true
                                            }
                                        }
                                        viewModel.resetState()
                                    }
                                }

                                StartScreen(
                                    state = state,
                                    signInWithEmailAndPassword = { navController.navigate("register") },
                                    loginWithEmailAndPassword = { navController.navigate("login") },
                                    signInWithGoogle = {
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
                                val viewModel =
                                    it.sharedViewModel<FirebaseAuthViewModel>(navController)
                                val signInState by viewModel.signInState.collectAsStateWithLifecycle()

                                LaunchedEffect(key1 = signInState.isSignInSuccessful) {
                                    if (signInState.isSignInSuccessful) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Sign in successful",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        navController.navigate(Screen.Main.route) {
                                            popUpTo(Screen.Auth.route) {
                                                inclusive = true
                                            }
                                        }
                                        viewModel.resetState()
                                    }
                                }

                                RegistrationScreen(
                                    viewModel = viewModel,
                                    signInState = signInState,
                                    registerUser = {
                                        lifecycleScope.launch {
                                            val signInResult =
                                                firebaseAuthManager.createAccountWithEmailAndPassword(
                                                    viewModel.registrationFormState.email,
                                                    viewModel.registrationFormState.password
                                                )
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                )
                            }
                            composable("login") {
                                val viewModel =
                                    it.sharedViewModel<FirebaseAuthViewModel>(navController)
                                val signInState by viewModel.signInState.collectAsStateWithLifecycle()

                                LaunchedEffect(key1 = signInState.isSignInSuccessful) {
                                    if (signInState.isSignInSuccessful) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Login successful",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(Screen.Auth.route) {
                                                inclusive = true
                                            }
                                        }
                                        viewModel.resetState()
                                    }
                                }

                                LoginScreen(
                                    viewModel = viewModel,
                                    signInState = signInState,
                                    loginUser = {
                                        lifecycleScope.launch {
                                            val signInResult =
                                                firebaseAuthManager.signInWithEmailAndPassword(
                                                    viewModel.loginFormState.email,
                                                    viewModel.loginFormState.password
                                                )
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                )
                            }
                        }

                        navigation(
                            startDestination = "service_test",
                            route = Screen.Home.route
                        ) {
                            composable("missing_persons") {
                                val missingPersons by remember {
                                    mutableStateOf(listOf<MissingPerson>())
                                }
                                FinderUI(missingPersons)
                            }

                            composable("map") {
                                val viewModel = it.sharedViewModel<FinderViewModel>(navController)
                                val dialogQueue = viewModel.visiblePermissionDialogQueue

                                val multiplePermissionResultLauncher =
                                    rememberLauncherForActivityResult(
                                        contract = ActivityResultContracts.RequestMultiplePermissions(),
                                        onResult = { perms ->
                                            perms.keys.forEach { permission ->
                                                viewModel.onPermissionResult(
                                                    permission = permission,
                                                    isGranted = perms[permission] == true
                                                )
                                            }
                                        }
                                    )

                                LaunchedEffect(key1 = multiplePermissionResultLauncher) {
                                    multiplePermissionResultLauncher.launch(permissionsToRequest)
                                }

                                MapScreen(
                                    currentLocationState = viewModel.currentLocationState,
                                    mapState = viewModel.mapState,
                                    onEvent = viewModel::onEvent
                                )

                                dialogQueue
                                    .reversed()
                                    .forEach { permission ->
                                        PermissionDialog(
                                            permissionTextProvider = when (permission) {
                                                Manifest.permission.ACCESS_COARSE_LOCATION -> CoarseLocationTextProvider()
                                                Manifest.permission.ACCESS_FINE_LOCATION -> FineLocationTextProvider()
                                                Manifest.permission.POST_NOTIFICATIONS -> PostNotificationsTextProvider()
                                                else -> return@forEach
                                            },
                                            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                                                permission
                                            ),
                                            onDismiss = viewModel::dismissDialog,
                                            onOkayClick = {
                                                viewModel.dismissDialog()
                                                multiplePermissionResultLauncher.launch(
                                                    arrayOf(permission)
                                                )
                                            },
                                            onGoToAppSettingsClick = { openAppSettings() }
                                        )
                                    }
                            }

                            composable("service_test") {
                                val viewModel = it.sharedViewModel<FinderViewModel>(navController)

                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Button(onClick = {
                                        Intent(
                                            applicationContext,
                                            NavigationService::class.java
                                        ).also { intent ->
                                            intent.action = NavigationService.Actions.START.toString()
                                            startService(intent)
                                        }
                                    }) {
                                        Text("Start service")
                                    }
                                    Button(onClick = {
                                        Intent(
                                            applicationContext,
                                            NavigationService::class.java
                                        ).also {intent ->
                                            intent.action = NavigationService.Actions.STOP.toString()
                                            startService(intent)
                                        }
                                    }) {
                                        Text("Stop service")
                                    }
                                }
                            }
                        }

                        composable(Screen.Profile.route) {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.background
                            ) {
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

                                            navController.navigate(Screen.Auth.route) {
                                                popUpTo(Screen.Profile.route) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    },
                                    onNavigateToFinderUI = {
                                        navController.navigate(Screen.Home.route)
                                    }
                                )
                            }
                        }

                        navigation(
                            route = "weather",
                            startDestination = "weather_screen"
                        ) {
                            composable("weather_screen") {
                                val viewModel: WeatherViewModel = hiltViewModel()
                                val permissionLauncher = registerForActivityResult(
                                    ActivityResultContracts.RequestMultiplePermissions()
                                ) {
                                    viewModel.loadWeatherInfo()
                                }
                                permissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                    )
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(DarkBlue)
                                ) {
                                    WeatherCart(state = viewModel.state, backgroundColor = DeepBlue)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    data object Auth : Screen("auth", R.string.auth_nav_resource)
    data object Main : Screen("auth", R.string.main_nav_resource)
    data object Home : Screen("home", R.string.home_nav_resource)
    data object Map : Screen("map", R.string.map_nav_resource)
    data object Profile : Screen("profile", R.string.profile_nav_resource)
    data object Loading : Screen("loading", R.string.loading_nav_resource)
    data object Settings : Screen("settings", R.string.settings_nav_resource)
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

