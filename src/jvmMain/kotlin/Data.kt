import kotlinx.datetime.*

object Data {
    val sashaId = 20021
    val idToTestUser = mutableMapOf(
        sashaId to createTestUser("Саша", "2002-04-30")
    )

    fun nowDate() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    fun updateTestUserList(testUser: TestUser) {
        val missedWeeks = (testUser
            .userInfo
            .dateOfBirth
            .toLocalDate()
            .daysUntil(nowDate()) + 6) / 7 - testUser.weekNoteList.size
        val oldSize = testUser.weekNoteList.size
        testUser.weekNoteList.addAll(List(missedWeeks) { WeekNote(id = oldSize + it) })
    }

    fun createTestUser(name: String, dateOfBirth: String): TestUser {
        return createTestUser(UserInfo(name, dateOfBirth))
    }

    fun createTestUser(userInfo: UserInfo): TestUser {
        return TestUser(
            userInfo,
            MutableList(
                (userInfo.dateOfBirth.toLocalDate().daysUntil(nowDate()) + 6) / 7
            ) { WeekNote(id = it) }
        )
    }
}