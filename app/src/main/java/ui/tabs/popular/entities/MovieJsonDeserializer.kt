package ui.tabs.popular.entities

import android.os.Looper
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ui.tabs.pojo.Movie
import java.lang.reflect.Type

class MovieJsonDeserializer : JsonDeserializer<List<Movie>> {
    override fun deserialize(json: JsonElement?, type: Type?, context: JsonDeserializationContext?): List<Movie> {
        val jsonElement = json?.asJsonObject?.get("results")
        val result =  Gson().fromJson<List<Movie>>(jsonElement, type)

        println("qwerty size = ${result.size} first title = ${result.firstOrNull()?.title}")

        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            println("qwerty MovieJsonDeserializer is UI thread")
        } else {
            println("qwerty MovieJsonDeserializer is not UI thread")
        }

        return result
    }
}