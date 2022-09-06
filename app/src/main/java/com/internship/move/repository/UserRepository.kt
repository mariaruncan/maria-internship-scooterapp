package com.internship.move.repository

import com.internship.move.MoveApp
import com.internship.move.network.ServiceApi
import com.internship.move.network.model.AddLicenseResponse
import com.internship.move.network.model.LoginRequest
import com.internship.move.network.model.RegisterRequest
import id.zelory.compressor.Compressor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UserRepository(
    private val api: ServiceApi
) {

    suspend fun register(name: String, email: String, password: String) = api.register(RegisterRequest(name, email, password))

    suspend fun login(email: String, password: String) = api.login(LoginRequest(email, password))

    suspend fun addLicense(token: String, imagePath: String): AddLicenseResponse {
        val file = Compressor.compress(MoveApp.getAppContext(), File(imagePath))
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("productImage", file.name, requestFile)
        val requestToken = RequestBody.create(MediaType.parse("multipart/form-data"), token)

        return api.addDrivingLicense(requestToken, body)
    }
}