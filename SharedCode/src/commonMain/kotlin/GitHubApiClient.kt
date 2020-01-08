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

internal expect val ApplicationDispatcher: CoroutineDispatcher

open class GitHubApiClient(open val login: String, open val password: String) {

    private val httpClient = HttpClient()
    private val json = "[{ " +
            "   \"id\":100," +
            "   \"date\":\"27.10.2017\"" +
            ", \"date_gmt\": \"2019-12-22T13:30:38\"," +
            "\"ff\":\"gggg\"}]"

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
                    val json =
                        Json(JsonConfiguration.Stable)

                    //.parse(GitHubRepo.serializer().list, reposString)
                    //JSON.nonstrict
                    val article = Json.nonstrict.parse(NWArticle.serializer().list,reposString)
                    successCallback(article)
                } catch (ex: Exception) {
                    errorCallback(ex)
                }
            }
        }
    }
}

