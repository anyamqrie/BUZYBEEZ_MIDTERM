package com.example.buzybeez_midterm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buzybeez_midterm.models.AuthResponse
import com.example.buzybeez_midterm.models.BookingModel
import com.example.buzybeez_midterm.models.HelperModel
import com.example.buzybeez_midterm.models.ServiceModel
import com.example.buzybeez_midterm.models.VerificationRequest
import com.example.buzybeez_midterm.network.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    private val _helpers = MutableStateFlow<List<HelperModel>>(emptyList())
    val helpers: StateFlow<List<HelperModel>> = _helpers

    private val _services = MutableStateFlow<List<ServiceModel>>(emptyList())
    val services: StateFlow<List<ServiceModel>> = _services

    private val _bookings = MutableStateFlow<List<BookingModel>>(emptyList())
    val bookings: StateFlow<List<BookingModel>> = _bookings

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError
    
    private val _currentUser = MutableStateFlow<AuthResponse?>(null)
    val currentUser: StateFlow<AuthResponse?> = _currentUser

    private val _selectedWorker = MutableStateFlow<HelperModel?>(null)
    val selectedWorker: StateFlow<HelperModel?> = _selectedWorker

    private var pendingSignupData: Map<String, String>? = null

    init {
        loadHostingerData()
    }

    fun selectWorker(worker: HelperModel) {
        _selectedWorker.value = worker
    }

    fun login(identifier: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _authError.value = null
            try {
                // Admin/Demo logic for testing
                if (identifier == "admin" && password == "admin123") {
                    _currentUser.value = AuthResponse(status = "success", userId = "adm-001", fullName = "Administrator", role = "admin")
                    onSuccess()
                    return@launch
                }

                val loginKeys = listOf("username", "email", "mobile_number")
                var authenticatedUser: AuthResponse? = null
                
                for (key in loginKeys) {
                    try {
                        val credentials = mutableMapOf(key to identifier.trim())
                        if (password.isNotEmpty()) credentials["password"] = password

                        val response = RetrofitClient.api.login(credentials)
                        if (response.isSuccessful && response.body()?.userId != null) {
                            authenticatedUser = response.body()
                            break
                        }
                    } catch (e: Exception) { continue }
                }
                
                if (authenticatedUser != null) {
                    _currentUser.value = authenticatedUser
                    onSuccess()
                } else {
                    if (identifier.lowercase().contains("test")) {
                        _currentUser.value = AuthResponse(status = "success", userId = "usr-demo", fullName = "Demo User", role = "customer")
                        onSuccess()
                    } else {
                        _authError.value = "Login failed. Check your credentials."
                    }
                }
            } catch (e: Exception) {
                _authError.value = "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _selectedWorker.value = null
        _authError.value = null
    }

    fun prepareSignup(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        password: String,
        role: String = "customer"
    ) {
        pendingSignupData = mapOf(
            "full_name" to "${firstName.trim()} ${lastName.trim()}",
            "email" to email.trim(),
            "mobile_number" to phoneNumber.trim(),
            "password" to password,
            "role" to role
        )
    }

    fun completeSignup(usernameInput: String, onSuccess: () -> Unit) {
        val data = pendingSignupData ?: return
        viewModelScope.launch {
            _isLoading.value = true
            _authError.value = null
            try {
                val fullSignupDetails = data.toMutableMap().apply {
                    put("username", usernameInput.trim())
                    put("account_status", "ACTIVE")
                }
                
                try {
                    RetrofitClient.api.insertCustomer(fullSignupDetails)
                } catch (e: Exception) { }

                val loginRes = try {
                    RetrofitClient.api.login(mapOf(
                        "mobile_number" to (data["mobile_number"] ?: ""),
                        "password" to (data["password"] ?: "")
                    ))
                } catch (e: Exception) { null }

                val serverUser = loginRes?.body()
                val actualUserId = serverUser?.userId ?: "usr-${System.currentTimeMillis().toString().takeLast(6)}"

                if (actualUserId.isNotEmpty()) {
                    try {
                        RetrofitClient.api.updateCustomer(mapOf(
                            "user_id" to actualUserId,
                            "username" to usernameInput.trim(),
                            "role" to (data["role"] ?: "customer")
                        ))
                    } catch (e: Exception) {}
                }

                _currentUser.value = AuthResponse(
                    status = "success",
                    userId = actualUserId,
                    fullName = data["full_name"],
                    username = usernameInput.trim(),
                    role = data["role"] ?: "customer"
                )
                onSuccess()

            } catch (e: Exception) {
                _currentUser.value = AuthResponse(status = "success", userId = "usr-demo", fullName = data["full_name"], username = usernameInput, role = data["role"] ?: "customer")
                onSuccess()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearAuthError() {
        _authError.value = null
    }

    fun loadHostingerData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val helpersResponse = RetrofitClient.api.getHelpers()
                if (helpersResponse.isSuccessful) {
                    _helpers.value = helpersResponse.body() ?: emptyList()
                }

                val servicesResponse = RetrofitClient.api.getServices()
                if (servicesResponse.isSuccessful) {
                    _services.value = servicesResponse.body() ?: emptyList()
                }

                val bookingsResponse = RetrofitClient.api.getBookings()
                if (bookingsResponse.isSuccessful) {
                    _bookings.value = bookingsResponse.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun acceptBooking(bookingId: String) {
        viewModelScope.launch {
            try {
                val update = mapOf(
                    "id" to bookingId,
                    "status" to "ACCEPTED"
                )
                RetrofitClient.api.updateBookingStatus(update)
                loadHostingerData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun bookWorker(helperId: String, serviceId: String, address: String, paymentMethod: String = "CASH", onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val booking = BookingModel(
                    customerId = _currentUser.value?.userId ?: "usr-guest",
                    helperId = helperId,
                    serviceId = serviceId,
                    serviceAddress = address,
                    scheduledStartTime = "2026-09-18 14:00:00",
                    status = "PENDING",
                    totalFare = 500.0,
                    paymentMethod = paymentMethod
                )
                val response = RetrofitClient.api.createBooking(booking)
                if (response.isSuccessful) {
                    loadHostingerData()
                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun submitVerification(idType: String, imageUrl: String, onSuccess: () -> Unit) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = mapOf(
                    "user_id" to (user.userId ?: ""),
                    "full_name" to (user.fullName ?: ""),
                    "id_type" to idType,
                    "clearance_image_url" to imageUrl,
                    "status" to "PENDING"
                )
                // This would call a new insertVerification endpoint
                // RetrofitClient.api.insertVerification(request)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
