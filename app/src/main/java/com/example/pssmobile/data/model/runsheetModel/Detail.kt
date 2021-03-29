import com.google.gson.annotations.SerializedName

data class Detail (

		@SerializedName("code") val code : Int,
		@SerializedName("data") val data : List<Data>
)