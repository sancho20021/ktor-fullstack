import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


fun main(args: Array<String>) {
    val extPort = 8080
    embeddedServer(Netty, extPort) {
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
    }.start(wait = true)
}

