import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Data(
		//@SerializedName("Checkpoint_ID2") val checkpoint_ID2: String,
		@SerializedName("checkpoints") val checkpoints: String,
		@SerializedName("Location1") val location1: String,
		@SerializedName("Job_type") val job_type: String,
		@SerializedName("Severity") val severity: String,
		@SerializedName("Allocated_to") val allocated_to: @RawValue Allocated_to,
		@SerializedName("Start_date_time") val start_date_time: String,
		@SerializedName("Job_closed") val job_closed: String,
		@SerializedName("Day_due") val day_due: String,
		@SerializedName("Active") val active: String,
		@SerializedName("Patrol_Officer") val patrol_Officer: String,
		@SerializedName("Date_time_job_completed") val date_time_job_completed: String,
		@SerializedName("ID") val iD: String,
		@SerializedName("Job_description") val job_description: String,
		@SerializedName("Evidence_3") val evidence_3: String,
		@SerializedName("incident") val incident: String,
		@SerializedName("Select_a_job1") val select_a_job1: @RawValue Select_a_job1,
		@SerializedName("End_date_time") val end_date_time: String,
		@SerializedName("Evidence_2") val evidence_2: String,
		@SerializedName("Evidence_1") val evidence_1: String,
		@SerializedName("Job_date") val job_date: String
) : Parcelable