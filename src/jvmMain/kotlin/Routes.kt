import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
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

internal fun Routing.apiRoute() {
    route(CommonRoutes.API) {
        route("/{id}" + CommonRoutes.WEEKNOTELIST) {
            get {
                val weekNoteList = Data.idToFullUsers[call.parameters["id"]!!.toInt()]
                    ?.weekNoteList?.list
                    ?: emptyList()
                call.respond(weekNoteList)
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
        route(CommonRoutes.CREATE) {
            post {
                val myUser = call.receive<UserInfo>()
                val dateOfBirth = myUser.dateOfBirth.toLocalDate()
                if (dateOfBirth >= WeekNoteList.nowDate()
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
        route(CommonRoutes.CHECKUE) {
            get("/{id}") {
                var id = call.parameters["id"]?.toIntOrNull()
                if (id != null) {
                    if (!Data.idToFullUsers.containsKey(id))
                        id = null
                }
                call.respondText(if (id != null) "yes" else "no")
            }
        }
    }
}