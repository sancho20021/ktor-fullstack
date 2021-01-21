import com.mongodb.ConnectionString
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


object DataBase {
    private val connectionString: ConnectionString? = System.getenv("MONGODB_URI")?.let {
        ConnectionString("$it?retryWrites=false")
    }

    private val client =
        if (connectionString != null) KMongo.createClient(connectionString).coroutine
        else KMongo.createClient().coroutine
    private val database = client.getDatabase(connectionString?.database ?: "lifeCalendar")
    val users = database.getCollection<FullUser>()
}