package com.example.moviles.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.moviles.ui.settings.SettingsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

// Definimos colores personalizados con tonos de amarillo y negro
private val ProjectYellow = Color(0xFFFFEB3B) // Amarillo primario (más suave que el amarillo puro)
private val ProjectYellowSecondary = Color(0xFFFDD835) // Amarillo secundario (ligeramente más oscuro)
private val ProjectBackgroundLight = Color(0xFFFAFAFA) // Fondo muy claro, casi blanco para modo claro
private val ProjectSurfaceLight = Color(0xFFFFFFFF) // Superficie blanca para modo claro
private val ProjectOnYellow = Color(0xFF212121) // Texto oscuro sobre amarillo (casi negro)
private val ProjectBackgroundDark = Color(0xFF212121) // Fondo oscuro (casi negro) para modo oscuro
private val ProjectSurfaceDark = Color(0xFF424242) // Superficie oscura ligeramente más clara
private val ProjectAccentYellow = Color(0xFFFFC107) // Amarillo acento (amarillo oro para resaltar)
private val ProjectGradientStart = ProjectYellowSecondary // Inicio del gradiente amarillo
private val ProjectGradientEnd = Color(0xFFFFECB3) // Fin del gradiente amarillo (más claro)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserConfigScreen(settingsViewModel: SettingsViewModel, onBack: () -> Unit) { // Receive SettingsViewModel and onBack
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val languages = listOf("Español", "English", "Portugues", "Français", "Chinese", "Deutsch", "Italiano")

    val userNameState by settingsViewModel.userName.collectAsState()
    val darkModeState by settingsViewModel.isDarkMode.collectAsState()
    val selectedLanguageIndex by settingsViewModel.selectedLanguageIndex.collectAsState()
    val notificationVolume by settingsViewModel.notificationVolume.collectAsState()
    val lastAccess by settingsViewModel.lastAccess.collectAsState()
    val lastLocation by settingsViewModel.lastLocation.collectAsState()
    val totalUsageTimeText by settingsViewModel.totalUsageTimeText.collectAsState()


    var expandedDropdown by remember { mutableStateOf(false) }
    var volumeState by remember { mutableFloatStateOf(notificationVolume.toFloat()) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            settingsViewModel.requestLocationUpdates(context)
        } else {
            Toast.makeText(
                context,
                "Permiso de ubicación denegado",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            settingsViewModel.requestLocationUpdates(context)
        }
        settingsViewModel.loadPreferences()
    }

    val colorScheme = if (darkModeState) {
        darkColorScheme(
            primary = ProjectYellow,
            secondary = ProjectYellowSecondary,
            tertiary = ProjectAccentYellow,
            background = ProjectBackgroundDark,
            surface = ProjectSurfaceDark,
            onPrimary = ProjectOnYellow
        )
    } else {
        lightColorScheme(
            primary = ProjectYellow,
            secondary = ProjectYellowSecondary,
            tertiary = ProjectAccentYellow,
            background = ProjectBackgroundLight,
            surface = ProjectSurfaceLight,
            onPrimary = ProjectOnYellow
        )
    }

    MaterialTheme(colorScheme = colorScheme) { // Apply MaterialTheme with colorScheme here
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header con diseño moderno de gradiente
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(ProjectGradientStart, ProjectGradientEnd)
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            // Icono de perfil aquí si lo deseas
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = "Mi perfil",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = ProjectOnYellow // Texto oscuro para contrastar con el amarillo
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Configuraciones y estadísticas",
                                fontSize = 16.sp,
                                color = ProjectOnYellow.copy(alpha = 0.8f) // Texto oscuro con transparencia
                            )
                        }
                    }
                }

                // Sección de usuario con diseño moderno
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AccountCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary, // Usa el amarillo primario
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Caracteristicas de usuario",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary // Usa el amarillo primario
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        OutlinedTextField(
                            value = userNameState,
                            onValueChange = { settingsViewModel.updateUserName(it) },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Usuario") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = "Usuario",
                                    tint = MaterialTheme.colorScheme.primary // Usa el amarillo primario
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary, // Usa el amarillo primario
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedLabelColor = MaterialTheme.colorScheme.primary // Usa el amarillo primario
                            )
                        )
                    }
                }

                // Sección de preferencias con nuevo diseño
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary, // Usa el amarillo primario
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Configuración",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary // Usa el amarillo primario
                            )
                        }

                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                        // Tema oscuro
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "Tema oscuro",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface // Texto en superficie
                                )
                            }
                            Switch(
                                checked = darkModeState,
                                onCheckedChange = {
                                    settingsViewModel.updateIsDarkMode(it)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary, // Texto sobre primario (amarillo)
                                    checkedTrackColor = MaterialTheme.colorScheme.primary, // Amarillo primario
                                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            )
                        }

                        // Idioma
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "Idioma",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface // Texto en superficie
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            ExposedDropdownMenuBox(
                                expanded = expandedDropdown,
                                onExpandedChange = { expandedDropdown = it },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = languages[selectedLanguageIndex],
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = expandedDropdown
                                        )
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MaterialTheme.colorScheme.primary, // Amarillo primario
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                    )
                                )

                                ExposedDropdownMenu(
                                    expanded = expandedDropdown,
                                    onDismissRequest = { expandedDropdown = false },
                                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                                ) {
                                    languages.forEachIndexed { index, language ->
                                        DropdownMenuItem(
                                            text = { Text(language, color = MaterialTheme.colorScheme.onSurface) }, // Texto en superficie
                                            onClick = {
                                                settingsViewModel.updateSelectedLanguageIndex(index)
                                                expandedDropdown = false
                                            },
                                            leadingIcon = {
                                                if (index == selectedLanguageIndex) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.primary // Amarillo primario
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // Volumen
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "Volumen de notificaciones",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface // Texto en superficie
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "${volumeState.toInt()}%",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary // Amarillo primario
                                )
                            }
                            Slider(
                                value = volumeState,
                                onValueChange = {
                                    volumeState = it
                                    settingsViewModel.updateNotificationVolume(it.toInt())
                                },
                                valueRange = 0f..100f,
                                steps = 100,
                                modifier = Modifier.fillMaxWidth(),
                                colors = SliderDefaults.colors(
                                    thumbColor = MaterialTheme.colorScheme.primary, // Amarillo primario
                                    activeTrackColor = MaterialTheme.colorScheme.primary, // Amarillo primario
                                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            )
                        }
                    }
                }

                // Estadísticas con nuevo diseño
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Análisis de Actividad",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary // Usa el amarillo primario
                            )
                        }

                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                        // Último acceso
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Último acceso",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant // Texto en variante de superficie
                                )
                                Text(
                                    text = lastAccess,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface // Texto en superficie
                                )
                            }
                        }

                        // Última ubicación
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = "Última ubicación",
                                tint = MaterialTheme.colorScheme.primary, // Usa el amarillo primario
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Última ubicación",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant // Texto en variante de superficie
                                )
                                Text(
                                    text = lastLocation,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface // Texto en superficie
                                )
                            }
                        }

                        // Tiempo total
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Tiempo total de uso",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant // Texto en variante de superficie
                                )
                                Text(
                                    text = totalUsageTimeText,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface // Texto en superficie
                                )
                            }
                        }
                    }
                }

                // Botón de guardar con diseño flotante
                Button(
                    onClick = {
                        settingsViewModel.savePreferences(context)
                        Toast.makeText(
                            context,
                            "Configuración guardada con éxito",
                            Toast.LENGTH_SHORT
                        ).show()
                        onBack() // Navigate back after saving
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary, // Usa el amarillo primario
                        contentColor = MaterialTheme.colorScheme.onPrimary // Texto sobre primario
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Guardar Cambios",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }

                // Espacio al final para evitar que el botón flotante tape contenido
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}