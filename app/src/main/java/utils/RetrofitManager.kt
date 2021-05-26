package utils

import com.google.gson.Gson
import contracts.BackendService
import constants.WebConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitManager {
    fun getWebAPI(gson: Gson? = null): BackendService { //TODO singleton
        val converterFactory = if (gson == null) {
            GsonConverterFactory.create()
        } else {
            GsonConverterFactory.create(gson)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(WebConstants.url)
            .addConverterFactory(converterFactory)
            .build()

        return retrofit.create(BackendService::class.java)
    }
}