import kotlinx.datetime.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo


fun nowDate(): LocalDate {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}

object Data {
    val users = mutableListOf<TestUser>()
    val client = KMongo.createClient().coroutine
    val database = client.getDatabase("lifecalendar")
    val collection = database.getCollection<TestUser>()


    fun updateTestUserList(testUser: TestUser) {
        val missedWeeks = (testUser
            .userInfo
            .dateOfBirth
            .toLocalDate()
            .daysUntil(nowDate()) + 6) / 7 - testUser.weekNoteList.size
        val oldSize = testUser.weekNoteList.size
        testUser.weekNoteList.addAll(List(missedWeeks) { WeekNote(id = oldSize + it) })
    }

    suspend fun createTestUser(name: String, dateOfBirth: String): TestUser {
        return createTestUser(UserInfo(name, dateOfBirth))
    }

    suspend fun createTestUser(userInfo: UserInfo): TestUser {
        val newLink = getNewLink()
        val user = TestUser(
            userInfo,
            MutableList(
                (userInfo.dateOfBirth.toLocalDate().daysUntil(nowDate()) + 6) / 7
            ) { WeekNote(id = it) },
            newLink,
            newLink
        )
        return user
    }

    suspend fun getNewLink(): String {
        var newLink: String? = null
        while (newLink == null || collection.findOne(TestUser::link eq newLink) != null) {
            newLink = getRandomString()
        }
        return newLink
    }

    fun findOne(predicate: TestUser.() -> Boolean): TestUser? {
        for (user in users) {
            if (user.predicate()) {
                return user
            }
        }
        return null
    }

    fun getRandomString(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..LINK_LEN)
            .map { allowedChars.random() }
            .joinToString("")
    }
}