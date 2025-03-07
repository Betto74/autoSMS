package com.example.autosms

import android.Manifest
import android.content.Context
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
import androidx.compose.material3.TextField
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
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PermissionScreen(
                            modifier = Modifier.padding(bottom = 16.dp),
                            onRequestPermissions = { checkPermissions() }
                        )
                        InputScreen(context = this@MainActivity)
                    }
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

    Button(
        onClick = {
            permissionRequested = true
            onRequestPermissions()
        },
        modifier = modifier
    ) {
        Text(text = if (permissionRequested) "Revisar permisos" else "Solicitar permisos")
    }
}


// Función para guardar datos en SharedPreferences
private fun saveData(context: Context, phoneNumber: String, message: String) {
    val sharedPreferences = context.getSharedPreferences("AutoSMS", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("phoneNumber", phoneNumber)
    editor.putString("message", message)
    editor.apply()
}

@Composable
fun InputScreen(context: Context) {
    var phoneNumber by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Número de teléfono") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Mensaje") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                // Guardar los datos en SharedPreferences
                saveData(context, phoneNumber, message)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Aceptar")
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

@Preview(showBackground = true)
@Composable
fun InputScreenPreview() {
    AutoSMSTheme {
        InputScreen(context = androidx.compose.ui.platform.LocalContext.current)
    }
}