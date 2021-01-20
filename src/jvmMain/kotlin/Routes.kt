import Data.createTestUser
import Data.nowDate
import Data.updateTestUserList
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
    while (id == null || Data.idToTestUser.containsKey(id)) {
        id = Random.nextInt()
    }
    return id
}

private fun getTestUserOrNull(idParam: String?): TestUser? {
    var testUser: TestUser? = null
    val id = idParam?.toIntOrNull()
    if (id != null)
        testUser = Data.idToTestUser[id]
    return testUser
}

internal fun Routing.apiRoute() {
    route(CommonRoutes.API) {
        route("/{id}") {
            route(CommonRoutes.WEEKNOTE) {
                post {
                    val weekNoteList = Data.idToTestUser[call.parameters["id"]!!.toInt()]!!.weekNoteList

                    val weekNote = call.receive<WeekNote>()
                    if (weekNote.id >= weekNoteList.size)
                        call.respond(HttpStatusCode.BadRequest)
                    weekNoteList[weekNote.id] = weekNote
                    call.respond(HttpStatusCode.OK)
                }
            }
            get(CommonRoutes.TESTUSER) {
                val testUser = getTestUserOrNull(call.parameters["id"])
                if (testUser != null) {
                    updateTestUserList(testUser)
                }
                call.respond(testUser ?: TestUser(UserInfo("no", "no"), mutableListOf()))
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
                if (!isOk || dateOfBirth!! >= nowDate()
                    || (nowDate().minus(dateOfBirth).years > 100)
                ) {
                    call.respondText("invalid date")
                } else {
                    val testUser = createTestUser(userInfo)
                    val newId = getNewId()
                    Data.idToTestUser[newId] = testUser
                    call.respondText("127.0.0.1:9090${CommonRoutes.CALENDARS}?id=$newId")
                }
            }
        }
    }
}