import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import model.NWArticle

class TestSerializer(private val content:String) {
    fun getResult():List<NWArticle>{
        val json = Json(JsonConfiguration.Stable)
        return json.parse(NWArticle.serializer().list,content)
    }
}