import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDate
import kotlin.random.Random

private fun getNewId(): Int {
    var id: Int? = null
    while (id == null || Data.idToFullUsers.containsKey(id)) {
        id = Random.nextInt()
    }
    return id
}

private fun getUserInfoOrNull(idParam: String?): UserInfo? {
    var userInfo: UserInfo? = null
    val id = idParam?.toIntOrNull()
    if (id != null)
        userInfo = Data.idToFullUsers[id]?.userInfo
    return userInfo
}

internal fun Routing.apiRoute() {
    route(CommonRoutes.API) {
        route("/{id}") {
            route(CommonRoutes.WEEKNOTELIST) {
                get {
                    val weekNoteList = Data.idToFullUsers[call.parameters["id"]!!.toInt()]
                        ?.weekNoteList
                    weekNoteList?.update()
                    call.respond(weekNoteList?.list ?: emptyList<WeekNote>())
                }
                post {
                    val weekNoteList = Data.idToFullUsers[call.parameters["id"]!!.toInt()]!!.weekNoteList

                    val weekNote = call.receive<WeekNote>()
                    if (weekNote.id >= weekNoteList.list.size)
                        call.respond(HttpStatusCode.BadRequest)
                    weekNoteList.list[weekNote.id] = weekNote
                    call.respond(HttpStatusCode.OK)
                }
            }
            get(CommonRoutes.CHECKUE) {
                val userInfo = getUserInfoOrNull(call.parameters["id"])
                call.respondText(if (userInfo != null) "yes" else "no")
            }
            get(CommonRoutes.USERINFO) {
                val userInfo = getUserInfoOrNull(call.parameters["id"])
                call.respond(userInfo ?: HttpStatusCode.BadRequest)
            }
        }
        route(CommonRoutes.CREATE) {
            post {
                val myUser = call.receive<UserInfo>()
                var dateOfBirth: LocalDate? = null
                var isOk = true
                try {
                    dateOfBirth = myUser.dateOfBirth.toLocalDate()
                } catch (e: IllegalArgumentException) {
                    isOk = false;
                }
                if (!isOk || dateOfBirth!! >= WeekNoteList.nowDate()
                    || (WeekNoteList.nowDate().minus(dateOfBirth).years > 100)
                ) {
                    call.respondText("invalid date")
                } else {
                    val fullUser = FullUser(myUser, WeekNoteList(myUser.dateOfBirth))
                    val newId = getNewId()
                    Data.idToFullUsers[newId] = fullUser
                    call.respondText("127.0.0.1:9090${CommonRoutes.CALENDARS}?id=$newId")
                }
            }
        }
    }
}