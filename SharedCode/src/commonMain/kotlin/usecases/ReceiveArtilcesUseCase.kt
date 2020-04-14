package usecases

import data.Response
import model.NWArticle
import repository.ArticlesRepository

open class ReceiveArticlesUseCase(val repo: ArticlesRepository): BaseUseCase<ReceiveArticlesRequest, MutableList<NWArticle>>() {
    override suspend fun run(): Response<MutableList<NWArticle>> = repo.getFirstArticles()
}