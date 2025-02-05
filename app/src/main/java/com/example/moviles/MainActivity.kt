package com.example.moviles

import RegisterScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviles.ui.Products.ui.AddProductScreen
import com.example.moviles.ui.Products.ui.EditDeleteProductScreen
import com.example.moviles.ui.Products.ui.EditSingleProductScreen
import com.example.moviles.ui.Products.ui.ListProductsScreen
import com.example.moviles.ui.login.ui.LoginScreen
import com.example.moviles.ui.theme.MovilesTheme
import com.example.moviles.ui.Home.ui.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovilesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login_screen") {
                        composable("login_screen") {
                            LoginScreen(navController = navController)
                        }
                        composable("register_screen"){
                            RegisterScreen(navigateToLogin = {
                                navController.navigate("login_screen")
                            })
                        }
                        composable("home_screen") {
                            HomeScreen(navController = navController)
                        }
                        composable("add_product_screen") {
                            AddProductScreen(navController = navController)
                        }
                        composable("list_products_screen") {
                            ListProductsScreen(navController = navController)
                        }
                        composable("edit_delete_product_screen") {
                            EditDeleteProductScreen(navController = navController)
                        }
                        composable(
                            route = "edit_single_product_screen/{productId}",
                            arguments = listOf(navArgument("productId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            EditSingleProductScreen(navController = navController, productId = backStackEntry.arguments?.getString("productId") )
                        }
                    }
                }
            }
        }
    }
}