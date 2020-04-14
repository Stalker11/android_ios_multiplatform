package usecases

import data.Response
import kotlinx.coroutines.channels.Channel

abstract class BaseUseCase<R: BaseRequest, T>() {
    var mRequest:R? = null
    private val channel = Channel<Response<T>>(Channel.UNLIMITED)
    suspend fun execute(request: R? = null):Response<T>{
        mRequest = request
        val validated = request?.validate() ?: false
        if (validated) return run()
        return Response.Error(IllegalArgumentException("Cannot get data from API"))
    }
    abstract suspend fun run(): Response<T>

    fun getChannel() = channel
}