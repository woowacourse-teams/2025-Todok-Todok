package com.team.data.network.adapter

import com.team.domain.model.exception.NetworkResult
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 * Retrofit [CallAdapter] 구현체로, 원본 [Call] 객체를 [ResponseCall]로 감싸
 * 서버 응답을 일관된 [NetworkResult] 타입으로 변환합니다.
 *
 * @param T 서버에서 반환되는 실제 데이터 타입
 * @property responseType 변환할 응답 타입 정보
 *
 */
class TodokTodokAdapter<T>(
    private val responseType: Type,
) : CallAdapter<T, Call<NetworkResult<T>>> {
    /** Retrofit이 요구하는 응답 타입 반환 */
    override fun responseType(): Type = responseType

    /** 원본 Call을 [ResponseCall]로 변환 */
    override fun adapt(call: Call<T>): Call<NetworkResult<T>> = ResponseCall(call)
}
