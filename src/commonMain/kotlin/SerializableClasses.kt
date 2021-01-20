import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(val name: String, val dateOfBirth: String)

@Serializable
data class TestUser(
    val userInfo: UserInfo,
    val weekNoteList: MutableList<WeekNote>
)

@Serializable
data class WeekNote(var id: Int, var desc: String = "")