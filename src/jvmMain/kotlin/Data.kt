object Data {
    val sasha = MyUser("Sasha", "04-30-2002")
    val sashaId = 20021
    val idToFullUsers: Map<Int, FullUser> = mapOf(
        sashaId to FullUser(sasha, WeekNoteList(sasha.dateOfBirth))
    )
}