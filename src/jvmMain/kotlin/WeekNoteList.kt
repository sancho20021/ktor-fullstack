import kotlinx.datetime.*

data class WeekNoteList(
    var born: LocalDate,
    val list: MutableList<WeekNote>
) {
    constructor(dateOfBirth: LocalDate) :
            this(dateOfBirth,
                MutableList(
                    (nowDate() - dateOfBirth).weeks()
                ) { WeekNote() })

    constructor(dateOfBirth: String) : this(dateOfBirth.toLocalDate())

    companion object {
        const val path = Paths.weekNoteListPath
        fun DatePeriod.weeks() = (this.years * 365 + this.days) / 7;
        fun nowDate() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    fun update() {
        val missedWeeks = (nowDate() - born).weeks() - list.size;
        list.addAll(List(missedWeeks) { WeekNote() })
    }
}
