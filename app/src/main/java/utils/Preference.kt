package utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.popularmovies.BuildConfig
import ui.tabs.pojo.MovieDetails

object Preference {
    private fun preferences(context: Context) = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    fun getFavoriteMovies(context: Context): MutableList<MovieDetails> = Gson().fromJson(preferences(context).getString("favoriteMovies", "[]"), object : TypeToken<MutableList<MovieDetails>>(){}.type)
    fun setFavoriteMovies(context: Context, value: MutableList<MovieDetails>) = preferences(context).edit().putString("favoriteMovies", Gson().toJson(value)).apply()
}