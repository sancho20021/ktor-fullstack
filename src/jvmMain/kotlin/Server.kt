import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main(args: Array<String>) {
    val env = applicationEngineEnvironment {
        module {
            runBlocking {
                main()
            }
        }
        connector {
            host = "127.0.0.1"
            port = 9090
        }
    }
    embeddedServer(Netty, env).start(true)
}

suspend fun Application.main() {
    Data.users.add(Data.createTestUser("Саша", "2002-04-30"))
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
