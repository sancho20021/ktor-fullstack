import WeekNoteList.Companion.weeks
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.datetime.minus

val weekNoteList = WeekNoteList("2002-04-30")

fun main(args: Array<String>) {
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
                println(weekNoteList.born)
                println(WeekNoteList.nowDate())
                println(WeekNoteList.nowDate().minus(weekNoteList.born).weeks())
                call.respond(weekNoteList.list)
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