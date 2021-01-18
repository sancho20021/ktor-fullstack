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

// returns empty List if user with id doesn't exist
suspend fun getWeekNoteList(id: Int): List<WeekNote> {
    return jsonClient.get(endpoint + "${CommonRoutes.API}/$id/${CommonRoutes.WEEKNOTELIST}")
}

suspend fun setWeekNote(id: Int, weekNote: WeekNote) {
    jsonClient.post<Unit>(endpoint + "${CommonRoutes.API}/$id/${CommonRoutes.WEEKNOTELIST}") {
        contentType(ContentType.Application.Json)
        body = weekNote
    }
}

