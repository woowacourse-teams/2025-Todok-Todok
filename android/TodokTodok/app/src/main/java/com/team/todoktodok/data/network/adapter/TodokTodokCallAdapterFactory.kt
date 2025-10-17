package com.team.todoktodok.data.network.adapter

import com.team.domain.model.exception.NetworkResult
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.jvm.java

/**
 * Retrofit [CallAdapter.Factory] 구현체로, [NetworkResult] 타입을 반환하는
 * [Call]을 [ResponseCall]로 감싸는 커스텀 CallAdapter를 제공합니다.
 *
 * 이 Factory는 Retrofit에서 호출되는 모든 API 메서드의 반환 타입을 검사하여,
 * 반환 타입이 [Call]<[NetworkResult]<T>> 형태일 경우 [TodokTodokAdapter]를 반환합니다.
 *
 * @see TodokTodokAdapter
 */
class TodokTodokCallAdapterFactory : CallAdapter.Factory() {
    /**
     * Retrofit이 호출하는 각 API 메서드의 반환 타입을 기반으로
     * 적절한 [CallAdapter]를 생성합니다.
     *
     * @param returnType API 메서드의 반환 타입
     * @param annotations 메서드에 붙은 어노테이션
     * @param retrofit Retrofit 인스턴스
     * @return 반환 타입이 [Call]<[NetworkResult]<T>>이면 [TodokTodokAdapter], 아니면 null
     */
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation?>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        // API 메서드의 반환 타입이 Call인지 확인. 아니면 null 반환
        if (Call::class.java != getRawType(returnType)) return null

        // 반환 타입이 제네릭인지 확인. 아니면 예외 발생
        check(returnType is ParameterizedType)

        // Call<T>에서 T를 추출 (T는 NetworkResult<SuccessBody> 형태여야 함)
        val responseType = getParameterUpperBound(0, returnType)

        // 추출한 T의 실제 클래스가 NetworkResult인지 확인. 아니면 null 반환
        if (getRawType(responseType) != NetworkResult::class.java) return null

        // NetworkResult<SuccessBody>가 제네릭인지 확인. 아니면 예외 발생
        check(responseType is ParameterizedType)

        // NetworkResult<SuccessBody>에서 SuccessBody 타입을 추출
        val successBodyType = getParameterUpperBound(0, responseType)

        // TodokTodokAdapter를 생성해 반환 (ResponseCall을 통해 NetworkResult로 래핑)
        return TodokTodokAdapter<Any>(successBodyType)
    }
}
