package com.community.swaphub.ui.screens.post

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.community.swaphub.data.model.ItemType
import com.community.swaphub.viewmodel.ItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItemScreen(
    onBackClick: () -> Unit,
    onPostSuccess: () -> Unit,
    viewModel: ItemViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) } // Backend uses String
    var selectedType by remember { mutableStateOf<ItemType>(ItemType.GIVEAWAY) }
    
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(uiState) {
        if (uiState is com.community.swaphub.viewmodel.ItemUiState.Success) {
            onPostSuccess()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Post an Item") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 5
            )
            
            // Category Selection
            Text("Category", style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val categories = listOf("BOOKS", "TOYS", "CLOTHES", "ELECTRONICS", "FURNITURE", "KITCHEN", "SPORTS", "OTHER")
                categories.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            // Type Selection
            Text("Type", style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ItemType.values().forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { selectedType = type },
                        label = { Text(type.name) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            if (uiState is com.community.swaphub.viewmodel.ItemUiState.Error) {
                Text(
                    text = (uiState as com.community.swaphub.viewmodel.ItemUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Button(
                onClick = {
                    if (selectedCategory != null) {
                        viewModel.postItem(
                            title = title,
                            description = description,
                            category = selectedCategory,
                            type = selectedType,
                            location = location
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() && description.isNotBlank() && 
                         location.isNotBlank() && selectedCategory != null &&
                         uiState !is com.community.swaphub.viewmodel.ItemUiState.Loading
            ) {
                if (uiState is com.community.swaphub.viewmodel.ItemUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Post Item")
                }
            }
        }
    }
}

