package com.example.buzybeez_midterm.models

import com.google.gson.annotations.SerializedName

data class HelperModel(
    @SerializedName("helper_id") val helperId: String = "",
    @SerializedName("full_name") val fullName: String = "",
    @SerializedName("mobile_number") val mobileNumber: String = "",
    @SerializedName("verification_status") val verificationStatus: String = "PENDING",
    @SerializedName("availability_status") val availabilityStatus: String = "OFFLINE",
    @SerializedName("current_latitude") val latitude: Double? = null,
    @SerializedName("current_longitude") val longitude: Double? = null
)

data class ServiceModel(
    @SerializedName("service_id") val serviceId: String = "",
    @SerializedName("service_name") val serviceName: String = "",
    @SerializedName("description") val description: String? = null,
    @SerializedName("category") val category: String = "",
    @SerializedName("base_rate_per_hour") val baseRatePerHour: Double = 0.0,
    @SerializedName("is_active") val isActive: Int = 1
)

data class BookingModel(
    @SerializedName("booking_id") val bookingId: String? = null,
    @SerializedName("customer_id") val customerId: String = "",
    @SerializedName("helper_id") val helperId: String? = null,
    @SerializedName("service_id") val serviceId: String = "",
    @SerializedName("service_address") val serviceAddress: String = "",
    @SerializedName("scheduled_start_time") val scheduledStartTime: String = "",
    @SerializedName("hourly_rate_applied") val hourlyRateApplied: Double = 0.0,
    @SerializedName("total_fare") val totalFare: Double = 0.0,
    @SerializedName("status") val status: String = "PENDING",
    @SerializedName("payment_method") val paymentMethod: String = "CASH"
)

data class AuthResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("user_id") val userId: String? = null,
    @SerializedName("full_name") val fullName: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("role") val role: String? = "customer" // roles: customer, worker, admin
)

data class VerificationRequest(
    @SerializedName("verification_id") val id: String? = null,
    @SerializedName("user_id") val userId: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("id_type") val idType: String,
    @SerializedName("clearance_image_url") val imageUrl: String? = null,
    @SerializedName("status") val status: String = "PENDING"
)
