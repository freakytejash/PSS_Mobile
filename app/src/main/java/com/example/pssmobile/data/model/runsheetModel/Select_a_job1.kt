import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Select_a_job1 (

		@SerializedName("display_value") val display_value : String,
		@SerializedName("ID") val iD : String
): Parcelable