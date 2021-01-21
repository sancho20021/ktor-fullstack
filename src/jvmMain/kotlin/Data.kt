import kotlinx.datetime.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo


object Data {
    val client = KMongo.createClient().coroutine
    val database = client.getDatabase("lifeCalendar")
    val users = database.getCollection<FullUser>()

    fun nowDate(): LocalDate {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    fun updateFullUserList(fullUser: FullUser) {
        val missedWeeks = (fullUser
            .userInfo
            .dateOfBirth
            .toLocalDate()
            .daysUntil(nowDate()) + 6) / 7 - fullUser.weekNoteList.size
        val oldSize = fullUser.weekNoteList.size
        fullUser.weekNoteList.addAll(List(missedWeeks) { WeekNote(id = oldSize + it) })
    }

    suspend fun createFullUser(userInfo: UserInfo) =
        FullUser(
            userInfo,
            MutableList(
                (userInfo.dateOfBirth.toLocalDate().daysUntil(nowDate()) + 6) / 7
            ) { WeekNote(id = it) },
            getNewId()
        )

    private suspend fun getNewId(): String {
        var newId: String? = null
        while (newId == null || users.findOne(FullUser::_id eq newId) != null) {
            newId = getRandomString()
        }
        return newId
    }

    private fun getRandomString(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..LINK_LEN)
            .map { allowedChars.random() }
            .joinToString("")
    }
}