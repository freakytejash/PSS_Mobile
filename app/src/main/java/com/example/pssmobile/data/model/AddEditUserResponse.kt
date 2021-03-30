package com.example.pssmobile.data.model

import com.google.gson.annotations.SerializedName

data class AddEditUserResponse(
        @SerializedName("Status") val status: Boolean,
        @SerializedName("Message") val message: String,
        @SerializedName("Detail") val detail: String
)
