package su.pank.solver.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import su.pank.solver.ui.main.MainScreen
import su.pank.solver.ui.main.Main

@Composable
fun GeneralNavigation(){
    val navController = rememberNavController()

    NavHost(navController, Main){
        composable<Main>{
            MainScreen()
        }
    }
}