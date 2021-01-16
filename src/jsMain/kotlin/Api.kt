import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.browser.window

val endpoint = window.location.origin

val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
}

suspend fun getWeekNoteList(): List<WeekNote> {
    return jsonClient.get(endpoint + Paths.weekNoteListPath)
}

suspend fun setWeekNote(weekNote: WeekNote) {
    jsonClient.post<Unit>(endpoint + Paths.weekNoteListPath) {
        contentType(ContentType.Application.Json)
        body = weekNote
    }
}
