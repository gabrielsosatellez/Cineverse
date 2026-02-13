package com.project.cineversemobile.Util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

fun generateQrBitmap(content: String, size: Int = 300): Bitmap {
    val bitMatrix = MultiFormatWriter().encode(
        content,
        BarcodeFormat.QR_CODE,
        size,
        size
    )

    val bitmap = createBitmap(size, size, Bitmap.Config.RGB_565)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bitmap[x, y] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
        }
    }
    return bitmap
}