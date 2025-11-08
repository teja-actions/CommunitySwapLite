package com.community.swaphub.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.community.swaphub.ui.components.CategoryChip
import com.community.swaphub.ui.components.ItemCard
import com.community.swaphub.viewmodel.ItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onItemClick: (String) -> Unit, // UUID as String
    onPostItemClick: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: ItemViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val selectedCategory = remember { mutableStateOf<String?>(null) } // Backend uses String
    
    LaunchedEffect(selectedCategory.value) {
        viewModel.loadItems(category = selectedCategory.value)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Community Swap Hub") },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onPostItemClick) {
                Icon(Icons.Default.Add, contentDescription = "Post Item")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Category Filter
            LazyRow(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                val categories = listOf("BOOKS", "TOYS", "CLOTHES", "ELECTRONICS", "FURNITURE", "KITCHEN", "SPORTS", "OTHER")
                categories.forEach { category ->
                    item {
                        CategoryChip(
                            category = category,
                            selected = selectedCategory.value == category,
                            onClick = {
                                selectedCategory.value = if (selectedCategory.value == category) null else category
                            }
                        )
                    }
                }
            }
            
            // Items Grid
            when (uiState) {
                is com.community.swaphub.viewmodel.ItemUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
                    }
                }
                is com.community.swaphub.viewmodel.ItemUiState.Error -> {
                    Text(
                        text = (uiState as com.community.swaphub.viewmodel.ItemUiState.Error).message,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    if (items.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Text("No items available")
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(1),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(items) { item ->
                                ItemCard(
                                    item = item,
                                    onClick = { onItemClick(item.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

