package com.team.todoktodok.data.network.adapter

import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.model.exception.toDomain
import kotlinx.serialization.json.Json
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * [ResponseCall] 은 Retrofit 의 [Call] 을 감싸
 * 서버 응답을 [NetworkResult] 형태로 변환하는 커스텀 Call 구현체입니다.
 *
 * @param T 실제 서버 응답 바디 타입
 * @property proxy 원본 Retrofit [Call] 객체
 *
 * 이 클래스는 [TodokTodokCallAdapterFactory] 를 통해 Retrofit 에서 사용되며,
 * 성공/실패/네트워크 오류 등을 일관된 [NetworkResult] 타입으로 변환합니다.
 */
class ResponseCall<T>(
    private val proxy: Call<T>,
) : Call<NetworkResult<T>> {
    /**
     * 비동기 요청을 실행합니다.
     *
     * - 성공 시: 응답 바디가 존재하면 [NetworkResult.Success],
     *   바디가 null 이면 [NetworkResult.NullResult]
     * - 실패 시: [NetworkResult.ResponseException] 또는 [NetworkResult.RequestException]
     *
     * @param callback [NetworkResult] 로 래핑된 응답을 전달받는 콜백
     */
    override fun enqueue(callback: Callback<NetworkResult<T>>) {
        proxy.enqueue(
            object : Callback<T> {
                override fun onResponse(
                    call: Call<T>,
                    response: Response<T>,
                ) {
                    val result = parseToApiResult(response)

                    callback.onResponse(this@ResponseCall, Response.success(result))
                }

                override fun onFailure(
                    call: Call<T>,
                    t: Throwable,
                ) {
                    val exception = checkCanceled(call, t)
                    callback.onResponse(
                        this@ResponseCall,
                        Response.success(NetworkResult.Failure(exception)),
                    )
                }
            },
        )
    }

    private fun checkCanceled(
        call: Call<T>,
        t: Throwable,
    ) = if (call.isCanceled) {
        TodokTodokExceptions.CancellationException
    } else {
        t.toDomain()
    }

    /**
     * Retrofit [Response] 를 [NetworkResult] 로 변환합니다.
     *
     * - HTTP 2xx + body 있음 → [NetworkResult.Success]
     * - HTTP 오류 응답 → [NetworkResult.ResponseException]
     *
     * @param response 원본 Retrofit 응답
     * @return 변환된 [NetworkResult]
     */
    private fun parseToApiResult(response: Response<T>): NetworkResult<T> {
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            val exceptionResponse = errorBody?.let { parseToResponse(it) }
            val exception =
                exceptionResponse?.let {
                    TodokTodokExceptions.from(it.code, it.message)
                } ?: TodokTodokExceptions.UnknownException(null)
            return NetworkResult.Failure(exception)
        }

        return response.body()?.let {
            NetworkResult.Success(it)
        } ?: when (response.code()) {
            204 -> NetworkResult.Success(Unit as T)
            else -> NetworkResult.Failure(TodokTodokExceptions.EmptyBodyException)
        }
    }

    /**
     * JSON 문자열을 [ExceptionResponse] 객체로 변환합니다.
     *
     * @param json 서버에서 반환된 JSON 문자열
     * @return 파싱 성공 시 [ExceptionResponse], 실패 시 null
     */
    private fun parseToResponse(json: String?): ExceptionResponse? =
        json?.let {
            runCatching {
                Json.decodeFromString<ExceptionResponse>(json)
            }.getOrNull()
        }

    /** 동기 실행은 지원하지 않음 */
    override fun execute(): Response<NetworkResult<T>?> = throw UnsupportedOperationException()

    /** 요청이 실행 중인지 여부 */
    override fun isExecuted(): Boolean = throw UnsupportedOperationException()

    /** 요청 취소 */
    override fun cancel() = proxy.cancel()

    /** 요청이 취소되었는지 여부 */
    override fun isCanceled(): Boolean = proxy.isCanceled

    /** 동일한 요청을 복제 */
    override fun clone(): Call<NetworkResult<T>> = ResponseCall(proxy.clone())

    /** OkHttp [Request] 객체 반환 */
    override fun request(): Request = proxy.request()

    /** 요청의 [Timeout] 반환 */
    override fun timeout(): Timeout = proxy.timeout()
}
