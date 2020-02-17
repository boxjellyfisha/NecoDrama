package com.kellyhong.necodrama.http

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import io.reactivex.Single
import io.reactivex.functions.Consumer
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 *  The parent class of API observer owner's onError consumer.
 *  implement function, normalErr, and deal with the specify error.
 */
abstract class ErrorConsumer: Consumer<Throwable> {

    abstract fun normalErr(error: BaseErrorException)
    override fun accept(throwable: Throwable) {
        if(throwable is BaseErrorException) {
            normalErr(throwable)
        } else {
            normalErr(BaseErrorException.OtherError)
        }
    }
}

object ErrorCatch {
    fun parsing(throwable: Throwable): Throwable
         = when(throwable) {
            is UnknownHostException,
            is SocketTimeoutException -> {
                Log.e(_tag, "Internet Error: time out")
                BaseErrorException.InternetError
            }
            is retrofit2.HttpException -> {
                checkError(throwable.response()) ?: BaseErrorException.OtherError
            }
            is JsonSyntaxException,
            is IllegalStateException -> {
                Log.e(_tag, "Json parse error:"+ throwable.message)
                BaseErrorException.OtherError
            }
            is BaseErrorException -> {
                throwable
            }
            else ->  {
                Log.e(_tag, throwable.message?:"No msg")
                BaseErrorException.OtherError
            }
        }

    private const val _tag = "ErrorConsumer"
    private const val _notFoundCode = 404
    private const val _httpErrorCode = 450
    private const val _serverErrorCode = 500

    private fun checkError(response: Response<*>?): Throwable? {
        if(response?.code() in _notFoundCode.._httpErrorCode || response?.code() == _serverErrorCode)
            return BaseErrorException.ServerError

        else if (response?.errorBody() != null) {
            val gson = Gson()
            try {
                return gson.fromJson(response.errorBody()?.charStream(), BaseErrorException::class.java)
            } catch (e: JsonIOException) {
                Log.e(_tag, e.message?:"No msg")

            } catch (e: JsonSyntaxException) {
                Log.e(_tag, e.message?:"No msg")
            }
        }
        return BaseErrorException.OtherError
    }
}

fun <T> Single<T>.catchError(): Single<T> {
    return this.onErrorResumeNext { err ->
        Single.error<T>(ErrorCatch.parsing(err))
    }
}