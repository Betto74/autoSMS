package com.example.autosms

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autosms.ui.theme.AutoSMSTheme
import androidx.core.content.ContextCompat
import androidx.activity.result.ActivityResultLauncher

class MainActivity : ComponentActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted = permissions.all { it.value }
            if (granted) {
                // Permisos concedidos
            } else {
                // Permisos denegados
            }
        }

        setContent {
            AutoSMSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PermissionScreen(
                        modifier = Modifier.padding(innerPadding),
                        onRequestPermissions = { checkPermissions() }
                    )
                }
            }
        }
    }

    private fun checkPermissions() {
        val requiredPermissions = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CALL_LOG
        )
        if (requiredPermissions.any { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }) {
            requestPermissionLauncher.launch(requiredPermissions)
        }
    }
}

@Composable
fun PermissionScreen(modifier: Modifier = Modifier, onRequestPermissions: () -> Unit) {
    var permissionRequested by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            permissionRequested = true
            onRequestPermissions()
        }) {
            Text(text = if (permissionRequested) "Revisar permisos" else "Solicitar permisos")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PermissionScreenPreview() {
    AutoSMSTheme {
        PermissionScreen(onRequestPermissions = {})
    }
}
