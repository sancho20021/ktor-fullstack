import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.browser.window

val endpoint = window.location.origin

val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
}

suspend fun getWeekNoteList(): List<WeekNote> {
    return jsonClient.get(endpoint + Paths.weekNoteListPath)
}
