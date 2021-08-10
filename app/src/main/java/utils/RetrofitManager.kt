package utils

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import contracts.BackendService
import constants.WebConstants
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitManager {
    val backendService: BackendService

    init {
        val contentType = "application/json".toMediaType()

        val converterFactory = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }.asConverterFactory(contentType)

        val retrofit = Retrofit.Builder()
            .baseUrl(WebConstants.url)
            .addConverterFactory(converterFactory)
            .build()

        backendService = retrofit.create(BackendService::class.java)
    }
}

//TODO rename java folder (root folder) to kotlin folder

//TODO progress bar on details
//TODO MVI
// загрузить 1 страницу откл инет и вклю обратано