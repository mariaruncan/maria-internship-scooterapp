package com.internship.move.repository

import android.content.Context
import com.internship.move.network.UserApi
import com.internship.move.data.dto.AddLicenseResponseDTO
import com.internship.move.data.dto.LoginRequestDTO
import com.internship.move.data.dto.RegisterRequestDTO
import com.internship.move.data.dto.UserDTO
import id.zelory.compressor.Compressor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UserRepository(
    private val api: UserApi,
    private val compressor: Compressor,
    private val context: Context
) {

    suspend fun register(name: String, email: String, password: String) = api.register(RegisterRequestDTO(name, email, password))

    suspend fun login(email: String, password: String) = api.login(LoginRequestDTO(email, password))

    suspend fun addLicense(imagePath: String): AddLicenseResponseDTO {
        val file = compressor.compress(context, File(imagePath))
        val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
        val filePart = MultipartBody.Part.createFormData(KEY_JSON_IMAGE, file.name, requestFile)

        return api.addDrivingLicense(filePart)
    }

    suspend fun getCurrentUser(): UserDTO? = api.getCurrentUser().user

    companion object {
        private const val KEY_JSON_IMAGE = "productImage"
    }
}
