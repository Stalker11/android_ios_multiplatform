import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import io.ktor.client.engine.android.Android

internal actual val ApplicationDispatcher: CoroutineDispatcher = Dispatchers.Default
internal actual val httpClient: HttpClient
    get() = HttpClient(Android) {
        engine {

        }
    }