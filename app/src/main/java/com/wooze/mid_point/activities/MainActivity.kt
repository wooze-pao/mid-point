package com.wooze.mid_point.activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wooze.mid_point.R
import com.wooze.mid_point.data.NavRouter
import com.wooze.mid_point.state.UiState
import com.wooze.mid_point.tools.FloatWindowAction.closeFloatActivity
import com.wooze.mid_point.tools.FloatWindowAction.openFloatActivity
import com.wooze.mid_point.ui.SettingPage
import com.wooze.mid_point.ui.homeScreenUi.HomeScreen
import com.wooze.mid_point.ui.theme.MidPointTheme
import com.wooze.mid_point.viewModel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.initData(this@MainActivity)
        }

        Log.d("TaskTest", "MainActivity taskId = $taskId")
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            MidPointTheme(dynamicColor = true) {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomBar(navController) },
                    topBar = {
                        TopAppBar(
                            { Text(stringResource(R.string.top_bar)) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                MaterialTheme.colorScheme.inverseOnSurface
                            )
                        )
                    }
                ) { paddingValues ->
                    NavHost(navController, startDestination = NavRouter.HOME) {
                        composable(NavRouter.HOME) {
                            HomeScreen(
                                { openFloatActivity(context) },
                                { closeFloatActivity() },
                                viewModel,
                                paddingValues
                            )
                        }
                        composable(NavRouter.SETTING) {
                            SettingPage(viewModel)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkOverlayPermission(this)
        UiState.isShowing.value = FloatActivity.check()
    }

}

data class NavItem(val name: Int, val router: String, val icon: ImageVector)

val navItems = arrayOf(
    NavItem(R.string.bottom_bar_home, NavRouter.HOME, Icons.Filled.Home),
    NavItem(R.string.bottom_bar_setting, NavRouter.SETTING, Icons.Filled.Settings)
)

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    BottomAppBar {
        navItems.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.route == item.router,
                onClick = {
                    navController.navigate(item.router) {
                        popUpTo(NavRouter.HOME) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = stringResource(item.name)) },
                label = { Text(stringResource(item.name)) }
            )
        }
    }
}