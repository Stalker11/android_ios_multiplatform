package model
import kotlinx.serialization.*

@Serializable
data class NWArticle(val id:Long, val date:String, @SerialName("date_gmt")val dateGmt:String)