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

private fun getTestUserOrNull(link: String?): TestUser? {
    var testUser: TestUser? = null
    if (link != null)
        testUser = users.find { it.link == link }
    return testUser
}

internal fun Routing.apiRoute() {
    route(CommonRoutes.API) {
        route("/{link}") {
            route(CommonRoutes.WEEKNOTE) {
                post {
                    // val weekNoteList = users.find { it.link == call.parameters["link"]!! }?.weekNoteList

                    val user = Data.collection.findOne(TestUser::link eq call.parameters["link"]!!)
                    if (user == null) {
                        call.respond(HttpStatusCode.BadRequest)
                    } else {
                        val weekNoteList = user.weekNoteList
                        val weekNote = call.receive<WeekNote>()
                        if (weekNote.id >= weekNoteList.size) {
                            call.respond(HttpStatusCode.BadRequest)
                        } else {
                            weekNoteList[weekNote.id] = weekNote
                            Data.collection.updateOneById(user._id, user)
                            call.respond(HttpStatusCode.OK)
                        }

                    }
//                    if (weekNoteList == null) {
//                        call.respond(HttpStatusCode.BadRequest)
//                    } else {
//                        val weekNote = call.receive<WeekNote>()
//                        if (weekNote.id >= weekNoteList.size)
//                            call.respond(HttpStatusCode.BadRequest)
//                        weekNoteList[weekNote.id] = weekNote
//                        call.respond(HttpStatusCode.OK)
//                    }
                }
            }
            get(CommonRoutes.TESTUSER) {
                val link = call.parameters["link"]
                var user: TestUser? = null
                if (link != null) {
                    user = Data.collection.findOne(TestUser::link eq link)
                }

                if (user != null) {
                    Data.updateTestUserList(user)
                    Data.collection.updateOneById(user._id, user)
                }
                call.respond(
                    user ?: TestUser(
                        UserInfo("no", "no"),
                        mutableListOf(), "", "-1"
                    )
                )
            }
        }

        route(CommonRoutes.CREATE) {
            post {
                val userInfo = call.receive<UserInfo>()
                var dateOfBirth: LocalDate? = null
                var isOk = true
                try {
                    dateOfBirth = userInfo.dateOfBirth.toLocalDate()
                } catch (e: IllegalArgumentException) {
                    isOk = false;
                }
                if (!isOk || dateOfBirth!! >= nowDate() || (nowDate().minus(dateOfBirth).years > 100)) {
                    call.respondText("invalid date")
                } else {
                    val testUser = Data.createTestUser(userInfo)

                    Data.collection.insertOne(testUser)

                    call.respondText(
                        "127.0.0.1:9090${CommonRoutes.CALENDARS}?link=${testUser.link}"
                    )
                }
            }
        }
    }
}