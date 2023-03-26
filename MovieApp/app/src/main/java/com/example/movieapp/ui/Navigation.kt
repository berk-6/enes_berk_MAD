import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movieapp.HomeScreen
import com.example.movieapp.MessageList
import com.example.movieapp.ui.Screen

@Composable
fun NavigationCtrl() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(Screen.MainScreen.route) { HomeScreen() }
        composable(
            route = Screen.DetailScreen.route + "/{name}",
            arguments =
            listOf(
                navArgument("name") {
                    type = NavType.StringType
                    defaultValue = "Thomas"
                    nullable = true
                }
            )
        ) { entry ->
            DetailScreen(name = entry.arguments?.getString("name"), navController)
        }
    }
}

@Composable
fun DetailScreen(name: String?, navController: NavHostController) {
    MessageList()
}
