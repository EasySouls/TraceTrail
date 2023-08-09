package dev.easysouls.tracetrail.presentation

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import dev.easysouls.tracetrail.MainActivity
import dev.easysouls.tracetrail.R
import dev.easysouls.tracetrail.Screen

@SuppressLint("ResourceType")
@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    navItems: List<Screen>,
    modifier: Modifier = Modifier
    ) {
    BottomAppBar(
        modifier = modifier
    ) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        navItems.forEach { screen ->
            val isSelected = currentRoute == screen.route
            IconButton(
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                content = {
                    Icon(
                        // TODO: make this screen.resourceId
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = screen.route,
                        tint = if (isSelected) Color.White else Color.Gray
                    )
                }
            )
        }
    }
}