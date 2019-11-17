package model
import kotlinx.serialization.*

@Serializable
data class NWArticles (val articles:MutableList<NWArticle>)