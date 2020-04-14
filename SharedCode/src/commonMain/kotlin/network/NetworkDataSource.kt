package network

import data.Response
import model.NWArticle

class NetworkDataSource(private val loader: ArticlesLoader):INetworkDataSource {
    override suspend fun getFirstArticles(): Response<MutableList<NWArticle>> = loader.loadFirst()

}