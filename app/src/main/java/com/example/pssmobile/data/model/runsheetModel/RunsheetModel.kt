import com.google.gson.annotations.SerializedName

data class RunsheetModel (

		@SerializedName("Status") val status : Boolean,
		@SerializedName("Message") val message : String,
		@SerializedName("Detail") val detail : Detail
)