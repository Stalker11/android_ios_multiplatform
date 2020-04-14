package usecases

open class ReceiveArticlesRequest(page:String):BaseRequest {
    override fun validate(): Boolean = true
}