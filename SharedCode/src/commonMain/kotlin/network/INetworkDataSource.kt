package network

import data.Response
import model.NWArticle

interface INetworkDataSource {
    suspend fun getFirstArticles(): Response<MutableList<NWArticle>>
}