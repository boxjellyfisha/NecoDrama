package com.kellyhong.necodrama.http

import com.google.gson.GsonBuilder
import com.kellyhong.necodrama.BuildConfig
import io.reactivex.schedulers.Schedulers
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Create, set, and hold a retrofit instance.<br></br>
 * Use Gson to serialize/deserialize parameter and response.<br></br>
 */

object EndPointGenerator {
    /**
     * Gson for retrofit. Add your custom TypeAdapter at [GsonTypeAdapterFactory]
     */
    private val gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapterFactory(GsonTypeAdapterFactory())
            .create()

    /**
     * Gson for GsonTypeAdapter parsing the raw data
     */
    internal var dataGson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()

    /**
     * for OkHttpClient logging
     */
    private val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClientBuilder = OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)

    private val retrofitBuilder = Retrofit.Builder()

    /**
     * retrofit instance
     */
	private const val SERVER_URL = "https://static.linetv.tw/"
    private var retrofit = retrofitBuilder
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

	/**
	 * Get api endpoint via retrofit. Define endpoint at [EndPointService]
	 *
	 * @param serviceClass endpoint you want to create.
	 * @return created endpoint.
	 */
	fun <E> createWithHeaderRx(header: Headers?, serviceClass: Class<E>): E {
		addHeaderInterceptor(header)
		addLogInterceptor()
		retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
		retrofit = retrofitBuilder.build()
		return retrofit.create(serviceClass)
	}

	private fun addLogInterceptor() {
		/* if Client builder did not contain logging interceptor, add one and rebuild retrofit. */
		if (BuildConfig.DEBUG && !okHttpClientBuilder.interceptors().contains(httpLoggingInterceptor)) {
			okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
			retrofitBuilder.client(okHttpClientBuilder.build())
			retrofit = retrofitBuilder.build()
		}
	}

	private fun addHeaderInterceptor(header: Headers?) {
		if(okHttpClientBuilder.interceptors().isEmpty())
			okHttpClientBuilder.addInterceptor(newInterceptorWithHeader(header))
		else {
			okHttpClientBuilder.interceptors()[0] =
					newInterceptorWithHeader(header)
		}
		retrofitBuilder.client(okHttpClientBuilder.build())
		retrofit = retrofitBuilder.build()
	}

	private fun newInterceptorWithHeader(header: Headers?): Interceptor {
		return Interceptor { chain ->
			try {
				val original: Request = chain.request()
				val request =
					if(header == null)
						original.newBuilder()
							.method(original.method(), original.body())
							.build()
					else
						original.newBuilder()
							.method(original.method(), original.body())
							.headers(header)
							.build()
				return@Interceptor chain.proceed(request)

			} catch (e: KotlinNullPointerException) {
				return@Interceptor null
			}
		}
	}
}
