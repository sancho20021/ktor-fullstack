import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


fun main(args: Array<String>) {
    val extPort = System.getenv("PORT")?.toInt() ?: 9090
    val env = applicationEngineEnvironment {
        module {
            main()
        }
        connector {
            host = "127.0.0.1"
            port = extPort
        }
    }
    embeddedServer(Netty, env).start(true)
}

fun Application.main() {
    install(ContentNegotiation) {
        json()
    }

    routing {
        apiRoute()
        static("/") {
            resources("")
            defaultResource("index.html")
            static(CommonRoutes.CALENDARS) {
                resources("")
                defaultResource("index.html")
            }
        }
    }
}
