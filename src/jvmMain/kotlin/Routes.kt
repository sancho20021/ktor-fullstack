import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

internal fun Routing.apiRoute() {
    route(CommonRoutes.API) {
        route("/{id}" + CommonRoutes.WEEKNOTELIST) {
            get {
                val weekNoteList = Data.idToFullUsers[call.parameters["id"]!!.toInt()]
                    ?.weekNoteList?.list
                    ?: emptyList()
                if (weekNoteList.isEmpty()) {
                    call.respondRedirect(CommonRoutes.API + CommonRoutes.INVALID)
                } else {
                    call.respond(weekNoteList)
                }
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
    }
}