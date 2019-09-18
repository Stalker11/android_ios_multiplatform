import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal expect val ApplicationDispatcher: CoroutineDispatcher

class GitHubApiClient(private val githubUserName: String, private val githubPassword: String){

private val httpClient = HttpClient()

fun repos(successCallback: (MutableList<String>) -> Unit, errorCallback: (Exception) -> Unit) {
    GlobalScope.apply {
        launch(ApplicationDispatcher) {
            try {
                val reposString = httpClient.get<String> {
                    url {
                        protocol = URLProtocol.HTTPS
                        port = 443
                        host = "https://github.com/"
                        encodedPath = "user/repos"
                        header("Authorization", "Basic " + "")
                        //Timber.info { "Sending request to: ${buildString()}" }
                    }
                }
             //   val repos = JSON(strictMode = false).parse(GitHubRepo.serializer().list, reposString)
                successCallback(mutableListOf(reposString))
            } catch (ex: Exception) {
                errorCallback(ex)
            }
        }
    }
}
}

