package entities.helpers

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import kotlin.random.Random

object Utils {
    fun generatePastelDrawable(text: String): GradientDrawable {
        val random = Random(text.hashCode())

        val red = (255 + random.nextInt(256)) / 2
        val green = (255  + random.nextInt(256)) / 2
        val blue = (255 + random.nextInt(256)) / 2

        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(Color.rgb(red, green, blue))
        }
    }
}