package com.example.pssmobile.data.model

import com.google.gson.annotations.SerializedName


data class Detail (

		@SerializedName("UserDataContract") val userDataContract : UserDataContract,
		@SerializedName("TockenString") val tockenString : String,
		@SerializedName("StatusDataContract") val statusDataContract : String,
		@SerializedName("IsActive") val isActive : String,
		@SerializedName("CreatedDate") val createdDate : String,
		@SerializedName("ModifiedDate") val modifiedDate : String,
		@SerializedName("CreatedBy") val createdBy : String,
		@SerializedName("ModifiedBy") val modifiedBy : String,
		@SerializedName("LoginUserId") val loginUserId : String
)