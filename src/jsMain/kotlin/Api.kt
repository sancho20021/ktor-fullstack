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

suspend fun setWeekNote(id: Int, weekNote: WeekNote) {
    jsonClient.post<Unit>(endpoint + "${CommonRoutes.API}/$id/${CommonRoutes.WEEKNOTE}") {
        contentType(ContentType.Application.Json)
        body = weekNote
    }
}

suspend fun getNewUserLink(userInfo: UserInfo): String {
    return jsonClient.post(endpoint + CommonRoutes.API + CommonRoutes.CREATE) {
        contentType(ContentType.Application.Json)
        body = userInfo
    }
}

// returns TestUser with name = "no" and dateOfBirth = "no" if user doesn't exist
suspend fun getTestUser(id: Int): TestUser {
    return jsonClient.get(endpoint + CommonRoutes.API + "/$id" + CommonRoutes.TESTUSER)
}

