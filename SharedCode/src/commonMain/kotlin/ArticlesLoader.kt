import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import model.NWArticle
import org.kodein.di.erased.instance
import utils.Constants
import utils.MainCoroutine

internal expect val ApplicationDispatcher: CoroutineDispatcher

open class ArticlesLoader {
    private val hostName = "elenergi.ru/"
    private val path = "wp-json/wp/v2/posts?_embed=true"
    private val pageName = "&page="
    private val httpClient: HttpClient by kodein.instance(Constants.httpClientTag)
    private val coroutine: MainCoroutine by kodein.instance(Constants.coroutineValue)

    @UnstableDefault
    fun loadFirst(successCallback: (List<NWArticle>) -> Unit, errorCallback: (Exception) -> Unit) {
        coroutine.apply {
            launch(ApplicationDispatcher) {
                try {
                    val reposString = httpClient.get<String> {
                        url {
                            protocol = URLProtocol.HTTPS
                            host = hostName
                            encodedPath = path
                        }
                    }
                    val article = Json.nonstrict.parse(NWArticle.serializer().list, reposString)
                    successCallback(article)
                } catch (ex: Exception) {
                    errorCallback(ex)
                }
            }
        }
    }

    @UnstableDefault
    fun loadNext(
        successCallback: (List<NWArticle>) -> Unit,
        errorCallback: (Exception) -> Unit,
        page: Int
    ) {
        coroutine.apply {
            launch(ApplicationDispatcher) {
                try {
                    val reposString = httpClient.get<String> {
                        url {
                            protocol = URLProtocol.HTTPS
                            host = hostName
                            encodedPath = path.plus(pageName).plus(page)
                        }
                    }
                    val article = Json.nonstrict.parse(NWArticle.serializer().list, reposString)
                    successCallback(article)
                } catch (ex: Exception) {
                    errorCallback(ex)
                }
            }
        }
    }

    fun cancel() {
        if (coroutine.isActive) coroutine.cancel()
    }
}

