package com.community.swaphub.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.community.swaphub.viewmodel.AuthViewModel
import com.community.swaphub.viewmodel.AuthUiState

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    // State variables for input fields
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    // Observe ViewModel state
    val uiState by viewModel.uiState.collectAsState()

    // Navigate on successful registration
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Join the Community!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // --- Name field ---
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Email field ---
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Password field ---
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Location field (optional) ---
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // --- Error message display ---
        if (uiState is AuthUiState.Error) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = (uiState as AuthUiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Register button ---
        Button(
            onClick = {
                viewModel.register(
                    name = name.ifBlank { "Anonymous" }, // avoid null name crash
                    email = email,
                    password = password,
                    location = if (location.isNotBlank()) location else null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is AuthUiState.Loading
        ) {
            if (uiState is AuthUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(4.dp)
                )
            } else {
                Text("Register")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Navigate to Login ---
        TextButton(onClick = onNavigateToLogin) {
            Text("Already have an account? Login")
        }
    }
}
