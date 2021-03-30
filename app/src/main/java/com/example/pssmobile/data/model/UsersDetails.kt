import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UsersDetails (

		@SerializedName("UserId") val userId : Int,
		@SerializedName("Name") var name : String,
		@SerializedName("Phone") var phone : Double,
		@SerializedName("Email") var email : String,
		@SerializedName("RoleId") val roleId : Int,
		@SerializedName("LastLogin") val lastLogin : String,
		@SerializedName("Password") var password : String,
		@SerializedName("IsAdmin") val isAdmin : Boolean,
		@SerializedName("ZohoCreatorUserName") var zohoCreatorUserName : String,
		@SerializedName("ZohoCreatorUserId") var zohoCreatorUserId : String,
		@SerializedName("ImageData") val imageData : String,
		@SerializedName("ProfileImageName") val profileImageName : String,
		@SerializedName("IsActive") val isActive : Boolean,
		@SerializedName("CreatedDate") val createdDate : String,
		@SerializedName("ModifiedDate") val modifiedDate : String,
		@SerializedName("CreatedBy") val createdBy : String,
		@SerializedName("ModifiedBy") val modifiedBy : String,
		@SerializedName("LoginUserId") val loginUserId : String
): Parcelable