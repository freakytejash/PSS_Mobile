import com.google.gson.annotations.SerializedName



data class UserList (

	@SerializedName("Status") val status : Boolean,
	@SerializedName("Message") val message : String,
	@SerializedName("Detail") val detail : List<UsersDetails>
)