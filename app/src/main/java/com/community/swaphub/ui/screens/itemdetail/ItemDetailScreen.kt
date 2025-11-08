package com.community.swaphub.ui.screens.itemdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.community.swaphub.data.model.ItemType
import com.community.swaphub.viewmodel.ItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    itemId: String, // UUID as String
    onBackClick: () -> Unit,
    onChatClick: (String) -> Unit, // UUID as String
    onSwapClick: (String) -> Unit, // UUID as String
    viewModel: ItemViewModel = hiltViewModel()
) {
    val item by viewModel.selectedItem.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(itemId) {
        viewModel.loadItem(itemId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Item Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        when (uiState) {
            is com.community.swaphub.viewmodel.ItemUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is com.community.swaphub.viewmodel.ItemUiState.Error -> {
                Text(
                    text = (uiState as com.community.swaphub.viewmodel.ItemUiState.Error).message,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                item?.let { currentItem ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Item Image
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = currentItem.imageUrl ?: "https://via.placeholder.com/400"
                            ),
                            contentDescription = currentItem.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                            contentScale = ContentScale.Crop
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Item Details
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Text(
                                    text = currentItem.title,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Surface(
                                    color = when (currentItem.type) {
                                        ItemType.GIVEAWAY -> MaterialTheme.colorScheme.primaryContainer
                                        ItemType.SWAP -> MaterialTheme.colorScheme.secondaryContainer
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = currentItem.type.name,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            currentItem.category?.let { category ->
                                Text(
                                    text = "Category: $category",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "Location: ${currentItem.location}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.titleMedium
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = currentItem.description ?: "No description",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { currentItem.ownerId?.let { onChatClick(it) } },
                                    modifier = Modifier.weight(1f),
                                    enabled = currentItem.ownerId != null
                                ) {
                                    Text("Chat with Owner")
                                }
                                
                                if (currentItem.type == ItemType.SWAP) {
                                    Button(
                                        onClick = { onSwapClick(currentItem.id) },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Request Swap")
                                    }
                                }
                            }
                        }
                    }
                } ?: run {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text("Item not found")
                    }
                }
            }
        }
    }
}

