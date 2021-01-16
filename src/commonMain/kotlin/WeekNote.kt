import kotlinx.serialization.Serializable

@Serializable
data class WeekNote(var id: Int, var desc: String = "")