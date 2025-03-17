package com.example.moviles.ui.Home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.moviles.ui.settings.SettingsViewModel

@Composable
fun HomeScreen(navController: NavHostController, settingsViewModel: SettingsViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { navController.navigate("add_product_screen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow, contentColor = Color.Black), // Changed to Yellow
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Agregar Producto",
                    tint = Color.Black // Changed to Black
                )
                Spacer(Modifier.padding(4.dp))
                Text(
                    text = "Agregar producto",
                    color = Color.Black, // Changed to Black
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Button(
                onClick = { navController.navigate("list_products_screen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow, contentColor = Color.Black), // Changed to Yellow
            ) {
                Icon(
                    imageVector = Icons.Filled.List,
                    contentDescription = "Listar Productos",
                    tint = Color.Black // Changed to Black
                )
                Spacer(Modifier.padding(4.dp))
                Text(
                    text = "Ver productos",
                    color = Color.Black, // Changed to Black
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Button(
                onClick = { navController.navigate("edit_delete_product_screen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow, contentColor = Color.Black), // Changed to Yellow
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Editar producto",
                    tint = Color.Black // Changed to Black
                )
                Spacer(Modifier.padding(4.dp))
                Text(
                    text = "Editar producto",
                    color = Color.Black, // Changed to Black
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Button(
                onClick = {
                    navController.navigate("settings_screen") // Navigate to settings screen
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp) // Reduced height for settings button to fit
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow, contentColor = Color.Black), // Changed to Yellow
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings, // Use a settings-like icon
                    contentDescription = "Configuraciones",
                    tint = Color.Black // Changed to Black
                )
                Spacer(Modifier.padding(4.dp))
                Text(
                    text = "Configuraciones",
                    color = Color.Black, // Changed to Black
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Button(
                onClick = {
                    navController.navigate("login_screen") {
                        popUpTo("login_screen") {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow, contentColor = Color.Black), // Changed to Yellow
            ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Salir",
                    tint = Color.Black // Changed to Black
                )
                Spacer(Modifier.padding(4.dp))
                Text(
                    text = "Salir",
                    color = Color.Black, // Changed to Black
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController()
    HomeScreen(
        navController = navController,
        settingsViewModel = TODO()
    )
}