import kotlinx.datetime.*

data class WeekNoteList(
    var born: LocalDate,
    val list: MutableList<WeekNote>
) {
    constructor(dateOfBirth: LocalDate) :
            this(dateOfBirth,
                MutableList(
                    (nowDate() - dateOfBirth).weeks()
                ) { WeekNote(id = it) })

    constructor(mmddyyyy: String) : this(parseDate(mmddyyyy))

    companion object {
        fun DatePeriod.weeks() = (this.years * 365 + this.days) / 7
        fun nowDate() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        fun parseDate(mmddyyyy: String): LocalDate {
            val params = mmddyyyy.split("-")
            val month = params[0].toInt()
            val day = params[1].toInt()
            val year = params[2].toInt()
            return LocalDate(year = year, monthNumber = month, dayOfMonth = day)
        }
    }

    fun update() {
        val missedWeeks = (nowDate() - born).weeks() - list.size
        val oldSize = list.size
        list.addAll(List(missedWeeks) { WeekNote(id = oldSize + it) })
    }
}
