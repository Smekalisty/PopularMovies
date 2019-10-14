package entities.helpers

import com.google.gson.Gson
import contracts.WebAPI
import entities.WebConstants
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitManager {
    fun getWebAPI(gson: Gson? = null): WebAPI {
        val converterFactory = if (gson == null) {
            GsonConverterFactory.create()
        } else {
            GsonConverterFactory.create(gson)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(WebConstants.url)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(WebAPI::class.java)
    }
}