import io.ktor.client.HttpClient
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.provider
import org.kodein.di.erased.singleton
import utils.Constants
import utils.MainCoroutine

val kodein = Kodein {
    bind<HttpClient>(Constants.httpClientTag) with singleton { HttpClient() }
    bind<MainCoroutine>(Constants.coroutineValue) with provider { MainCoroutine() }
}
