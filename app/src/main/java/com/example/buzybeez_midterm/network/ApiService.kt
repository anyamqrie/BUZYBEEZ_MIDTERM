package com.example.buzybeez_midterm.network

import com.example.buzybeez_midterm.models.AuthResponse
import com.example.buzybeez_midterm.models.BookingModel
import com.example.buzybeez_midterm.models.HelperModel
import com.example.buzybeez_midterm.models.ServiceModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("index.php?action=get_all&table=helpers")
    suspend fun getHelpers(): Response<List<HelperModel>>

    @GET("index.php?action=get_all&table=services")
    suspend fun getServices(): Response<List<ServiceModel>>

    @POST("index.php?action=create_booking")
    suspend fun createBooking(@Body booking: BookingModel): Response<Map<String, Any>>

    @POST("index.php?action=login&table=customers")
    suspend fun login(@Body credentials: Map<String, String>): Response<AuthResponse>

    @POST("index.php?action=signup&table=customers")
    suspend fun signup(@Body details: Map<String, String>): Response<AuthResponse>

    @POST("index.php?action=insert&table=customers")
    suspend fun insertCustomer(@Body details: Map<String, String>): Response<AuthResponse>

    @POST("index.php?action=insert&table=helpers")
    suspend fun registerHelper(@Body details: Map<String, String>): Response<Map<String, Any>>

    @GET("index.php?action=get_all&table=bookings")
    suspend fun getBookings(): Response<List<BookingModel>>

    @POST("index.php?action=update&table=bookings")
    suspend fun updateBookingStatus(@Body update: Map<String, String>): Response<Map<String, Any>>

    @POST("index.php?action=update&table=customers")
    suspend fun updateCustomer(@Body update: Map<String, String>): Response<Map<String, Any>>
}