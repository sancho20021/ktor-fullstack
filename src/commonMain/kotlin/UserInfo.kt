import kotlinx.datetime.*
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(val name: String, val dateOfBirth: String)
