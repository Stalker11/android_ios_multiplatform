package viewmodel

import data.Response
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kodein
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import model.NWArticle
import org.kodein.di.erased.instance
import org.kodein.di.newInstance
import usecases.ReceiveArticlesRequest
import usecases.ReceiveArticlesUseCase
import utils.launchSilent
import kotlin.coroutines.CoroutineContext

class ArticleViewModel:ViewModel() {
    // LIVE DATA
    var mGetGitHubRepoListLiveData = MutableLiveData<LoadingGetArticlesList>(LoadingGetArticlesListState())

    // USE CASE
    private val mGetGitHubRepoListUseCase by kodein.instance<ReceiveArticlesUseCase>()

    // ASYNC - COROUTINES
    private val coroutineContext by kodein.instance<CoroutineContext>()
    private var job: Job = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }

    /**
     * GET FIRST TEN ARTILCES LIST
     */
    fun getGitHubRepoList(page: String) = launchSilent(coroutineContext, exceptionHandler, job) {
        mGetGitHubRepoListLiveData.postValue(LoadingGetArticlesListState())

        val request = ReceiveArticlesRequest(page)
        val response = mGetGitHubRepoListUseCase.execute(request)
        processGitHubRepoListResponse(response)
    }
    /**
     * PROCCESS RESPONSE
     */
    private fun processGitHubRepoListResponse(response: Response<List<NWArticle>>){
        if (response is Response.RequestSuccess) {
            mGetGitHubRepoListLiveData.postValue(
                SuccessGetArticlesListState(
                    response
                )
            )
        } else if (response is Response.Error) {
            mGetGitHubRepoListLiveData.postValue(
                ErrorGetArticlesListState(
                    response
                )
            )
        }
    }
}