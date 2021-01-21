import DataBase.users
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.datetime.*
import org.litote.kmongo.eq

fun nowDate(): LocalDate {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}

private fun updateFullUserList(fullUser: FullUser) {
    val missedWeeks = (fullUser
        .userInfo
        .dateOfBirth
        .toLocalDate()
        .daysUntil(nowDate()) + 6) / 7 - fullUser.weekNoteList.size
    val oldSize = fullUser.weekNoteList.size
    fullUser.weekNoteList.addAll(List(missedWeeks) { WeekNote(id = oldSize + it) })
}

private suspend fun createFullUser(userInfo: UserInfo) =
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

internal fun Routing.apiRoute() {
    route(CommonRoutes.API) {
        route("/{id}") {
            route(CommonRoutes.WEEKNOTE) {
                post {
                    val user = users.findOne(FullUser::_id eq call.parameters["id"]!!)
                    if (user == null) {
                        call.respond(HttpStatusCode.BadRequest)
                    } else {
                        val weekNoteList = user.weekNoteList
                        val weekNote = call.receive<WeekNote>()
                        if (weekNote.id >= weekNoteList.size) {
                            call.respond(HttpStatusCode.BadRequest)
                        } else {
                            weekNoteList[weekNote.id] = weekNote
                            users.updateOneById(user._id, user)
                            call.respond(HttpStatusCode.OK)
                        }
                    }
                }
            }
            get(CommonRoutes.FULLUSER) {
                val id = call.parameters["id"]
                var user: FullUser? = null
                if (id != null) {
                    user = users.findOneById(id)
                }
                if (user != null) {
                    updateFullUserList(user)
                    users.updateOneById(user._id, user)
                }
                call.respond(
                    user ?: FullUser(UserInfo("no", "no"), mutableListOf(), "")
                )
            }
        }

        route(CommonRoutes.CREATE) {
            post {
                val userInfo = call.receive<UserInfo>()
                var dateOfBirth: LocalDate? = null
                var isDate = true
                try {
                    dateOfBirth = userInfo.dateOfBirth.toLocalDate()
                } catch (e: IllegalArgumentException) {
                    isDate = false
                }
                if (!isDate || dateOfBirth!! >= nowDate() || (nowDate() - dateOfBirth).years > MAX_AGE) {
                    call.respondText("invalid date")
                } else {
                    val fullUser = createFullUser(userInfo)
                    users.insertOne(fullUser)
                    call.respondText(
                        "http://${call.request.host()}:" +
                                "${call.request.port()}" +
                                "${CommonRoutes.CALENDARS}?id=${fullUser._id}"
                    )
                }
            }
        }
    }
}