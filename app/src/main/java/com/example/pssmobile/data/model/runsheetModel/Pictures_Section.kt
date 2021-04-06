import com.google.gson.annotations.SerializedName

data class Pictures_Section (

		@SerializedName("Add_picture") val add_picture : String,
		@SerializedName("Pic_Time") val pic_Time : String,
		@SerializedName("Pic_Location") val pic_Location : String
)