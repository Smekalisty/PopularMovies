package ui.tabs.pojo

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

open class Movie() : Parcelable {
    var id = 0
    var title = ""
    var overview: String? = null
    @SerializedName("vote_count") var voteCount: Long = 0
    @SerializedName("vote_average") var voteAverage: Float = 0f
    @SerializedName("backdrop_path") var backdropPath: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString() ?: ""
        overview = parcel.readString()
        voteCount = parcel.readLong()
        voteAverage = parcel.readFloat()
        backdropPath = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(overview)
        parcel.writeLong(voteCount)
        parcel.writeFloat(voteAverage)
        parcel.writeString(backdropPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}