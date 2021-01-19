object Data {
    val sasha = UserInfo("Sasha", "2002-04-30")
    val sashaId = 20021
    val idToFullUsers = mutableMapOf(
        sashaId to FullUser(sasha, WeekNoteList(sasha.dateOfBirth))
    )
}