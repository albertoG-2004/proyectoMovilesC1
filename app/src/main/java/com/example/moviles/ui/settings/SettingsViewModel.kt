package com.example.moviles.ui.settings

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*

class SettingsViewModel(
    private val encryptedSharedPreferences: SharedPreferences,
    private val application: Application
) : ViewModel(), LocationListener {

    private lateinit var locationManager: LocationManager
    private var startTime: Long = 0
    private var totalUsageTime: Long = 0
    private var isFirstRun = true

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    private val _selectedLanguageIndex = MutableStateFlow(0)
    val selectedLanguageIndex: StateFlow<Int> = _selectedLanguageIndex

    private val _notificationVolume = MutableStateFlow(50)
    val notificationVolume: StateFlow<Int> = _notificationVolume

    private val _lastAccess = MutableStateFlow("Nunca")
    val lastAccess: StateFlow<String> = _lastAccess

    private val _lastLocation = MutableStateFlow("Desconocida")
    val lastLocation: StateFlow<String> = _lastLocation

    private val _totalUsageTimeText = MutableStateFlow("00:00:00")
    val totalUsageTimeText: StateFlow<String> = _totalUsageTimeText

    init {
        locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        startTime = System.currentTimeMillis()
        loadPreferences() // Load preferences when ViewModel is created
    }

    fun loadPreferences() {
        try {
            _userName.value = encryptedSharedPreferences.getString("userName", "") ?: ""
            _isDarkMode.value = encryptedSharedPreferences.getBoolean("darkMode", false)
            _selectedLanguageIndex.value = encryptedSharedPreferences.getInt("language", 0)
            _notificationVolume.value = encryptedSharedPreferences.getInt("volume", 50)
            _lastAccess.value = encryptedSharedPreferences.getString("lastAccess", "Nunca") ?: "Nunca"
            _lastLocation.value = encryptedSharedPreferences.getString("lastLocation", "Desconocida") ?: "Desconocida"

            totalUsageTime = encryptedSharedPreferences.getLong("totalUsageTime", 0)
            updateTotalUsageTimeDisplay()

            val currentDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val currentDate = currentDateFormat.format(Date())

            if (!isFirstRun) {
                val editor = encryptedSharedPreferences.edit()
                editor.putString("lastAccess", currentDate)
                editor.apply()
                _lastAccess.value = currentDate
            } else {
                isFirstRun = false
            }
        } catch (e: Exception) {
            Toast.makeText(
                application.applicationContext,
                "Error al cargar preferencias: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun savePreferences(context: Context) {
        try {
            val editor = encryptedSharedPreferences.edit()
            editor.putString("userName", _userName.value)
            editor.putBoolean("darkMode", _isDarkMode.value)
            editor.putInt("language", _selectedLanguageIndex.value)
            editor.putInt("volume", _notificationVolume.value)

            val currentDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val currentDate = currentDateFormat.format(Date())
            editor.putString("lastAccess", currentDate)
            _lastAccess.value = currentDate

            updateTotalUsageTime()
            editor.putLong("totalUsageTime", totalUsageTime)

            editor.apply()
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Error al guardar preferencias: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun updateUserName(name: String) {
        _userName.value = name
    }

    fun updateIsDarkMode(darkMode: Boolean) {
        _isDarkMode.value = darkMode
    }

    fun updateSelectedLanguageIndex(index: Int) {
        _selectedLanguageIndex.value = index
    }

    fun updateNotificationVolume(volume: Int) {
        _notificationVolume.value = volume
    }

    override fun onLocationChanged(location: Location) {
        val locationText = "Lat: ${location.latitude}, Long: ${location.longitude}"
        _lastLocation.value = locationText

        try {
            val editor = encryptedSharedPreferences.edit()
            editor.putString("lastLocation", locationText)
            editor.apply()
        } catch (e: Exception) {
            Toast.makeText(
                application.applicationContext,
                "Error al guardar ubicación: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}


    fun onPause() {
        updateTotalUsageTime()
        savePreferences(application.applicationContext)
    }

    fun onResume() {
        startTime = System.currentTimeMillis()
        loadPreferences()
    }

    private fun updateTotalUsageTime() {
        val currentTime = System.currentTimeMillis()
        val sessionTime = currentTime - startTime
        totalUsageTime += sessionTime
        startTime = currentTime
        updateTotalUsageTimeDisplay()
    }

    private fun updateTotalUsageTimeDisplay() {
        val seconds = (totalUsageTime / 1000) % 60
        val minutes = (totalUsageTime / (1000 * 60)) % 60
        val hours = (totalUsageTime / (1000 * 60 * 60))

        _totalUsageTimeText.value = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun requestLocationUpdates(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    5f,
                    this@SettingsViewModel
                )
            } catch (e: SecurityException) {
                Toast.makeText(context, "Error al obtener ubicación: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}