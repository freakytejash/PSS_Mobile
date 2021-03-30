package com.example.pssmobile.data.model

import com.google.gson.annotations.SerializedName

data class UserDataContract (

		@SerializedName("UserId") val userId : Int,
		@SerializedName("Name") val name : String,
		@SerializedName("Phone") val phone : Double,
		@SerializedName("Email") val email : String,
		@SerializedName("RoleId") val roleId : Int,
		@SerializedName("LastLogin") val lastLogin : String,
		@SerializedName("Password") val password : String,
		@SerializedName("IsAdmin") val isAdmin : Boolean,
		@SerializedName("ZohoCreatorUserId") val zohoCreatorUserId : String,
		@SerializedName("ImageData") val imageData : String,
		@SerializedName("ProfileImageName") val profileImageName : String,
		@SerializedName("IsActive") val isActive : Boolean,
		@SerializedName("CreatedDate") val createdDate : String,
		@SerializedName("ModifiedDate") val modifiedDate : String,
		@SerializedName("CreatedBy") val createdBy : String,
		@SerializedName("ModifiedBy") val modifiedBy : String,
		@SerializedName("LoginUserId") val loginUserId : String
)