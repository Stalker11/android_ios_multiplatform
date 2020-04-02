import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher

internal expect val ApplicationDispatcher: CoroutineDispatcher
internal expect val httpClient:HttpClient