import com.google.gson.annotations.SerializedName

data class Detail1 (

		@SerializedName("code") val code : Int,
		@SerializedName("data") val data : Data1,
		@SerializedName("message") val message : String
)