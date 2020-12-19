import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

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
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
    }
}