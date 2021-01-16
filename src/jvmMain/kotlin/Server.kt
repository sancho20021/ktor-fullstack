import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.datetime.minus

val weekNoteList = WeekNoteList("2002-04-30")

fun main(args: Array<String>) {
    weekNoteList.list[0].desc = "День рождения"
    weekNoteList.list[5].desc = "5 неделя с рождения\n перенос строки"
    val env = applicationEngineEnvironment {
        module {
            main()
        }
        connector {
            host = "127.0.0.1"
            port = 9090
        }
    }
    embeddedServer(Netty, env).start(true)
}

fun Application.main() {
    install(ContentNegotiation) {
        json()
//        jackson {
//            enable(SerializationFeature.INDENT_OUTPUT)
//        }
    }

    routing {
        route(WeekNoteList.path) {
            get {
                weekNoteList.update()
                call.respond(weekNoteList.list)
            }
            post {
                val weekNote = call.receive<WeekNote>()
                if (weekNote.id >= weekNoteList.list.size)
                    call.respond(HttpStatusCode.BadRequest)
                weekNoteList.list[weekNote.id] = weekNote
                call.respond(HttpStatusCode.OK)
            }
        }
        get("/") {
            call.respondText(
                this::class.java.classLoader.getResource("index.html")!!.readText(),
                ContentType.Text.Html
            )
        }
        static("/") {
            resources("")
        }
    }
}