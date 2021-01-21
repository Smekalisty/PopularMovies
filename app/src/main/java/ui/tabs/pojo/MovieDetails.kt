package ui.tabs.pojo

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class MovieDetails() : Movie() {
    var budget = 0
    var homepage: String? = null
    @SerializedName("tagline") var tagLine = ""
    @SerializedName("release_date") var releaseDate = ""
    @SerializedName("poster_path") var posterPath: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString() ?: ""
        overview = parcel.readString()
        voteCount = parcel.readLong()
        voteAverage = parcel.readFloat()
        backdropPath = parcel.readString()
        budget = parcel.readInt()
        homepage = parcel.readString()
        tagLine = parcel.readString() ?: ""
        releaseDate = parcel.readString() ?: ""
        posterPath = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeInt(budget)
        parcel.writeString(homepage)
        parcel.writeString(tagLine)
        parcel.writeString(releaseDate)
        parcel.writeString(posterPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieDetails> {
        override fun createFromParcel(parcel: Parcel): MovieDetails {
            return MovieDetails(parcel)
        }

        override fun newArray(size: Int): Array<MovieDetails?> {
            return arrayOfNulls(size)
        }
    }
}