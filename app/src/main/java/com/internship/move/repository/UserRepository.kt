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

    suspend fun addLicense(tokenString: String, imagePath: String): AddLicenseResponse {
        val file = Compressor.compress(MoveApp.getAppContext(), File(imagePath))
        val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
        val filePart = MultipartBody.Part.createFormData(KEY_FILE, file.name, requestFile)
        val requestToken = RequestBody.create(MultipartBody.FORM, tokenString)

        return api.addDrivingLicense(requestToken, filePart)
    }

    companion object {
        private const val KEY_FILE = "productImage"
    }
}