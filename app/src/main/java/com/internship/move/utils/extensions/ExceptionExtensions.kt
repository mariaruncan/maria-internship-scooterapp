package com.internship.move.utils.extensions

import com.internship.move.data.dto.ErrorResponseDTO
import com.squareup.moshi.JsonAdapter
import retrofit2.HttpException

fun Exception.toErrorResponseDTO(errorJSONAdapter: JsonAdapter<ErrorResponseDTO>): ErrorResponseDTO {
    return when (this) {
        is HttpException -> {
            try{
                errorJSONAdapter.fromJson(response()?.errorBody()?.string().toString()) ?: ErrorResponseDTO(message.toString())
            }
            catch (e: Exception){
                ErrorResponseDTO(e.message.toString())
            }
        }
        else -> ErrorResponseDTO(message.toString())
    }
}
