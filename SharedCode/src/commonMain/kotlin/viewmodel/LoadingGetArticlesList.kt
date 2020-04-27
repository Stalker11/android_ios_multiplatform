package viewmodel

import data.Response
import model.NWArticle

sealed class LoadingGetArticlesList {
    abstract val response: Response<List<NWArticle>>?
}
data class SuccessGetArticlesListState(override val response: Response<List<NWArticle>>) : LoadingGetArticlesList()
data class LoadingGetArticlesListState(override val response: Response<List<NWArticle>>? = null) : LoadingGetArticlesList()
data class ErrorGetArticlesListState(override val response: Response<List<NWArticle>>) : LoadingGetArticlesList()