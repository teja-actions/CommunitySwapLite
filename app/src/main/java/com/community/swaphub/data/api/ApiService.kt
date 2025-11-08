package com.community.swaphub.data.api

import com.community.swaphub.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // User endpoints
    // Backend expects User object, not RegisterRequest
    @POST("api/users/register")
    suspend fun register(@Body user: User): Response<User> // Backend returns User, not AuthResponse
    
    // Backend uses /api/auth/login, not /api/users/login
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @GET("api/users/{id}")
    suspend fun getUser(@Path("id") id: String): Response<User> // UUID as String
    
    @PUT("api/users/{id}")
    suspend fun updateUser(
        @Path("id") id: String, // UUID as String
        @Body user: User
    ): Response<User>
    
    @GET("api/users/me")
    suspend fun getCurrentUser(): Response<User>
    
    // Item endpoints
    @GET("api/items")
    suspend fun getItems(
        @Query("type") type: String? = null, // Backend uses 'type' not 'category'
        @Query("location") location: String? = null
    ): Response<List<Item>>
    
    @GET("api/items/{id}")
    suspend fun getItem(@Path("id") id: String): Response<Item> // UUID as String
    
    @POST("api/items")
    suspend fun postItem(@Body request: PostItemRequest): Response<Item>
    
    @PUT("api/items/{id}")
    suspend fun updateItem(
        @Path("id") id: String, // UUID as String
        @Body item: PostItemRequest
    ): Response<Item>
    
    @DELETE("api/items/{id}")
    suspend fun deleteItem(@Path("id") id: String): Response<Unit> // UUID as String
    
    @GET("api/items/my-items")
    suspend fun getMyItems(): Response<List<Item>>
    
    @GET("api/items/others-items")
    suspend fun getOthersItems(): Response<List<Item>>
    
    // Swap Request endpoints
    @POST("api/swap-requests")
    suspend fun createSwapRequest(@Body request: CreateSwapRequest): Response<SwapRequest>
    
    @GET("api/swap-requests/my-swaps")
    suspend fun getMySwapRequests(): Response<List<SwapRequest>> // Backend uses /my-swaps
    
    @GET("api/swap-requests/item/{itemId}")
    suspend fun getSwapRequestsByItem(@Path("itemId") itemId: String): Response<List<SwapRequest>>
    
    @PUT("api/swap-requests/{id}/accept")
    suspend fun acceptSwapRequest(@Path("id") id: String): Response<SwapRequest> // UUID as String
    
    @PUT("api/swap-requests/{id}/complete")
    suspend fun completeSwapRequest(@Path("id") id: String): Response<SwapRequest> // UUID as String
    
    // Chat endpoints (keeping as is for now, may need updates)
    @POST("api/chat/send")
    suspend fun sendMessage(@Body request: SendMessageRequest): Response<Chat>
    
    @GET("api/chat/conversations/{userId}")
    suspend fun getConversations(@Path("userId") userId: String): Response<List<ChatConversation>> // UUID as String
    
    @GET("api/chat/messages")
    suspend fun getMessages(
        @Query("userId1") userId1: String, // UUID as String
        @Query("userId2") userId2: String, // UUID as String
        @Query("itemId") itemId: String? = null // UUID as String
    ): Response<List<Chat>>
    
    // Points endpoints
    @GET("api/points/user/{userId}")
    suspend fun getUserPoints(@Path("userId") userId: String): Response<Int> // UUID as String
    
    @GET("api/points/history/{userId}")
    suspend fun getPointsHistory(@Path("userId") userId: String): Response<List<PointsHistory>> // UUID as String
    
    // Admin endpoints
    @GET("api/admin/users")
    suspend fun getAllUsers(): Response<List<User>>
    
    @GET("api/admin/items")
    suspend fun getAllItems(): Response<List<Item>>
    
    @DELETE("api/admin/users/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<Unit> // UUID as String
    
    @DELETE("api/admin/items/{id}")
    suspend fun deleteItemAdmin(@Path("id") id: String): Response<Unit> // UUID as String
}

