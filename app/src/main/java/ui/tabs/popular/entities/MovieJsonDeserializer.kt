package ui.tabs.popular.entities

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ui.tabs.pojo.Movie
import java.lang.reflect.Type

class MovieJsonDeserializer : JsonDeserializer<List<Movie>> {
    override fun deserialize(json: JsonElement?, type: Type?, context: JsonDeserializationContext?): List<Movie> {
        val jsonElement = json?.asJsonObject?.get("results")
        return Gson().fromJson(jsonElement, type)
    }
}