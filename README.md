# Community Swap Hub - Android App

A full-featured Android mobile application built with Kotlin and Jetpack Compose for the Community Swap Hub platform. This app enables users to give away, swap, and reuse everyday items while earning points and connecting locally.

## 🌟 Features

- **User Authentication**: Register and login with secure token-based authentication
- **Item Management**: Post, browse, and manage items with categories and filters
- **Chat System**: Real-time messaging with item owners
- **Swap Requests**: Request swaps, accept/decline, and complete transactions
- **Points System**: Track points earned from giveaways and swaps
- **Admin Dashboard**: Admin-only panel for content moderation
- **Offline Support**: Room database for local caching
- **Modern UI**: Beautiful Material Design 3 interface

## 🏗️ Architecture

- **MVVM Pattern**: ViewModels manage UI state and business logic
- **Repository Pattern**: Clean separation between data sources
- **Dependency Injection**: Hilt for dependency management
- **Jetpack Compose**: Modern declarative UI framework
- **Room Database**: Local data persistence
- **Retrofit**: RESTful API communication
- **Coroutines & Flow**: Asynchronous programming

## 📋 Prerequisites

- Android Studio Hedgehog or later
- JDK 17 or later
- Android SDK 24+ (Android 7.0+)
- Spring Boot backend running (see backend setup)

## 🚀 Setup Instructions

### 1. Backend Configuration

Update the base URL in `AppModule.kt`:

```kotlin
// For Android Emulator
private const val BASE_URL = "http://10.0.2.2:8080/"

// For real device, use your computer's IP address
// private const val BASE_URL = "http://192.168.x.x:8080/"
```

**Important**: 
- For emulator: Use `10.0.2.2` which maps to `localhost` on your host machine
- For physical device: Use your computer's local IP address (e.g., `192.168.1.100`)
- Ensure your Spring Boot backend allows connections from your device/emulator

### 2. Build and Run

1. Open the project in Android Studio
2. Sync Gradle files
3. Ensure your Spring Boot backend is running
4. Run the app on an emulator or physical device

## 📱 App Structure

### Screens

- **LoginScreen**: User authentication
- **RegisterScreen**: New user registration
- **HomeScreen**: Browse items with category filters
- **ItemDetailScreen**: View item details, chat, and request swaps
- **PostItemScreen**: Create new item listings
- **ProfileScreen**: View profile, items, and points
- **ChatScreen**: Messaging interface
- **AdminDashboardScreen**: Admin moderation panel

### Key Components

- **Data Models**: User, Item, SwapRequest, Chat, PointsHistory
- **Repositories**: AuthRepository, ItemRepository, ChatRepository, etc.
- **ViewModels**: Manage UI state and business logic
- **API Service**: Retrofit interface for backend communication
- **Room Database**: Local data caching

## 🔌 API Endpoints

The app expects the following Spring Boot endpoints:

- `POST /api/users/register` - User registration
- `POST /api/users/login` - User login
- `GET /api/items` - Get items (with optional filters)
- `POST /api/items` - Create new item
- `GET /api/items/{id}` - Get item details
- `POST /api/swap-requests` - Create swap request
- `POST /api/chat/send` - Send message
- `GET /api/chat/conversations/{userId}` - Get conversations
- `GET /api/points/user/{userId}` - Get user points
- And more...

## 🎨 UI/UX Features

- Material Design 3 components
- Smooth animations and transitions
- Responsive layouts
- Dark theme support (system-based)
- Loading states and error handling
- Image loading with Coil

## 🔒 Security

- Token-based authentication
- Secure credential storage with DataStore
- HTTPS support (configure in backend)
- Input validation

## 📦 Dependencies

Key libraries used:
- Jetpack Compose
- Hilt (Dependency Injection)
- Retrofit & OkHttp (Networking)
- Room (Database)
- Coil (Image Loading)
- Navigation Compose
- DataStore (Preferences)
- Coroutines & Flow

## 🐛 Troubleshooting

### Connection Issues

1. **Emulator**: Ensure backend is running and use `10.0.2.2:8080`
2. **Physical Device**: 
   - Use your computer's local IP
   - Ensure device and computer are on same network
   - Check firewall settings
3. **Backend**: Verify CORS settings allow mobile app connections

### Build Issues

- Ensure JDK 17 is set in project settings
- Sync Gradle files
- Clean and rebuild project

## 🚧 Future Enhancements

- Image upload functionality
- Push notifications
- Google Maps integration for location
- Item recommendations
- Social features
- In-app camera for item photos

## 📄 License

This project is part of the Community Swap Hub platform.

## 👥 Contributing

This is a full-stack project. Ensure backend endpoints match the API service definitions in the app.

---

**Note**: Make sure your Spring Boot backend is running and accessible before testing the app. Update the `BASE_URL` in `AppModule.kt` to match your backend configuration.

