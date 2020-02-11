package io.github.armcha.coloredshadow

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.widget.ImageView
import io.github.armcha.coloredshadow.ShadowImageView.Companion.DOWNSCALE_FACTOR


object BlurShadow {

    private var renderScript: RenderScript? = null

    fun init(context: Context) {
        if (renderScript == null)
            renderScript = RenderScript.create(context)
    }

    fun blur(view: ImageView, width: Int, height: Int, radius: Float): Bitmap? {
        val src = getBitmapForView(view, DOWNSCALE_FACTOR, width, height) ?: return null
        val input = Allocation.createFromBitmap(renderScript, src)
        val output = Allocation.createTyped(renderScript, input.type)
        val script = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        } else {
            TODO("VERSION.SDK_INT < JELLY_BEAN_MR1")
        }
        script.apply {
            setRadius(radius)
            setInput(input)
            forEach(output)
        }
        output.copyTo(src)
        return src
    }

    private fun getBitmapForView(view: ImageView, downscaleFactor: Float, width: Int, height: Int): Bitmap? {
        val bitmap = Bitmap.createBitmap(
                (width * downscaleFactor).toInt(),
                (height * downscaleFactor).toInt(),
                Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        val matrix = Matrix()
        matrix.preScale(downscaleFactor, downscaleFactor)
        canvas.matrix = matrix
        view.draw(canvas)
        return bitmap
    }
}
