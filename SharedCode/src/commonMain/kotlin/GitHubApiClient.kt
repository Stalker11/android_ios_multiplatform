import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import model.NWArticle
import model.NWArticles

internal expect val ApplicationDispatcher: CoroutineDispatcher

open class GitHubApiClient(open val login:String, open val password:String){

private val httpClient = HttpClient()

fun repos(successCallback: (List<NWArticle>) -> Unit, errorCallback: (Exception) -> Unit) {
    GlobalScope.apply {
        launch(ApplicationDispatcher) {
            try {
                val reposString = httpClient.get<String> {
                    url {
                        protocol = URLProtocol.HTTPS
                        port = 443
                        host = "elenergi.ru/"
                        encodedPath = "wp-json/wp/v2/posts?_embed=true"
                       // header("Authorization", "Basic " + "")
                        //Timber.info { "Sending request to: ${buildString()}" }
                    }
                }
                val json = Json(JsonConfiguration.Stable)//.parse(GitHubRepo.serializer().list, reposString)
                val article = json.parse(NWArticle.serializer().list,reposString)
                successCallback(article)
            } catch (ex: Exception) {
                errorCallback(ex)
            }
        }
    }
}
}

