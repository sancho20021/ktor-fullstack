import kotlinx.datetime.*

data class WeekNoteList(
    var born: LocalDate,
    val list: MutableList<WeekNote>
) {
    constructor(dateOfBirth: LocalDate) :
            this(dateOfBirth,
                MutableList(
                    (dateOfBirth.daysUntil(nowDate()) + 6) / 7
                ) { WeekNote(id = it) })

    constructor(yyyymmdd: String) : this(yyyymmdd.toLocalDate())

    companion object {
        fun nowDate() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    fun update() {
        val missedWeeks = (born.daysUntil(nowDate()) + 6) / 7 - list.size
        val oldSize = list.size
        list.addAll(List(missedWeeks) { WeekNote(id = oldSize + it) })
    }
}
