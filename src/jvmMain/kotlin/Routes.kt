import Data.users
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDate
import org.litote.kmongo.eq


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
                    Data.updateFullUserList(user)
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
                    isDate = false;
                }
                if (!isDate || dateOfBirth!! >= Data.nowDate() || (Data.nowDate() - dateOfBirth).years > MAX_AGE) {
                    call.respondText("invalid date")
                } else {
                    val fullUser = Data.createFullUser(userInfo)
                    users.insertOne(fullUser)
                    call.respondText(
                        "http://127.0.0.1:9090${CommonRoutes.CALENDARS}?id=${fullUser._id}"
                    )
                }
            }
        }
    }
}