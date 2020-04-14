package repository

import data.Response
import model.NWArticle
import network.NetworkDataSource

class ArticlesRepository(private val dataSource: NetworkDataSource) {
    suspend fun getFirstArticles():Response<MutableList<NWArticle>> = dataSource.getFirstArticles()
}